package com.spring.model;

import java.io.Serializable;

/**
 * Created by liangll18922 on 2017/1/12.
 */

public class Id implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static Long get(Id id) {
        return id == null ? null : id.getId();
    }

}
