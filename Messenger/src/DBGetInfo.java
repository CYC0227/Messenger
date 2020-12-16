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
		// Connection ��ü�� �ڵ��ϼ����� import�� ���� com.mysql.connection�� �ƴ�
		// java ǥ���� java.sql.Connection Ŭ������ import�ؾ� �Ѵ�.
		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;

		ResultSet rs = null;
		ResultSet rs2 = null;

		
		int userNumber = 0;
		
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
			String sql2 = "";
			
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();

				sql = "SELECT * from userinfo";

				// 5. ���� ����
				// ���ڵ���� ResultSet ��ü�� �߰��ȴ�.
				rs = stmt.executeQuery(sql);
				rs2 = stmt2.executeQuery(sql);
				
				while (rs2.next()) {
					userNumber++;
				}
				
				this.userNumber = userNumber;
				
				uInfo = new User[userNumber];
				
				int cnt = 0;
				while (rs.next()) {
					// ���ڵ��� Į���� �迭�� �޸� 0���� �������� �ʰ� 1���� �����Ѵ�.
					// �����ͺ��̽����� �������� �������� Ÿ�Կ� �°� getString �Ǵ� getInt ���� ȣ���Ѵ�.
					uInfo[cnt] = new User(rs.getString(1), rs.getString(2),rs.getString(3),rs.getString(4)
							,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8));	
					cnt++;
				}
				
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

	public static void main(String [] argvs) {
		DBGetInfo db = new DBGetInfo();
		System.out.println(db.uInfo[0].id + db.uInfo[0].pw);
		
	}
}