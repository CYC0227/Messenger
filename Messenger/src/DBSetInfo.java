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
		// Connection 객체를 자동완성으로 import할 때는 com.mysql.connection이 아닌
		// java 표준인 java.sql.Connection 클래스를 import해야 한다.
		Connection conn = null;
		Statement stmt = null;
		
		int userNumber = 0;
		
		PWGenerator pwg = new PWGenerator(pw);
		this.EncryptedPW = pwg.getEncryptedPW();
		
		try {
			// 1. 드라이버 로딩
			// 드라이버 인터페이스를 구현한 클래스를 로딩
			// mysql, oracle 등 각 벤더사 마다 클래스 이름이 다르다.
			// mysql은 "com.mysql.jdbc.Driver"이며, 이는 외우는 것이 아니라 구글링하면 된다.
			// 참고로 이전에 연동했던 jar 파일을 보면 com.mysql.jdbc 패키지에 Driver 라는 클래스가 있다.
			Class.forName("com.mysql.cj.jdbc.Driver");

			// 2. 연결하기
			// 드라이버 매니저에게 Connection 객체를 달라고 요청한다.
			// Connection을 얻기 위해 필요한 url 역시, 벤더사마다 다르다.
			// mysql은 "jdbc:mysql://localhost/사용할db이름" 이다.
			String url = "jdbc:mysql://localhost/messenger?serverTimezone=UTC";
 
			// @param getConnection(url, userName, password);
			// @return Connection
			conn = DriverManager.getConnection(url, "root", "asdfuiop196");
			System.out.println("연결 성공");

			String sql = "";
		//	sql = "insert into userinfo values(\"" + id + "," + EncryptedPW + "," + name + ","  + nickname + ","  + birthday + "," + phonenum + "," +homepage+ ", \" \")";
			
			sql = "insert into userinfo(id, pw, name, nickname, birthday, telephone, homepage, message) "
		      + "values('"+id+"', '"+EncryptedPW+"', '"+name+"', '"+nickname+"', '"+birthday+"', '"+phonenum+"', '"+homepage+"', '\" \"')";
			 stmt = conn.createStatement();
			 stmt.executeUpdate(sql);
			 
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러: " + e);
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