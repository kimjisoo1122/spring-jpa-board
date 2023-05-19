package com.example.board.util;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PageHandler {

    private int nowPage;
    private int totalPage;
    private int pageSize;
    private int navSize;
    private int startPage;
    private int enePage;
    private boolean isPrevious;
    private boolean isNext;


    public PageHandler(Page page, int navSize) {
        this.nowPage = page.getNumber();
        this.pageSize = page.getSize();
        this.totalPage = page.getTotalPages();
        this.navSize = navSize;
        this.startPage = ((nowPage - 1) / pageSize) * pageSize + 1;
        this.enePage = Math.min((startPage + page.getSize()) - 1, totalPage);
        this.isPrevious = startPage > 1;
        this.isNext = enePage < totalPage;
    }
}
