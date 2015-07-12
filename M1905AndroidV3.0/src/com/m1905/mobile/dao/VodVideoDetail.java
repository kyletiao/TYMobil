package com.m1905.mobile.dao;


public class VodVideoDetail extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;//内容ID
	private String img;//图片URL
	private String title;//标题
	private String actor;//主演
	private String director;//导演
	private String mtype;//影片类型
	private String years;//上映时间
	private String description;//简介
	private String area;//地区
	private int duration;//播放时长
	private float score;//评分
	private String stageurl;//剧照
	private String playurl;//播放地址
	
	public String getStageurl() {
		return stageurl;
	}
	public void setStageurl(String stageurl) {
		this.stageurl = stageurl;
	}
	public int getId() {
		return id;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getActor() {
		return actor;
	}
	public void setActor(String actor) {
		this.actor = actor;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	
	public String getMtype() {
		return mtype;
	}
	public void setMtype(String mtype) {
		this.mtype = mtype;
	}
	public String getYears() {
		return years;
	}
	public void setYears(String years) {
		this.years = years;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public String getPlayurl() {
		return playurl;
	}
	public void setPlayurl(String playurl) {
		this.playurl = playurl;
	}
	
	
}
