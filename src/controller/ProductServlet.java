package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.ProductBean;
import model.ProductService;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@WebServlet(
		urlPatterns={"/pages/product.controller"} 
)
public class ProductServlet extends HttpServlet {
	private ProductService service = null;
	@Override
	public void init() throws ServletException {
		ServletContext application = this.getServletContext();
		ApplicationContext context = 
				WebApplicationContextUtils.getWebApplicationContext(application);
		service = (ProductService) context.getBean("productService");
	}
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doGET");
//接收資料
		String temp1 = request.getParameter("id");
		String name = request.getParameter("name");
		String temp2 = request.getParameter("price");
		String temp3 = request.getParameter("make");
		String temp4 = request.getParameter("expire");
		String prodaction = request.getParameter("prodaction");
		System.out.println("name="+name);
//驗證資料
		Map<String, String> errors = new HashMap<String, String>();
		request.setAttribute("errorMsgs", errors);
		
		if(prodaction!=null) {
			if(prodaction.equals("Insert") ||
					prodaction.equals("Update") ||
						prodaction.equals("Delete")) {
				if(temp1==null || temp1.length()==0) {
					errors.put("xxx", "Please enter Id");
				}
			}
		}

//轉換資料
		int id = 0;
		if(temp1!=null && temp1.length()!=0) {
			id = ProductBean.convertInt(temp1);
			if(id==-1000) {
				errors.put("xxx", "Id must be an integer");
			}
		}
		
		double price = 0;
		if(temp2!=null && temp2.length()!=0) {
			price = ProductBean.convertDouble(temp2);
			if(price==-1000) {
				errors.put("price", "Price must be a number");
			}
		}
		
		java.util.Date make = null;
		if(temp3!=null && temp3.length()!=0) {
			make = ProductBean.convertDate(temp3);
			if(make.equals(new java.util.Date(0))) {
				errors.put("make", "Make must be a date of the following format: yyyy-mm-dd");
			}
		}
		
		int expire = 0;
		if(temp4!=null && temp4.length()!=0) {
			expire = ProductBean.convertInt(temp4);
			if(expire==-1000) {
				errors.put("expire", "Expire must be an integer");
			}
		}
		
		if(errors!=null && !errors.isEmpty()) {
			request.getRequestDispatcher(
					"/pages/product.jsp").forward(request, response);
			return;
		}
		
//呼叫Model、根據Model執行結果導向View
		ProductBean bean = new ProductBean();
		bean.setId(id);
		bean.setName(name);
		bean.setPrice(price);
		bean.setMake(make);
		bean.setExpire(expire);

//		ProductService service = new ProductService();
		
		if(prodaction!=null && prodaction.equals("Select")) {
			List<ProductBean> result = service.select(bean);
			request.setAttribute("select", result);
			request.getRequestDispatcher(
					"/pages/display.jsp").forward(request, response);
			return;
		} else if(prodaction!=null && prodaction.equals("Insert")) {
			ProductBean result = service.insert(bean);
			if(result==null) {
				errors.put("action", "Insert has failed");
			} else {
				request.setAttribute("insert", result);
			}
			request.getRequestDispatcher(
					"/pages/product.jsp").forward(request, response);
			return;
		} else if(prodaction!=null && prodaction.equals("Update")) {
			ProductBean result = service.update(bean);
			if(result==null) {
				errors.put("action", "Update has failed");
			} else {
				request.setAttribute("update", result);
			}
			request.getRequestDispatcher(
					"/pages/product.jsp").forward(request, response);
			return;
		} else if(prodaction!=null && prodaction.equals("Delete")) {
			boolean result = service.delete(bean);
			request.setAttribute("delete", result);
			request.getRequestDispatcher(
					"/pages/product.jsp").forward(request, response);
			return;
		} else {
			errors.put("action", "Unknown Action:"+prodaction);
			request.getRequestDispatcher(
					"/pages/product.jsp").forward(request, response);
			return;
		}
	}
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
}
