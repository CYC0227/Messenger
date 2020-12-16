import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

class User{
	String id;
	String pw;
	String name;
	String nickname;
	String birthday;
	String telephone;
	String homepage;
	String message;
	String isOn;
	public User(String id,String pw,String name,String nickname,
			String birthday,String telephone,String homepage,String message) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.nickname = nickname;
		this.birthday = birthday;
		this.telephone =telephone;
		this.homepage = homepage;
		this.message = message;
		
	}
}

public class DBGetInfo {

	User[] uInfo = null;
	int userNumber = 0;
	public User[] getUserInfo(){
		return uInfo;
	}
	public int getUserNum(){
		return userNumber;
	}
	public DBGetInfo() {
		// Connection 객체를 자동완성으로 import할 때는 com.mysql.connection이 아닌
		// java 표준인 java.sql.Connection 클래스를 import해야 한다.
		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;

		ResultSet rs = null;
		ResultSet rs2 = null;

		
		int userNumber = 0;
		
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
			String sql2 = "";
			
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();

				sql = "SELECT * from userinfo";

				// 5. 쿼리 수행
				// 레코드들은 ResultSet 객체에 추가된다.
				rs = stmt.executeQuery(sql);
				rs2 = stmt2.executeQuery(sql);
				
				while (rs2.next()) {
					userNumber++;
				}
				
				this.userNumber = userNumber;
				
				uInfo = new User[userNumber];
				
				int cnt = 0;
				while (rs.next()) {
					// 레코드의 칼럼은 배열과 달리 0부터 시작하지 않고 1부터 시작한다.
					// 데이터베이스에서 가져오는 데이터의 타입에 맞게 getString 또는 getInt 등을 호출한다.
					uInfo[cnt] = new User(rs.getString(1), rs.getString(2),rs.getString(3),rs.getString(4)
							,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8));	
					cnt++;
				}
				
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

	public static void main(String [] argvs) {
		DBGetInfo db = new DBGetInfo();
		System.out.println(db.uInfo[0].id + db.uInfo[0].pw);
		
	}
}