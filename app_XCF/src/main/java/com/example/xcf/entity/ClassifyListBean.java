package com.example.xcf.entity;

/**
 * 菜谱分类详情
 * Created by tarena on 2017/6/22.
 */

public class ClassifyListBean {
    /**
     * id : 1
     * name : 家常菜
     * parentId : 10001
     */

    private String id;
    private String name;
    private String parentId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
