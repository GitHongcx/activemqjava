package com.hcx.activemq.model;

import java.io.Serializable;

/**
 * Created by hongcaixia on 2019/7/14.
 */
public class Person implements Serializable{

    private static final long serialVersionUID = 2785000655132561850L;

    private String name;
    private String phone;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
