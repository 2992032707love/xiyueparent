package com.rts.util;

import com.alibaba.fastjson.JSONObject;
import com.rts.elastic.BaseContent;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Component
public class ElasticUtil<T extends BaseContent>{
    @Resource
    RestHighLevelClient client;
    public void add(String index, T obj) throws IOException {
        IndexRequest request = new IndexRequest(index);
        request.id(obj.getId());
        request.source(JSONObject.toJSONString(obj), XContentType.JSON);
        client.index(request, RequestOptions.DEFAULT);
    }
    public void saveBatch(String index, List<T> list) throws IOException {
        BulkRequest bulkRequest = new BulkRequest(index);
        if (list != null) {
            for (T t : list) {
                IndexRequest indexRequest = new IndexRequest(index);
                indexRequest.id(t.getId());
                indexRequest.source(JSONObject.toJSONString(t), XContentType.JSON);
                bulkRequest.add(indexRequest);
            }
        }
        client.bulk(bulkRequest, RequestOptions.DEFAULT);
    }
    public void update(String index, T obj) throws IOException {
        UpdateRequest request = new UpdateRequest(index, obj.getId());
        request.doc(JSONObject.toJSONString(obj), XContentType.JSON);
        client.update(request, RequestOptions.DEFAULT);
    }
    public void del(String index, String id) throws IOException {
        DeleteRequest request = new DeleteRequest(index);
        request.id(id);
        client.delete(request, RequestOptions.DEFAULT);
    }
    public ElasticResult<T> search (String index, Integer pageNo, Integer pageSize, Map<String, String> search) throws IOException {
        SearchRequest request = new SearchRequest(index);
        HighlightBuilder highlightBuilder = new HighlightBuilder().requireFieldMatch(false);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        pageNo = pageNo < 0 ? 1 : pageNo;
        pageSize = pageSize < 0 ? 10 : pageSize;
        int start = (pageNo - 1) * pageSize;
        sourceBuilder.from(start < 0 ? 0 : start);
        sourceBuilder.size(pageSize < 0 ? 10 : pageSize);
        for (Map.Entry<String, String> entry : search.entrySet()) {
            highlightBuilder = highlightBuilder.field(entry.getKey()).preTags("<span style='color: #ff0000'>").postTags("</span>");
            sourceBuilder.query(QueryBuilders.matchQuery(entry.getKey(), entry.getValue()));
        }
        sourceBuilder.highlighter(highlightBuilder);
        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        SearchHit[] hitList = hits.getHits();
        List<T> list = new ArrayList<>();
        for (SearchHit hit : hitList) {
            T t = JSONObject.parseObject(hit.getSourceAsString(), clz);
            Map<String, HighlightField> map = hit.getHighlightFields();
           Field[] fields = clz.getDeclaredFields();
           for (Map.Entry<String, String> entry : search.entrySet()) {
               for (Field field : fields) {
                   if (map.containsKey(field.getName())) {
                       String content = map.get(field.getName()).fragments()[0].toString();
                       field.setAccessible(true);
                       field.set(t, content);
                   }
               }
           }
           list.add(t);
        }
        long total = hits.getTotalHits().value;
        long pages = total % pageSize > 0 ? total / pageSize + 1 : total / pageSize;
        ElasticResult<T> elasticResult = new ElasticResult(
                list,
                pageNo,
                pageSize,
                total,
                pages
        );
        return elasticResult;
    }
}
