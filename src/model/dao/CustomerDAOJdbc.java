package model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import model.CustomerBean;
import model.CustomerDAO;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;

public class CustomerDAOJdbc implements CustomerDAO {
	private DataSource dataSource;
	private JdbcTemplate template;
	
	public void setTemplate(JdbcTemplate template) {
		this.template = template;
//		this.template = new JdbcTemplate(dataSource);
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	//constructor setting
//	public CustomerDAOJdbc () {
//		DriverManagerDataSource dmds = new DriverManagerDataSource();
//		dmds.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//		dmds.setUrl("jdbc:sqlserver://localhost:1433;database=JDBC");
//		dmds.setUsername("sa");
//		dmds.setPassword("passw0rd");
//		this.dataSource = dmds;
//	}

	private static final String UPDATE =
			"update customer set password=?, email=?, birth=? where custid=?";
	@Override
	public CustomerBean update(final byte[] password,
			final String email, final java.util.Date birth, final String custid) {
		CustomerBean result = null;
		int i = template.update(UPDATE, new PreparedStatementSetter(){
			@Override
			public void setValues(PreparedStatement stmt) throws SQLException {
				stmt.setBytes(1, password);
				stmt.setString(2, email);
				if(birth!=null) {
					long time = birth.getTime();
					stmt.setDate(3, new java.sql.Date(time));
				} else {
					stmt.setDate(3, null);
				}
				stmt.setString(4, custid);
			}
		});
		
		if(i==1) {
			return this.select(custid);
		} else {
			return null;
		}
	}
	
	private static final String SELECT_BY_CUSTID = 
			"select * from customer where custid = ?";
	@Override
	public CustomerBean select(final String custid) {
		PreparedStatementSetter stmtSetter = new PreparedStatementSetter() {
			public void setValues(PreparedStatement stmt) throws SQLException {
				stmt.setString(1, custid);
			}
		};
		ResultSetExtractor<CustomerBean> rsetExtract = new ResultSetExtractor<CustomerBean>() {
			public CustomerBean extractData(ResultSet rset)
					throws SQLException, DataAccessException {
				CustomerBean result = null;
				if(rset.next()) {
					result = new CustomerBean();
					result.setCustid(rset.getString("custid"));
					result.setPassword(rset.getBytes("password"));
					result.setEmail(rset.getString("email"));
					result.setBirth(rset.getDate("birth"));
				}
				return result;
			}
		};
		return template.query(SELECT_BY_CUSTID, stmtSetter, rsetExtract);
	}
	public static void main(String[] args) {
//		CustomerDAOJdbc dao = new CustomerDAOJdbc();
//		
		ApplicationContext context =
				new ClassPathXmlApplicationContext("beans.config.xml");
		CustomerDAO dao = (CustomerDAO)  context.getBean("customerDao");
		 
//		CustomerBean bean = dao.select("Alex");
		CustomerBean bean =dao.update(new byte[] {65},
				"alex@iii.org.tw", new java.util.Date(), "Alex");
		System.out.println(bean);
//		dao.update(new byte[] {65},
//				"alex@iii.org.tw", new java.util.Date(), "Alex");
		
		
		
	}
}
