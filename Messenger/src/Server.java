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
* 멀티스레드 채팅방 서버. 클라이언트가 서버 요청을 연결할 때
* 클라이언트에게 "SUPmitNAME" 텍스트를 전송하여 화면 이름을 유지하고
* 고유 이름이 수신될 때까지 이름 요청 클라이언트가 a를 제출한 후
* 고유 이름, 서버가 "NAMEACLEED"로 승인함. 그러면 모든 메시지
* 해당 클라이언트에서 다음을 제출한 다른 모든 클라이언트로 브로드캐스트됨
* 고유 화면 이름. 방송 메시지 앞에는 "MESSAGE"가 붙는다.
*
*/
public class Server {

	//String 과 PrintWriter를 해시맵으로 저장하여, 클라이언트를 통제하고 중복을 없앤다.
	private static HashMap<String, PrintWriter> hm = new HashMap<>(15);

	public static void main(String[] args) throws Exception {
		System.out.println("The chat server is running...");
		ExecutorService pool = Executors.newFixedThreadPool(500);//멀티 스래드용 클래스
		try (ServerSocket listener = new ServerSocket(Configuration.PORT)) {//서버소캣 생성
			while (true) {
				pool.execute(new Handler(listener.accept()));
			}
		}
	}

	//클라이언트 핸들러
	private static class Handler implements Runnable {
		private String name;
		private Socket socket;
		private Scanner in;
		private PrintWriter out;

		//소켓별로 스래드 생성
		public Handler(Socket socket) {
			this.socket = socket;
		}

		//새로 접속할 클라이언트의 이름과 소켓 연결. 이름이 겹치지 않아야 종료
		public void run() {
			try {
				in = new Scanner(socket.getInputStream());
				out = new PrintWriter(socket.getOutputStream(), true);

				// 유일한 이름 입력받기전까지 계속 호출
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

				//새로 들어온 클라이언트 이름 다른 사용자들에게 모두 알리기
				out.println("NAMEACCEPTED " + name);
				for (PrintWriter writer : hm.values()) {
					writer.println("MESSAGE " + name + " has joined");
				}
				// client로부터 받은 메세지 처리
				while (true) {
					String input = in.nextLine();
					if (input.toLowerCase().startsWith("/quit")) {// /quit 으로 시작시 클라이언트 종료
						return;
					}
					if (input.toLowerCase().startsWith("/whisper")) {// 귓속말 예시) /whisper kevin hello 

						String[] str = input.split(" ", 3);
						String nameWhisper = null;
						
						if(str.length == 3) {
							nameWhisper= str[1];
						}
						
						if (hm.containsKey(nameWhisper)) {//클라이언트가 정상적인 방법으로 귓속말 사용시 실행
							for (String strName : hm.keySet()) {
								if (nameWhisper.equals(strName) || name.equals(strName))
									hm.get(strName).println("MESSAGE_WHISPER " + name + "->" + nameWhisper + "(Whisper)"
											+ ": " + str[2]);
							}
						} else {//비정상적인 사용시 경고메세지 출력
							for (String strName : hm.keySet()) 
								if (name.equals(strName))
									hm.get(strName).println("MESSAGE " + "WRONG RECIEVER!");
						}
					} else {//평범한 메세지 출력
						for (PrintWriter writer : hm.values()) {
							writer.println("MESSAGE " + name + ": " + input);
						}
					} 
				}
			} catch (Exception e) {
				System.out.println(e);
			} finally {
				if (out != null) {

					PrintWriter valueToBeRemoved = out;//hashmap value 이용 matching 삭제 
					hm.entrySet().removeIf(entry -> (valueToBeRemoved.equals(entry.getValue())));
				}
				if (name != null) {
					System.out.println(name + " is leaving");//퇴장
					hm.remove(name);
					for (PrintWriter writer : hm.values()) {
						writer.println("MESSAGE " + name + " has left");//모두에게 퇴장 메세지 출력
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