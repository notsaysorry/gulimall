package com.atguigu.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.gulimall.product.service.PmsCategoryBrandRelationService;
import com.atguigu.gulimall.product.vo.Catalog2Vo;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.product.dao.PmsCategoryDao;
import com.atguigu.gulimall.product.entity.PmsCategoryEntity;
import com.atguigu.gulimall.product.service.PmsCategoryService;


@Service("pmsCategoryService")
public class PmsCategoryServiceImpl extends ServiceImpl<PmsCategoryDao, PmsCategoryEntity> implements PmsCategoryService {

    @Autowired
    private PmsCategoryBrandRelationService pmsCategoryBrandRelationService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsCategoryEntity> page = this.page(
                new Query<PmsCategoryEntity>().getPage(params),
                new QueryWrapper<PmsCategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<PmsCategoryEntity> listWithTree() {
        // 查询所有的分类
        List<PmsCategoryEntity> categoryList = baseMapper.selectList(null);
        // 为所有的一级分类添加子分类
        List<PmsCategoryEntity> listWithTree = categoryList.stream().filter(menu -> menu.getParentCid().equals("0"))
                .map(menu -> {
                    menu.setChildren(childrenCategory(menu, categoryList));
                    return menu;
                })
                .sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort())))
                .collect(Collectors.toList());

        return listWithTree;
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        // TODO 判断菜单是否被其他地方引用

        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public String[] queryCatelogs(String catalogId) {
        List<String> catelogs = new ArrayList<>();
        catelogs = queryParentCatelogs(catalogId, catelogs);
        Collections.reverse(catelogs);
        return catelogs.toArray(new String[catelogs.size()]);
    }

    @Override
    public void updateDetail(PmsCategoryEntity pmsCategory) {
        this.updateById(pmsCategory);
        if (StringUtils.isNotBlank(pmsCategory.getName())) {
            pmsCategoryBrandRelationService.updateCategory(pmsCategory.getCatId(), pmsCategory.getName());
        }
    }


    @Cacheable(value = {"category"}, key="#root.methodName") // 设置缓存的数据到一个分区。当前方法的结果需要缓存，如果缓存中有，方法不用调用；如果缓存中没有，会调用这个方法
    @Override
    public List<PmsCategoryEntity> getLevel1Category() {
        System.out.println("查询了");
        List<PmsCategoryEntity> categoryEntities = this.baseMapper.selectList(new QueryWrapper<PmsCategoryEntity>().eq("parent_cid", "0"));
        return categoryEntities;
    }

    @Override
    @Cacheable(value = {"category"}, key="#root.methodName")
    public Map<String, List<Catalog2Vo>> catalogJson() {
        return getCatalogJsonFromDb2();
    }

    private Map<String, List<Catalog2Vo>> getCatalogJsonFromDBRedissonLock() {
        // 获得分布式锁
        RLock catalogJsonLock = redissonClient.getLock("catalogJsonLock");
        catalogJsonLock.lock(10, TimeUnit.SECONDS);
        Map<String, List<Catalog2Vo>> catalogJsonFromDb;
        try {
            catalogJsonFromDb = getCatalogJsonFromDb2();
        } finally {
            catalogJsonLock.unlock();
        }
        return catalogJsonFromDb;
    }

    private Map<String, List<Catalog2Vo>> getCatalogJsonFromDBRedisLock() {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String uuid = UUID.randomUUID().toString();
        Boolean lock = ops.setIfAbsent("lock", uuid, 100, TimeUnit.SECONDS);
        if (lock) {
            Map<String, List<Catalog2Vo>> catalogJsonFromDb;
            try {
                catalogJsonFromDb = getCatalogJsonFromDb();
            } finally {
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                //删除锁
                redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class)
                        , Arrays.asList("lock"), uuid);
            }
            return catalogJsonFromDb;
        } else {
            // 自旋重试再获得锁
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getCatalogJsonFromDBRedisLock();
        }
    }

    private Map<String, List<Catalog2Vo>> getCatalogJsonFromDb() {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String catalogJson = ops.get("catalogJson");
        if (org.apache.commons.lang3.StringUtils.isNotBlank(catalogJson)) {
            return JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catalog2Vo>>>() {
            });
        }
        System.out.println("命中数据库。。");
        // 查询所有的分类
        List<PmsCategoryEntity> allCategoryEntities = this.baseMapper.selectList(null);

        List<PmsCategoryEntity> level1Categorys = getLevel1Category();
        Map<String, List<Catalog2Vo>> Catalog2VoMaps = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId(), v -> {
            List<PmsCategoryEntity> catalog2Levels = getCategoryByPId(allCategoryEntities, v.getCatId());

            List<Catalog2Vo> catalog2Vos = catalog2Levels.stream().map(l2 -> {
                Catalog2Vo catalog2Vo = new Catalog2Vo(v.getCatId(), null, l2.getCatId(), l2.getName());
                List<PmsCategoryEntity> catalog3Levels = getCategoryByPId(allCategoryEntities, l2.getCatId());
                if (catalog3Levels != null && catalog2Levels.size() > 0) {
                    List<Catalog2Vo.Catalog3Vo> Catalog3Vo = catalog3Levels.stream().map(l3 -> new Catalog2Vo.Catalog3Vo(l2.getCatId(), l3.getCatId(), l3.getName()))
                            .collect(Collectors.toList());
                    catalog2Vo.setCatalog3List(Catalog3Vo);
                }
                return catalog2Vo;
            }).collect(Collectors.toList());
            return catalog2Vos;
        }));
        ops.set("catalogJson", JSON.toJSONString(Catalog2VoMaps));
        return Catalog2VoMaps;
    }

    public Map<String, List<Catalog2Vo>> getCatalogJsonFromDb2() {
        System.out.println("命中数据库。。");
        // 查询所有的分类
        List<PmsCategoryEntity> allCategoryEntities = this.baseMapper.selectList(null);

        List<PmsCategoryEntity> level1Categorys = getLevel1Category();
        Map<String, List<Catalog2Vo>> Catalog2VoMaps = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId(), v -> {
            List<PmsCategoryEntity> catalog2Levels = getCategoryByPId(allCategoryEntities, v.getCatId());

            List<Catalog2Vo> catalog2Vos = catalog2Levels.stream().map(l2 -> {
                Catalog2Vo catalog2Vo = new Catalog2Vo(v.getCatId(), null, l2.getCatId(), l2.getName());
                List<PmsCategoryEntity> catalog3Levels = getCategoryByPId(allCategoryEntities, l2.getCatId());
                if (catalog3Levels != null && catalog2Levels.size() > 0) {
                    List<Catalog2Vo.Catalog3Vo> Catalog3Vo = catalog3Levels.stream().map(l3 -> new Catalog2Vo.Catalog3Vo(l2.getCatId(), l3.getCatId(), l3.getName()))
                            .collect(Collectors.toList());
                    catalog2Vo.setCatalog3List(Catalog3Vo);
                }
                return catalog2Vo;
            }).collect(Collectors.toList());
            return catalog2Vos;
        }));
        return Catalog2VoMaps;
    }

    private Map<String, List<Catalog2Vo>> getCatalogJsonFromDBLocateLock() {
        synchronized (this) {
            return getCatalogJsonFromDb();
        }
    }

    private List<PmsCategoryEntity> getCategoryByPId(List<PmsCategoryEntity> allCategoryEntities, String pId) {
        List<PmsCategoryEntity> collect = allCategoryEntities.stream().filter(item -> item.getParentCid().equals(pId)).collect(Collectors.toList());
        return collect;
    }

    public List<String> queryParentCatelogs(String catalogId, List<String> catelogs) {
        catelogs.add(catalogId);
        PmsCategoryEntity pmsCategoryEntity = baseMapper.selectById(catalogId);
        if (!pmsCategoryEntity.getParentCid().equals("0")) {
            queryParentCatelogs(pmsCategoryEntity.getParentCid(), catelogs);
        }
        return catelogs;
    }


    private List<PmsCategoryEntity> childrenCategory(PmsCategoryEntity root, List<PmsCategoryEntity> all) {
        return all.stream().filter(menu -> menu.getParentCid().equals(root.getCatId()))
                .map(menu -> {
                    menu.setChildren(childrenCategory(menu, all));
                    return menu;
                })
                .sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort())))
                .collect(Collectors.toList());
    }

}