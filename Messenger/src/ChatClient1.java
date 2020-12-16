import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


/**swing 기반 클라이언트.
 * 
 * 프로토콜
 * SUBMITNAME
 * NAMEACCEPTED
 * MESSAGE			
 * MESSAGE_WHISPER
 * 
 * 특수 입력: /quit							클라이언트 퇴장
 * 		   /whisper name message		귓속말
 * 
 */

public class ChatClient1 {

	String serverAddress;
	Scanner in;
	PrintWriter out;
	JFrame frame = new JFrame("Chatter");
	JTextField textField = new JTextField(50);
	JTextArea messageArea = new JTextArea(16, 50);

	//클라이언트 화면 생성
	public ChatClient1(String serverAddress) {
		this.serverAddress = serverAddress;

		textField.setEditable(false);//입력 화면
		messageArea.setEditable(false);//출력 화면
		frame.getContentPane().add(textField, BorderLayout.SOUTH);//화면 남쪽에 입력창
		frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);//화면 중앙에 출력창
		frame.pack();

		//텍스트 전송후 다음 메세지 기다리기
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println(textField.getText());
				textField.setText("");
			}
		});
	}

	private String getName() {//최초 화면 생성시 사용자 이름 생성 입력받는 화면
		return JOptionPane.showInputDialog(frame, "Choose a screen name:", "Screen name selection",
				JOptionPane.PLAIN_MESSAGE);
	}

	private void run() throws IOException {//서버로부터 메세지 받아와 프로토콜 따라 출력
		try {
			Socket socket = new Socket(serverAddress, Configuration.PORT);
			in = new Scanner(socket.getInputStream());
			out = new PrintWriter(socket.getOutputStream(), true);

			while (in.hasNextLine()) {
				String line = in.nextLine();
				if (line.startsWith("SUBMITNAME")) {//최초 사용자 이름 입력받기
					out.println(getName());
				} else if (line.startsWith("NAMEACCEPTED")) {//입력받은 이름따라 화면 타이틀 생성
					this.frame.setTitle("Chatter - " + line.substring(13));
					textField.setEditable(true);
				} else if (line.startsWith("MESSAGE_WHISPER")) {//메세지 출력:귓속말일때 
						messageArea.append(line.substring(16) + "\n");
				} else if (line.startsWith("MESSAGE")) {//메세지 출력
					messageArea.append(line.substring(8) + "\n");
				}
			}
		} finally {//최종 종료
			frame.setVisible(false);
			frame.dispose();
		}
	}

	public static void main(String[] args) throws Exception {

		ChatClient1 client = new ChatClient1(Configuration.IP);
		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.frame.setVisible(true);
		client.run();
	}
}