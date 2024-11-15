package com.GDP.TaskMasterDemo.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserActions {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long userid;
	private String action;
	private String desctiption;
	
	public UserActions(Long long1, String action, String desctiption) {
		super();
		this.userid = long1;
		this.action = action;
		this.desctiption = desctiption;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getDesctiption() {
		return desctiption;
	}
	public void setDesctiption(String desctiption) {
		this.desctiption = desctiption;
	}
	@Override
	public String toString() {
		return "UserActions [id=" + id + ", userid=" + userid + ", action=" + action + ", desctiption=" + desctiption
				+ "]";
	}
	
}
