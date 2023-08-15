package com.rts.test;

import com.alibaba.fastjson.JSONObject;
import com.rts.ElasticApp;
import com.rts.elastic.ProductSearch;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticApp.class)
public class ElasticTest {
    @Resource
    RestHighLevelClient client;
    @Test
    public void index() throws IOException {

        // 表名
        CreateIndexRequest request = new CreateIndexRequest("product");
        request.mapping("{\n" +
                "    \"properties\": {\n" +
                "      \"name\": {\n" +
                "        \"type\": \"text\",\n" +
                "        \"analyzer\": \"ik_max_word\"\n" +
                "      },\n" +
                "      \"price\": {\n" +
                "        \"type\": \"float\"\n" +
                "      },\n" +
                "      \"pic\": {\n" +
                "        \"type\": \"keyword\"\n" +
                "      }\n" +
                "    }\n" +
                "  }", XContentType.JSON);
        client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println("执行成功");
    }

    @Test
    public void add() throws IOException {
        ProductSearch productSearch = new ProductSearch();
        productSearch.setId("1");
        productSearch.setName("荣耀Play5T 22.5W超级快充 5000mAh大电池 6.5英寸护眼屏 全网通8GB+128GB");
        productSearch.setPrice(new BigDecimal("999.00"));
        productSearch.setPic("https://img13.360buyimg.com/n7/jfs/t1/201578/31/15673/77560/619479ceEd1bde507/c0dab826b71e0b84.jpg");
        IndexRequest indexRequest = new IndexRequest("product");
        indexRequest.id(String.valueOf(productSearch.getId()));
        indexRequest.source(JSONObject.toJSONString(productSearch), XContentType.JSON);

        client.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println("添加成功，添加的内容是 =====> " + JSONObject.toJSONString(productSearch));
    }

    @Test
    public void batchAdd () throws IOException {
        List<ProductSearch> list = new ArrayList<>();
        list.add(new ProductSearch("2",
                "荣耀play5t 手机 老人机 学生智能机 极光蓝 8+128GB 全网通",
                new BigDecimal("929.00"),
                "https://img11.360buyimg.com/n7/jfs/t1/123644/19/34911/105872/63ec3703F1fd9ea6b/607104df6e73e403.png"));
        list.add(new ProductSearch("3",
                "荣耀play5T 5000mAh大电池 22.5w闪充 6.5英寸护眼屏 新品手机 幻夜黑8G+128G",
                new BigDecimal("989.00"),
                "https://img11.360buyimg.com/n7/jfs/t1/123644/19/34911/105872/63ec3703F1fd9ea6b/607104df6e73e403.png"));
        list.add(new ProductSearch("4",
                "荣耀Play5T 4G手机 极光蓝 8G+128G 官方标配【配90天碎屏险】",
                new BigDecimal("938.00"),
                "https://img11.360buyimg.com/n7/jfs/t1/123644/19/34911/105872/63ec3703F1fd9ea6b/607104df6e73e403.png"));
        list.add(new ProductSearch("5",
                "荣耀X30 骁龙6nm疾速5G芯 66W超级快充 120Hz全视屏 全网通版 8GB+128GB",
                new BigDecimal("1399.00"),
                "https://img11.360buyimg.com/n7/jfs/t1/123644/19/34911/105872/63ec3703F1fd9ea6b/607104df6e73e403.png"));
        list.add(new ProductSearch("6",
                "荣耀X40 120Hz OLED硬核曲屏 5100mAh 快充大电池 7.9mm轻薄设计 5G手机",
                new BigDecimal("1699.00"),
                "https://img11.360buyimg.com/n7/jfs/t1/123644/19/34911/105872/63ec3703F1fd9ea6b/607104df6e73e403.png"));
        list.add(new ProductSearch("7",
                "荣耀80 1.6亿像素超清主摄 AI Vlog视频大师 全新Magic OS 7.0系统 5G手机",
                new BigDecimal("2799.00"),
                "https://img11.360buyimg.com/n7/jfs/t1/123644/19/34911/105872/63ec3703F1fd9ea6b/607104df6e73e403.png"));
        list.add(new ProductSearch("8",
                "荣耀畅玩20 5000mAh超大电池续航 6.5英寸大屏 莱茵护眼 4GB+64GB 全网通 钛空",
                new BigDecimal("749.00"),
                "https://img11.360buyimg.com/n7/jfs/t1/123644/19/34911/105872/63ec3703F1fd9ea6b/607104df6e73e403.png"));
        BulkRequest bulkRequest = new BulkRequest();
        for (ProductSearch productSearch : list) {
            IndexRequest indexRequest = new IndexRequest("product");
            indexRequest.id(String.valueOf(productSearch.getId()));
            indexRequest.source(JSONObject.toJSONString(productSearch), XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println("批量添加成功");

    }

    @Test
    public void getAll() throws IOException {
        SearchRequest request = new SearchRequest("product");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        builder.from(1);
        builder.size(4);
        request.source(builder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        SearchHit[] hitList = hits.getHits();
        for (SearchHit hit : hitList) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    @Test
    public void search() throws IOException {
        SearchRequest request = new SearchRequest("product");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchQuery("name", "X30"));
        request.source(builder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        SearchHit[] hitList = hits.getHits();
        for (SearchHit hit : hitList) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    @Test
    public void highlightSearch () throws IOException {
        SearchRequest searchRequest = new SearchRequest("product");
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.requireFieldMatch(false).field("name").preTags("<span style='color: #e4393c'>").postTags("</span>");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchQuery("name", "荣耀手机")).highlighter(highlightBuilder);
        searchRequest.source(builder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        SearchHit[] hitList = hits.getHits();
        List<ProductSearch> list = new ArrayList<>();
        for (SearchHit hit : hitList) {
            ProductSearch productSearch = JSONObject.parseObject(hit.getSourceAsString(), ProductSearch.class);
//            System.out.println(hit.getSourceAsString());
            Map<String, HighlightField> map = hit.getHighlightFields();
            if (map.containsKey("name")) {
                String name = map.get("name").fragments()[0].toString();
                productSearch.setName(name);
            }
            list.add(productSearch);
//            System.out.println(map.get("name").fragments()[0].toString());
        }
        System.out.println(list);
    }
}
