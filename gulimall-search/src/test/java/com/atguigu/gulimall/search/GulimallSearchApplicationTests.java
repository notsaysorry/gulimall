package com.atguigu.gulimall.search;

import com.alibaba.fastjson.JSON;
import com.atguigu.gulimall.search.config.ElasticSearchConfig;
import lombok.Data;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootTest
class GulimallSearchApplicationTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void test1(){
        System.out.println(restHighLevelClient);
    }

    @Test
    public void test2() throws IOException {
        IndexRequest request = new IndexRequest("user");
        User user = new User();
        user.setName("小明");
        user.setAge(20);
        String userStr = JSON.toJSONString(user);
        request.source(userStr, XContentType.JSON);
        IndexResponse indexResponse = restHighLevelClient.index(request, ElasticSearchConfig.COMMON_OPTIONS);
        System.out.println(indexResponse);
    }

    @Data
    class User{
        private String name;
        private Integer age;
    }


    @Test
    public void searchData() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));
        searchSourceBuilder.aggregation(AggregationBuilders.terms("ageAgg").field("age"));
        searchSourceBuilder.aggregation(AggregationBuilders.avg("balanceAgg").field("balance"));
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);

        SearchHits hits = searchResponse.getHits();
        SearchHit[] hitsData = hits.getHits();
        for (SearchHit hit : hitsData) {
            String sourceAsString = hit.getSourceAsString();
            System.out.println(sourceAsString);
        }
        System.out.println("-----------------------------------------------");
        Aggregations aggregations = searchResponse.getAggregations();
        Terms ageAgg = aggregations.get("ageAgg");
        Avg balanceAgg = aggregations.get("balanceAgg");
        List<? extends Terms.Bucket> aggBuckets = ageAgg.getBuckets();
        aggBuckets.forEach(item -> {
            System.out.println(item.getKey() + ":" + item.getDocCount());
        });
        double avgValue = balanceAgg.getValue();
        System.out.println(avgValue);

    }


}
