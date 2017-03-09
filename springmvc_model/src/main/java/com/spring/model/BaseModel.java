package com.spring.model;

import java.io.Serializable;

/**
 * Created by lili19289 on 2016/8/4.
 */
public class BaseModel implements Serializable {


    public long id;

    public long create_time = System.currentTimeMillis();

    public long lastModifyTime = System.currentTimeMillis();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
}
