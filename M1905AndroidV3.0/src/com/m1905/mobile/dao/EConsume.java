package com.m1905.mobile.dao;

import java.util.ArrayList;

/**
 * Created by m1905 on 14-2-8.
 */
public class EConsume extends Entity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Consume> consumes;
    public ArrayList<Consume> getConsumes() {
        return consumes==null?new ArrayList<Consume>():consumes;
    }

    public void setConsumes(ArrayList<Consume> consumes) {
        this.consumes = consumes;
    }
}
