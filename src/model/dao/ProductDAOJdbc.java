package model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import model.ProductBean;
import model.ProductDAO;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;

public class ProductDAOJdbc implements ProductDAO {
	private DataSource dataSource;
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	private JdbcTemplate template;
	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}
	private static final String SELECT_BY_ID = 
			"select * from product where id = ?";
	@Override
	public ProductBean select(final int id) {
		PreparedStatementSetter stmtSetter = new PreparedStatementSetter() {
			public void setValues(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, id);
			}
		};
		ResultSetExtractor<ProductBean> rsetExtract = new ResultSetExtractor<ProductBean>() {
			public ProductBean extractData(ResultSet rset)
					throws SQLException, DataAccessException {
				ProductBean result = null;
				if(rset.next()) {
					result = new ProductBean();
					result.setId(rset.getInt("id"));
					result.setName(rset.getString("name"));
					result.setPrice(rset.getDouble("price"));
					result.setMake(rset.getDate("make"));
					result.setExpire(rset.getInt("expire"));
				}
				return result;
			}
		};
		return template.query(SELECT_BY_ID, stmtSetter, rsetExtract);
	}

	private static final String SELECT_ALL = 
			"select * from product";
	@Override
	public List<ProductBean> select() {
		ResultSetExtractor<List<ProductBean>> rsetExtract = new ResultSetExtractor<List<ProductBean>>() {
			public List<ProductBean> extractData(ResultSet rset)
					throws SQLException, DataAccessException {
				List<ProductBean> result = new ArrayList<ProductBean>();
				while(rset.next()) {
					ProductBean temp = new ProductBean();
					temp.setId(rset.getInt("id"));
					temp.setName(rset.getString("name"));
					temp.setPrice(rset.getDouble("price"));
					temp.setMake(rset.getDate("make"));
					temp.setExpire(rset.getInt("expire"));
					
					result.add(temp);
				}
				return result;
			}
		};
		return template.query(SELECT_ALL, rsetExtract);
	}
	private static final String INSERT = 
			"insert into product (id, name, price, make, expire) values (?, ?, ?, ?, ?)";
	@Override
	public ProductBean insert(final ProductBean bean) {
		int i = template.update(INSERT, new PreparedStatementSetter() {
			public void setValues(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, bean.getId());
				stmt.setString(2, bean.getName());
				stmt.setDouble(3, bean.getPrice());
				java.util.Date make = bean.getMake();
				if(make!=null) {
					long temp = make.getTime();
					stmt.setDate(4, new java.sql.Date(temp));
				} else {
					stmt.setDate(4, null);
				}
				stmt.setInt(5, bean.getExpire());
			}
		});
		
		if(i==1) {
			return this.select(bean.getId());
		} else {
			return null;
		}
	}
	
	private static final String UPDATE = 
			"update product set name=?, price=?, make=?, expire=? where id=?";
	@Override
	public ProductBean update(final String name, final double price,
			final java.util.Date make, final int expire, final int id) {
		int i = template.update(UPDATE, new PreparedStatementSetter() {
			public void setValues(PreparedStatement stmt) throws SQLException {
				stmt.setString(1, name);
				stmt.setDouble(2, price);
				if(make!=null) {
					long temp = make.getTime();
					stmt.setDate(3, new java.sql.Date(temp));
				} else {
					stmt.setDate(3, null);
				}
				stmt.setInt(4, expire);
				stmt.setInt(5, id);
			}
		});
		ProductBean result = null;
		if(i==1) {
			result = new ProductBean();
			result.setId(id);
			result.setName(name);
			result.setPrice(price);
			result.setMake(make);
			result.setExpire(expire);
		}
		return result;
	}
	
	private static final String DELETE = 
			"delete from product where id = ?";
	@Override
	public boolean delete(final int id) {
		int i = template.update(DELETE, new PreparedStatementSetter() {
			public void setValues(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, id);
			}
		});
		if(i==1) {
			return true;
		} else {
			return false;
		}
	}
	public static void main(String[] args) {
		ApplicationContext context =
				new ClassPathXmlApplicationContext("beans.config.xml");
		ProductDAO dao = (ProductDAO) context.getBean("productDAO");
		List<ProductBean> beans = dao.select();
		System.out.println(beans);
		
	}
}
