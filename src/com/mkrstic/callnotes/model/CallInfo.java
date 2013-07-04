package com.mkrstic.callnotes.model;

import java.io.Serializable;

public class CallInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8155067488851085687L;
	private String phoneNumber;
	private String contactName;
	private long dateTimeInMillis;
	private int durationInSeconds;
	private int type;
	private String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public long getDateTimeInMillis() {
		return dateTimeInMillis;
	}
	public void setDateTimeInMillis(long date) {
		this.dateTimeInMillis = date;
	}
	public int getDurationInSeconds() {
		return durationInSeconds;
	}
	public void setDurationInSeconds(int durationInSeconds) {
		this.durationInSeconds = durationInSeconds;
	}

    public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}


    
}
