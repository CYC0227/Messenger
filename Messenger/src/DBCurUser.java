import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class DBCurUser {
 
	String[] friends = null;
	int friendNumber;
	public DBCurUser(String curUser) {
		// Connection ��ü�� �ڵ��ϼ����� import�� ���� com.mysql.connection�� �ƴ�
		// java ǥ���� java.sql.Connection Ŭ������ import�ؾ� �Ѵ�.
		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;

		ResultSet rs = null;
		ResultSet rs2 = null;

		
		int friendNumber = 0;
		
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
			
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();
			
				sql = "SELECT * from friend where person =" + "\"" + curUser +"\"";

				// 5. ���� ����
				// ���ڵ���� ResultSet ��ü�� �߰��ȴ�.
				rs = stmt.executeQuery(sql);
				rs2 = stmt2.executeQuery(sql);
				
				while (rs2.next()) {
					friendNumber++;
				}
				
				this.friendNumber = friendNumber;

				friends = new String[friendNumber];
				int cnt = 0;
				while (rs.next()) {
					// ���ڵ��� Į���� �迭�� �޸� 0���� �������� �ʰ� 1���� �����Ѵ�.
					// �����ͺ��̽����� �������� �������� Ÿ�Կ� �°� getString �Ǵ� getInt ���� ȣ���Ѵ�.
					friends[cnt] = rs.getString(2);
					cnt++;
				}
				
		} catch (ClassNotFoundException e) {
			System.out.println("����̹� �ε� ����");
		} catch (SQLException e) {
			System.out.println("����2: " + e);
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
		DBCurUser db = new DBCurUser("a016232");
		System.out.println(db.friends[0]);
		System.out.println(db.friends[1]);

		
	}
}