package model;

import java.util.Arrays;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import model.dao.CustomerDAOJdbc;

public class CustomerService {
	private CustomerDAO dao = null;
	public CustomerService(CustomerDAO dao){
		this.dao=dao;
	}
	public CustomerBean login(String username, String password) {
		CustomerBean bean = dao.select(username);
		if(bean!=null) {
			if(password!=null) {
				byte[] pass = bean.getPassword();	//��Ʈw�x�s���K�X
				byte[] temp = password.getBytes();	//�ϥΪ̿�J���K�X
				if(Arrays.equals(pass, temp)) {
					return bean;
				}
			}
		}
		return null;
	}
	public boolean changePassword(
			String username, String oldPassword, String newPassword) {
		CustomerBean bean = this.login(username, oldPassword);
		if(bean!=null) {
			if(newPassword!=null) {
				CustomerBean result = dao.update(newPassword.getBytes(),
						bean.getEmail(), bean.getBirth(), username);
				if(result!=null) {
					return true;
				}
			}
		}
		return false;
	}
	public static void main(String[] args) {
		
		ApplicationContext context =
				new ClassPathXmlApplicationContext("beans.config.xml");

		CustomerService service = (CustomerService)context.getBean("customerService");
		
		CustomerBean bean = service.login("Alex", "A");
		System.out.println(bean);
//		CustomerService service = new CustomerService();
//	CustomerBean bean = service.login("Alex", "A");
//		System.out.println(bean);
		
//		service.changePassword("Alex", "ABC", "A");

	}
}
