import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* ��Ƽ������ ä�ù� ����. Ŭ���̾�Ʈ�� ���� ��û�� ������ ��
* Ŭ���̾�Ʈ���� "SUPmitNAME" �ؽ�Ʈ�� �����Ͽ� ȭ�� �̸��� �����ϰ�
* ���� �̸��� ���ŵ� ������ �̸� ��û Ŭ���̾�Ʈ�� a�� ������ ��
* ���� �̸�, ������ "NAMEACLEED"�� ������. �׷��� ��� �޽���
* �ش� Ŭ���̾�Ʈ���� ������ ������ �ٸ� ��� Ŭ���̾�Ʈ�� ��ε�ĳ��Ʈ��
* ���� ȭ�� �̸�. ��� �޽��� �տ��� "MESSAGE"�� �ٴ´�.
*
*/
public class Server {

	//String �� PrintWriter�� �ؽø����� �����Ͽ�, Ŭ���̾�Ʈ�� �����ϰ� �ߺ��� ���ش�.
	private static HashMap<String, PrintWriter> hm = new HashMap<>(15);

	public static void main(String[] args) throws Exception {
		System.out.println("The chat server is running...");
		ExecutorService pool = Executors.newFixedThreadPool(500);//��Ƽ ������� Ŭ����
		try (ServerSocket listener = new ServerSocket(Configuration.PORT)) {//������Ĺ ����
			while (true) {
				pool.execute(new Handler(listener.accept()));
			}
		}
	}

	//Ŭ���̾�Ʈ �ڵ鷯
	private static class Handler implements Runnable {
		private String name;
		private Socket socket;
		private Scanner in;
		private PrintWriter out;

		//���Ϻ��� ������ ����
		public Handler(Socket socket) {
			this.socket = socket;
		}

		//���� ������ Ŭ���̾�Ʈ�� �̸��� ���� ����. �̸��� ��ġ�� �ʾƾ� ����
		public void run() {
			try {
				in = new Scanner(socket.getInputStream());
				out = new PrintWriter(socket.getOutputStream(), true);

				// ������ �̸� �Է¹ޱ������� ��� ȣ��
				while (true) {
					out.println("SUBMITNAME");
					name = in.nextLine();
					if (name == null) {
						return;
					}
					synchronized (hm) {
						if (name.length() > 0 && !hm.containsKey(name)) {
							hm.put(name, out);
							break;
						}
					}
				}

				//���� ���� Ŭ���̾�Ʈ �̸� �ٸ� ����ڵ鿡�� ��� �˸���
				out.println("NAMEACCEPTED " + name);
				for (PrintWriter writer : hm.values()) {
					writer.println("MESSAGE " + name + " has joined");
				}
				// client�κ��� ���� �޼��� ó��
				while (true) {
					String input = in.nextLine();
					if (input.toLowerCase().startsWith("/quit")) {// /quit ���� ���۽� Ŭ���̾�Ʈ ����
						return;
					}
					if (input.toLowerCase().startsWith("/whisper")) {// �ӼӸ� ����) /whisper kevin hello 

						String[] str = input.split(" ", 3);
						String nameWhisper = null;
						
						if(str.length == 3) {
							nameWhisper= str[1];
						}
						
						if (hm.containsKey(nameWhisper)) {//Ŭ���̾�Ʈ�� �������� ������� �ӼӸ� ���� ����
							for (String strName : hm.keySet()) {
								if (nameWhisper.equals(strName) || name.equals(strName))
									hm.get(strName).println("MESSAGE_WHISPER " + name + "->" + nameWhisper + "(Whisper)"
											+ ": " + str[2]);
							}
						} else {//���������� ���� ���޼��� ���
							for (String strName : hm.keySet()) 
								if (name.equals(strName))
									hm.get(strName).println("MESSAGE " + "WRONG RECIEVER!");
						}
					} else {//����� �޼��� ���
						for (PrintWriter writer : hm.values()) {
							writer.println("MESSAGE " + name + ": " + input);
						}
					} 
				}
			} catch (Exception e) {
				System.out.println(e);
			} finally {
				if (out != null) {

					PrintWriter valueToBeRemoved = out;//hashmap value �̿� matching ���� 
					hm.entrySet().removeIf(entry -> (valueToBeRemoved.equals(entry.getValue())));
				}
				if (name != null) {
					System.out.println(name + " is leaving");//����
					hm.remove(name);
					for (PrintWriter writer : hm.values()) {
						writer.println("MESSAGE " + name + " has left");//��ο��� ���� �޼��� ���
					}
				}
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
}