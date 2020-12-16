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


/**swing ��� Ŭ���̾�Ʈ.
 * 
 * ��������
 * SUBMITNAME
 * NAMEACCEPTED
 * MESSAGE			
 * MESSAGE_WHISPER
 * 
 * Ư�� �Է�: /quit							Ŭ���̾�Ʈ ����
 * 		   /whisper name message		�ӼӸ�
 * 
 */

public class ChatClient1 {

	String serverAddress;
	Scanner in;
	PrintWriter out;
	JFrame frame = new JFrame("Chatter");
	JTextField textField = new JTextField(50);
	JTextArea messageArea = new JTextArea(16, 50);

	//Ŭ���̾�Ʈ ȭ�� ����
	public ChatClient1(String serverAddress) {
		this.serverAddress = serverAddress;

		textField.setEditable(false);//�Է� ȭ��
		messageArea.setEditable(false);//��� ȭ��
		frame.getContentPane().add(textField, BorderLayout.SOUTH);//ȭ�� ���ʿ� �Է�â
		frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);//ȭ�� �߾ӿ� ���â
		frame.pack();

		//�ؽ�Ʈ ������ ���� �޼��� ��ٸ���
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println(textField.getText());
				textField.setText("");
			}
		});
	}

	private String getName() {//���� ȭ�� ������ ����� �̸� ���� �Է¹޴� ȭ��
		return JOptionPane.showInputDialog(frame, "Choose a screen name:", "Screen name selection",
				JOptionPane.PLAIN_MESSAGE);
	}

	private void run() throws IOException {//�����κ��� �޼��� �޾ƿ� �������� ���� ���
		try {
			Socket socket = new Socket(serverAddress, Configuration.PORT);
			in = new Scanner(socket.getInputStream());
			out = new PrintWriter(socket.getOutputStream(), true);

			while (in.hasNextLine()) {
				String line = in.nextLine();
				if (line.startsWith("SUBMITNAME")) {//���� ����� �̸� �Է¹ޱ�
					out.println(getName());
				} else if (line.startsWith("NAMEACCEPTED")) {//�Է¹��� �̸����� ȭ�� Ÿ��Ʋ ����
					this.frame.setTitle("Chatter - " + line.substring(13));
					textField.setEditable(true);
				} else if (line.startsWith("MESSAGE_WHISPER")) {//�޼��� ���:�ӼӸ��϶� 
						messageArea.append(line.substring(16) + "\n");
				} else if (line.startsWith("MESSAGE")) {//�޼��� ���
					messageArea.append(line.substring(8) + "\n");
				}
			}
		} finally {//���� ����
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