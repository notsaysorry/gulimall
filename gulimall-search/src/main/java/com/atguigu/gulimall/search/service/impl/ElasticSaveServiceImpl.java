package com.atguigu.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.gulimall.common.to.es.SkuEsModel;
import com.atguigu.gulimall.search.config.ElasticSearchConfig;
import com.atguigu.gulimall.search.constant.ESConstant;
import com.atguigu.gulimall.search.service.ElasticSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ElasticSaveServiceImpl implements ElasticSaveService {

    @Autowired
    private RestHighLevelClient client;

    @Override
    public boolean productUp(List<SkuEsModel> skuEsModels) throws IOException {
        boolean hasError = false;
        BulkRequest bulkRequest = new BulkRequest();
        skuEsModels.forEach(item -> {
            IndexRequest indexRequest = new IndexRequest(ESConstant.INDEX_PRODUCT);
            indexRequest.id(item.getSkuId());
            String s = JSON.toJSONString(item);
            indexRequest.source(s, XContentType.JSON);
            bulkRequest.add(indexRequest);
        });

        BulkResponse bulkResponse = client.bulk(bulkRequest, ElasticSearchConfig.COMMON_OPTIONS);
        hasError = bulkResponse.hasFailures();
        if (hasError){
            log.error("商品上架异常");
        }
        return false;
    }
}
