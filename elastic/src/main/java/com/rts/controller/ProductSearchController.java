package com.rts.controller;

import com.rts.common.ResultJson;
import com.rts.elastic.ProductSearch;
import com.rts.util.ElasticUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class ProductSearchController {
    @Resource
    ElasticUtil<ProductSearch> elasticUtil;
    @GetMapping("/product")
    ResultJson serchProduct (Integer pageNo, Integer pageSize, String name) throws IOException {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isNotBlank(name)) {
            map.put("name", name);
        }
        return ResultJson.success(elasticUtil.search("product", pageNo, pageSize, map, ProductSearch.class));
    }
}
