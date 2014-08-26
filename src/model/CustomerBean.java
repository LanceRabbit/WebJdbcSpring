package model;

import java.io.UnsupportedEncodingException;

public class CustomerBean {
	private String custid;
	private byte[] password;
	private String email;
	private java.util.Date birth;
	@Override
	public String toString() {
		String pwd = null;
		try {
			pwd = new String(password, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return custid+","+email+","+birth+","+pwd;
	}
	public String getCustid() {
		return custid;
	}
	public void setCustid(String custid) {
		this.custid = custid;
	}
	public byte[] getPassword() {
		return password;
	}
	public void setPassword(byte[] password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public java.util.Date getBirth() {
		return birth;
	}
	public void setBirth(java.util.Date birth) {
		this.birth = birth;
	}
}
