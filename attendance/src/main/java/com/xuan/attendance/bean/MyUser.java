package com.xuan.attendance.bean;

import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String  superidUid;

	public String getSuperidUid() {
		return superidUid;
	}

	public void setSuperidUid(String superidUid) {
		this.superidUid = superidUid;
	}




}
