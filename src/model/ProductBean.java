package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ProductBean {
	private int id;
	private String name;
	private double price;
	private java.util.Date make;
	private int expire;
	
	@Override
	public String toString() {
	
		return id+","+name+","+price+","+make+","+expire;
	}
	public ProductBean() {
		
	}
	public ProductBean(String line) throws IllegalArgumentException {
		if(line!=null) {
			String[] temp = line.split(",");
			if(temp!=null && temp.length==5) {
				this.id = ProductBean.convertInt(temp[0]);
				this.name = temp[1];
				this.price = ProductBean.convertDouble(temp[2]);
				this.make = ProductBean.convertDate(temp[3]);
				this.expire = ProductBean.convertInt(temp[4]);
			} else {
				throw new IllegalArgumentException("Wrong String Format : "+line);
			}
		}
	}
	private static SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static java.util.Date convertDate(String data) {
		java.util.Date result = null;
		try {
			result = sFormat.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
			result = new java.util.Date(0);
		}
		return result;
	}
	public static double convertDouble(String data) {
		double result = 0;
		try {
			result = Double.parseDouble(data);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			result = -1000;
		}
		return result;
	}
	public static int convertInt(String data) {
		int result = 0;
		try {
			result = Integer.parseInt(data);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			result = -1000;
		}
		return result;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public java.util.Date getMake() {
		return make;
	}
	public void setMake(java.util.Date make) {
		this.make = make;
	}
	public int getExpire() {
		return expire;
	}
	public void setExpire(int expire) {
		this.expire = expire;
	}
}
