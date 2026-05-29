package com.example.xdlearnbackend.dto;

import lombok.Data;

/**
 * @author 木又
 * @date 2026/5/17 15:54
 */
@Data
public class QueryResult {

    private int pageNum=1;

    private int pageSize=9;

    private String orderBy;

    private String orderDirection;


    public int getoffset() {
        return (pageNum - 1) * pageSize;
    }
}
