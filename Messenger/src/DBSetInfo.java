import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

class PWGenerator {
	String EncryptedPW;
	PWGenerator(String pw) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		md.update(pw.getBytes());
		String hex1 = String.format("%064x", new BigInteger(1, md.digest()));
		this.EncryptedPW = hex1;
	}
	String getEncryptedPW(){
		return EncryptedPW;
	}
}

public class DBSetInfo {
	private String EncryptedPW;
	
	public DBSetInfo(String id, String pw, String name, String nickname, String birthday, String phonenum,
			String homepage) {
		// Connection ��ü�� �ڵ��ϼ����� import�� ���� com.mysql.connection�� �ƴ�
		// java ǥ���� java.sql.Connection Ŭ������ import�ؾ� �Ѵ�.
		Connection conn = null;
		Statement stmt = null;
		
		int userNumber = 0;
		
		PWGenerator pwg = new PWGenerator(pw);
		this.EncryptedPW = pwg.getEncryptedPW();
		
		try {
			// 1. ����̹� �ε�
			// ����̹� �������̽��� ������ Ŭ������ �ε�
			// mysql, oracle �� �� ������ ���� Ŭ���� �̸��� �ٸ���.
			// mysql�� "com.mysql.jdbc.Driver"�̸�, �̴� �ܿ�� ���� �ƴ϶� ���۸��ϸ� �ȴ�.
			// ����� ������ �����ߴ� jar ������ ���� com.mysql.jdbc ��Ű���� Driver ��� Ŭ������ �ִ�.
			Class.forName("com.mysql.cj.jdbc.Driver");

			// 2. �����ϱ�
			// ����̹� �Ŵ������� Connection ��ü�� �޶�� ��û�Ѵ�.
			// Connection�� ��� ���� �ʿ��� url ����, �����縶�� �ٸ���.
			// mysql�� "jdbc:mysql://localhost/�����db�̸�" �̴�.
			String url = "jdbc:mysql://localhost/messenger?serverTimezone=UTC";
 
			// @param getConnection(url, userName, password);
			// @return Connection
			conn = DriverManager.getConnection(url, "root", "asdfuiop196");
			System.out.println("���� ����");

			String sql = "";
		//	sql = "insert into userinfo values(\"" + id + "," + EncryptedPW + "," + name + ","  + nickname + ","  + birthday + "," + phonenum + "," +homepage+ ", \" \")";
			
			sql = "insert into userinfo(id, pw, name, nickname, birthday, telephone, homepage, message) "
		      + "values('"+id+"', '"+EncryptedPW+"', '"+name+"', '"+nickname+"', '"+birthday+"', '"+phonenum+"', '"+homepage+"', '\" \"')";
			 stmt = conn.createStatement();
			 stmt.executeUpdate(sql);
			 
		} catch (ClassNotFoundException e) {
			System.out.println("����̹� �ε� ����");
		} catch (SQLException e) {
			System.out.println("����: " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


}