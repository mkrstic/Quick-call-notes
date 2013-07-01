package com.mkrstic.callnotes.model;

import java.io.Serializable;

public class Call implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8155067488851085687L;
	private String phoneNumber;
	private String name;
	private long date;
	private long duration;
	private int type;
	

	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}

    public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}


    
}
