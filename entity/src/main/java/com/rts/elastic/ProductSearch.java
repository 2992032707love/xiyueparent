package com.rts.elastic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductSearch extends BaseContent {
    public ProductSearch(String id, String name, BigDecimal price, String pic) {
        this.setId(id);
        this.name = name;
        this.price = price;
        this.pic = pic;
    }
    private String name;
    private BigDecimal price;
    private String pic;
}
