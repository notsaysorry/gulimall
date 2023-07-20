package com.atguigu.gulimall.search.service.impl;

import com.atguigu.gulimall.search.config.ElasticSearchConfig;
import com.atguigu.gulimall.search.constant.ESConstant;
import com.atguigu.gulimall.search.service.SearchService;
import com.atguigu.gulimall.search.vo.SearchParam;
import com.atguigu.gulimall.search.vo.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    @Override
    public SearchResult search(SearchParam searchParam) {
        SearchResult searchResult = null;
        // 动态构建出查询需要的dsl语句
        // 构建检索请求
        SearchRequest searchRequest = buildSearchRequest(searchParam);

        try {
            // 执行检索请求
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);
            // 将检索结果封装为目标结果
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    private SearchRequest buildSearchRequest(SearchParam searchParam) {
        // 模糊匹配，过滤（按照属性，分类，品牌，价格区间，库存）
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 构建bool-query
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
        if (StringUtils.isNotBlank(searchParam.getKeyword())) {
            // 模糊匹配关键字
            boolQueryBuilder.must(QueryBuilders.matchQuery("skuTitle", searchParam.getKeyword()));
        }
        if (StringUtils.isNotBlank(searchParam.getCatalog3Id())) {
            // 过滤分类
            boolQueryBuilder.filter(QueryBuilders.termQuery("catalogId", searchParam.getCatalog3Id()));
        }
        if (null != searchParam.getBrandId() && searchParam.getBrandId().size() > 0) {
            // 过滤品牌
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId", searchParam.getBrandId()));
        }
        if (!Objects.isNull(searchParam.getHasStock())) {
            // 过滤库存
            boolQueryBuilder.filter(QueryBuilders.termQuery("hasStock", searchParam.getHasStock() == 1));
        }
        if (searchParam.getAttrs() != null && searchParam.getAttrs().size() > 0) {
            // 属性过滤
            searchParam.getAttrs().forEach(item -> {
                //attrs=1_5寸:8寸&2_16G:8G
                BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();

                //attrs=1_5寸:8寸
                String[] s = item.split("_");
                String attrId = s[0];
                String[] attrValues = s[1].split(":");//这个属性检索用的值
                nestedBoolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                nestedBoolQuery.must(QueryBuilders.termsQuery("attrs.attrValue", attrValues));

                NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("attrs", nestedBoolQuery, ScoreMode.None);
                boolQueryBuilder.filter(nestedQueryBuilder);
            });

        }
        if (StringUtils.isNotBlank(searchParam.getSkuPrice())) {
            // 价格区间
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
            String[] price = searchParam.getSkuPrice().split("_");
            if (price.length == 2) {
                rangeQuery.gte(price[0]);
                rangeQuery.lte(price[1]);
            } else if (price.length == 1) {
                if (searchParam.getSkuPrice().startsWith("_")) {
                    rangeQuery.lte(price[0]);
                } else {
                    rangeQuery.gte(price[0]);
                }
            }
            boolQueryBuilder.filter(rangeQuery);
        }

        searchSourceBuilder.query(boolQueryBuilder);

        // 排序，分页，高亮
        //排序
        //形式为sort=hotScore_asc/desc
        if (!StringUtils.isEmpty(searchParam.getSort())) {
            String sort = searchParam.getSort();
            String[] sortFileds = sort.split("_");

            SortOrder sortOrder = "asc".equalsIgnoreCase(sortFileds[1]) ? SortOrder.ASC : SortOrder.DESC;

            searchSourceBuilder.sort(sortFileds[0], sortOrder);
        }

        //分页
        searchSourceBuilder.from((searchParam.getPageNum() - 1) * ESConstant.PRODUCT_PAGESIZE);
        searchSourceBuilder.size(ESConstant.PRODUCT_PAGESIZE);

        //高亮
        if (!StringUtils.isEmpty(searchParam.getKeyword())) {

            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            searchSourceBuilder.highlighter(highlightBuilder);
        }

        String s = searchSourceBuilder.toString();
        System.out.println("构建的dsl:" + s);

        SearchRequest searchRequest = new SearchRequest(new String[]{ESConstant.INDEX_PRODUCT}, searchSourceBuilder);
        return searchRequest;
    }

}
