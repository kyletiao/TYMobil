package com.m1905.mobile.common;

/**
 * 我的收藏
 * 
 * @author forcetech
 * 
 */
public class FavouriteContent extends DBContent {

    private boolean isChecked;

	public FavouriteContent(String id, int type, String title) {
		super(id, type, title);
	}

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
