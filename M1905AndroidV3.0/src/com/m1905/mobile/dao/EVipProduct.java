package com.m1905.mobile.dao;

import java.util.ArrayList;
import java.util.List;

public class EVipProduct extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<VipProduct> lstVipProduct;

	public List<VipProduct> getLstVipProduct() {
		return lstVipProduct == null ? lstVipProduct = new ArrayList<VipProduct>()
				: lstVipProduct;
	}

	public void setLstVipProduct(List<VipProduct> lstVipProduct) {
		this.lstVipProduct = lstVipProduct;
	}

}
