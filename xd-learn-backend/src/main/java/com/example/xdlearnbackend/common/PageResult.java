package com.example.xdlearnbackend.common;

import lombok.Data;

import java.util.List;

/**分页结果封装类
 * 用于封装分页查询结果
 * @author 木又
 * @date 2026/5/15 15:00
 */
@Data
public class PageResult <T>{
    /**
     * 当前页数据
     */
    private List<T> data;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 当前页码
     */
    private int pageNum;

    /**
     * 每页条数
     */
    private int pageSize;

    /**
     * 总页数
     */
    private int totalPages;

    /**
     * 是否有下一页
     */
    private boolean hasNextPage;

    /**
     * 是否有上一页
     */
    private boolean hasPreviousPage;

    /**
     * 构造方法
     * @param data 当前页数据
     * @param total 总记录数
     * @param pageNum 当前页码
     * @param pageSize 每页条数
     */
    public PageResult(List<T> data, long total, int pageNum, int pageSize) {
        this.data = data;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) total / pageSize);
        this.hasNextPage = pageNum < totalPages;
        this.hasPreviousPage = pageNum > 1;
    }

}
