package com.tarea.tarea.util;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class PageRender <T>{

    private String url;
    private Page<T> page;
    private int totalPages;
    private int elementsPerPage;
    private int actualPage;
    private List<PageItem> pages;

    //Constructor
    public PageRender(String url, Page<T> page) {
        this.url = url;
        this.page = page;
        this.pages = new ArrayList<>();
        elementsPerPage = 6; //es el numero de paginas que declaramos anteriormente en el Controller del cliente
        totalPages = page.getTotalPages();
        actualPage = page.getNumber() + 1;
        int desde, hasta;
        if (totalPages <= elementsPerPage){
            desde = 1;
            hasta = totalPages;
        } else {
            if (actualPage <= elementsPerPage /2){
                desde = 1;
                hasta = totalPages;
            } else if (actualPage >= totalPages - elementsPerPage){
                desde = totalPages - elementsPerPage + 1;
                hasta = elementsPerPage;
            } else {
                desde = actualPage - elementsPerPage /2;
                hasta = elementsPerPage;
            }
        }
        for (int i = 0; i < hasta; i++){
            pages.add(new PageItem(desde + i, actualPage == desde + i));
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getActualPage() {
        return actualPage;
    }

    public void setActualPage(int actualPage) {
        this.actualPage = actualPage;
    }

    public List<PageItem> getPages() {
        return pages;
    }

    public void setPages(List<PageItem> pages) {
        this.pages = pages;
    }

    public boolean isFirst(){
        return page.isFirst();
    }

    public boolean isLast(){
        return page.isLast();
    }

    public boolean isHasNext(){
        return page.hasNext();
    }
    public boolean isHasPrevious(){
        return page.hasPrevious();
    }
}
