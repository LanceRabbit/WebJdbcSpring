package model;


public interface CustomerDAO {

	public abstract CustomerBean update(
			byte[] password, String email, java.util.Date birth, String custid);
	
	public abstract CustomerBean select(String custid);

}