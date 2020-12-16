import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.ChangedCharSetException;

import org.json.simple.parser.ParseException;

@SuppressWarnings("serial")
class userInfo {//사용자정보
	String name;
	String nick;
	String birth;
	String tel;
	String homepage;
	String statusMsg;
}

public class ClientGUI extends JFrame {
	private Image screenImage;
	private Graphics screenGraphic;

	private ImageIcon exitButtonEnteredImage = new ImageIcon(
			Messenger.class.getResource("./images/exitButtonEntered.png"));
	private ImageIcon exitButtonBasicImage = new ImageIcon(Messenger.class.getResource("./images/exitButtonBasic.png"));
	private ImageIcon backButtonEnteredImage = new ImageIcon(
			Messenger.class.getResource("./images/backButtonEntered.png"));
	private ImageIcon backButtonBasicImage = new ImageIcon(Messenger.class.getResource("./images/backButtonBasic.png"));
	private ImageIcon on = new ImageIcon(Messenger.class.getResource("./images/on.png"));
	private ImageIcon off = new ImageIcon(Messenger.class.getResource("./images/off.png"));
	private ImageIcon myInfo = new ImageIcon(Messenger.class.getResource("./images/myInfo.png"));
	private ImageIcon addFriend = new ImageIcon(Messenger.class.getResource("./images/addFriend.jpg"));
	private ImageIcon sun = new ImageIcon(Messenger.class.getResource("./images/sun.png"));
	private ImageIcon snow = new ImageIcon(Messenger.class.getResource("./images/snow.png"));
	private ImageIcon rain = new ImageIcon(Messenger.class.getResource("./images/rain.png"));

	private JButton backButtonRG = new JButton(backButtonBasicImage);
	private JButton exitButton = new JButton(exitButtonBasicImage);
	private JButton loginButton = new JButton("Login");
	private JButton registerButton = new JButton("Register");
	private JButton registerButton2 = new JButton("Register");
	private JButton dupliIDButton = new JButton("Check");
	private JButton userInfoButton = new JButton();
	private JButton backButtonInfo = new JButton(backButtonBasicImage);
	private JButton myInfoButton = new JButton(myInfo);
	private JButton addFriendButton = new JButton(addFriend);
	private JButton changeMessageButton = new JButton();


	private JPanel IDPanel = new JPanel();
	private JPanel PWPanel = new JPanel();
	private JPanel RGPanelID = new JPanel();
	private JPanel RGPanelPW = new JPanel();
	private JPanel RGPanelPW2 = new JPanel();
	private JPanel RGPanelName = new JPanel();
	private JPanel RGPanelNick = new JPanel();
	private JPanel RGPanelBD = new JPanel();
	private JPanel RGPanelTel = new JPanel();
	private JPanel RGPanelHP = new JPanel();
	private JPanel RGPanelMsg = new JPanel();
	private JPanel curUserPanel = new JPanel();
	private JPanel mainPagePanel = new JPanel();
	private JPanel chatPanel = new JPanel();

	private JScrollPane scrollPane = new JScrollPane();

	private JLabel IDLabel = new JLabel();
	private JLabel PWLabel = new JLabel();

	private JLabel RGLabelID = new JLabel();
	private JLabel RGLabelPW = new JLabel();
	private JLabel RGLabelPW2 = new JLabel();
	private JLabel RGLabelName = new JLabel();
	private JLabel RGLabelNick = new JLabel();
	private JLabel RGLabelBD = new JLabel();
	private JLabel RGLabelTel = new JLabel();
	private JLabel RGLabelHP = new JLabel();
	private JLabel RGLabelMsg = new JLabel();
	private JLabel curUserLabelName = new JLabel();
	private JLabel curUserMessage = new JLabel();
	
	private JLabel sunLabel = new JLabel(sun);
	private JLabel snowLabel = new JLabel(snow);
	private JLabel rainLabel = new JLabel(rain);

	

	private JTextField IDTextField = new JTextField(50);
//	private JTextField PWTextField = new JTextField(50);
	private JTextField RGTextFieldID = new JTextField();
	private JTextField RGTextFieldPW = new JTextField();
	private JTextField RGTextFieldPW2 = new JTextField();
	private JTextField RGTextFieldName = new JTextField();
	private JTextField RGTextFieldNick = new JTextField();
	private JTextField RGTextFieldBD = new JTextField();
	private JTextField RGTextFieldTel = new JTextField();
	private JTextField RGTextFieldHP = new JTextField();
	private JTextField RGTextFieldMsg = new JTextField();
	
	private static JTextField chatTextField = new JTextField(50)	;
	private static JTextArea messageArea = new JTextArea(16,50)	;

	static String serverAddress;
	static Scanner in;
	static PrintWriter out;
	
	private JPasswordField PWTextField = new JPasswordField();

	private int mouseX, mouseY;

	private Image background = new ImageIcon(Messenger.class.getResource("./images/page1.png")).getImage();
	private JLabel menuBar = new JLabel(new ImageIcon(Messenger.class.getResource("./images/menubar.png")));

	private int numOfFriend = 4;
	private boolean isFreindOn = true;
	private int curUseridx;

	private DBGetInfo db;
	private User[] user;
	private static String curUser;

	DBCurUser dbcur = null;
	
	String curF;
	
	JPanel[] panel = null;
	JLabel[] onLabel = null;
	JButton[] userDB = null;
	JButton[] chat = null;
	JTextField[] text = null;
	ChatClient1 client ;
	

	JPanel buttonPanel = new JPanel();
	GridBagLayout layout = new GridBagLayout();
	JPanel mainPanel = new JPanel();
	GridBagConstraints gbc = new GridBagConstraints();
	Container contentPane = getContentPane();

	static Socket socket;
	
	JLabel whetherLabel = new JLabel();
	OpenAPI api= null;
	ClientGUI() throws IOException, ParseException {
		
		api = new OpenAPI();//공공데이터 호출
		
		serverAddress = Configuration.IP;//채팅용 아이피
		db = new DBGetInfo();
		user = db.getUserInfo();
		
		socket = new Socket(serverAddress, Configuration.PORT);//채팅용 소켓

		
		
		setUndecorated(true);
		setTitle("Messenger");
		setSize(Messenger.SCREEN_WIDTH, Messenger.SCREEN_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setBackground(new Color(255, 255, 255, 0));
		setLayout(null);

		exitButton.setBounds(465, 0, 30, 30);
		exitButton.setBorderPainted(false);
		exitButton.setContentAreaFilled(false);
		exitButton.setFocusPainted(false);
		exitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				exitButton.setIcon(exitButtonEnteredImage);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				exitButton.setIcon(exitButtonBasicImage);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				System.exit(0);
			}
		});
		add(exitButton);

		backButtonRG.setVisible(false);
		backButtonRG.setBounds(0, 0, 30, 30);
		backButtonRG.setBorderPainted(false);
		backButtonRG.setContentAreaFilled(false);
		backButtonRG.setFocusPainted(false);
		backButtonRG.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				backButtonRG.setIcon(backButtonEnteredImage);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				backButtonRG.setIcon(backButtonBasicImage);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				backLogin();
			}
		});
		add(backButtonRG);

		backButtonInfo.setVisible(false);
		backButtonInfo.setBounds(0, 0, 30, 30);
		backButtonInfo.setBorderPainted(false);
		backButtonInfo.setContentAreaFilled(false);
		backButtonInfo.setFocusPainted(false);
		backButtonInfo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				backButtonInfo.setIcon(backButtonEnteredImage);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				backButtonInfo.setIcon(backButtonBasicImage);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				changeMessageButton.setVisible(false);
				backMain();
			}
		});
		add(backButtonInfo);

		menuBar.setBounds(0, 0, 500, 30);
		menuBar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});
		menuBar.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				setLocation(x - mouseX, y - mouseY);
			}
		});
		add(menuBar);

		// idpanel
		IDPanel.setLayout(new BoxLayout(IDPanel, BoxLayout.X_AXIS));
		IDPanel.setBounds(125, 450, 250, 40);

		IDLabel.setText(" I D ");

		IDTextField.setEditable(true);
		IDTextField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		IDPanel.add(IDLabel);
		IDPanel.add(IDTextField);

		add(IDPanel);

		// pwpanel
		PWPanel.setLayout(new BoxLayout(PWPanel, BoxLayout.X_AXIS));
		PWPanel.setBounds(125, 500, 250, 40);

		PWLabel.setText("PW");
		PWTextField.setEditable(true);

		PWTextField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		PWPanel.add(PWLabel);
		PWPanel.add(PWTextField);

		add(PWPanel);

		loginButton.setVisible(true);
		loginButton.setBounds(275, 550, 100, 40);
		add(loginButton);

		registerButton.setVisible(true);
		registerButton.setBounds(125, 550, 100, 40);

		registerButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				enterRegister();
			}
		});
		add(registerButton);

		changeMessageButton.setText("Change Message");
		changeMessageButton.setVisible(false);
		changeMessageButton.setBounds(100, 450, 300, 40);
		changeMessageButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int idx = 0;;
				for(int k = 0 ; k < db.getUserNum();k++) {
					if(curUser.equals(db.uInfo[k].id)) {
						idx = k;
						break;
					}
				}
				user[idx].message = RGTextFieldMsg.getText();
				JOptionPane.showMessageDialog(null, "Message Changed!", "OK", JOptionPane.PLAIN_MESSAGE);

			}
		});
		add(changeMessageButton);
		
		
		RGLabelID.setText("       I D      ");
		RGLabelPW.setText("     P W     ");
		RGLabelPW2.setText("PW:agian");
		RGLabelName.setText("    Name    ");
		RGLabelNick.setText("Nickname");
		RGLabelBD.setText(" BirthDay ");
		RGLabelTel.setText("    Phone   ");
		RGLabelHP.setText("Homepage");

		RGLabelID.setSize(100, 40);

		RGTextFieldID.setEditable(true);
		RGTextFieldPW.setEditable(true);
		RGTextFieldPW2.setEditable(true);
		RGTextFieldName.setEditable(true);
		RGTextFieldNick.setEditable(true);
		RGTextFieldBD.setEditable(true);
		RGTextFieldTel.setEditable(true);
		RGTextFieldHP.setEditable(true);

		dupliIDButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				String id = RGTextFieldID.getText();
				String msg1 = "Usable ID";
				String msg2 = "OK!";

				for (int i = 0; i < db.getUserNum(); i++) {
					if (id.equals(db.uInfo[i].id)) {
						msg1 = "Please try to use another ID";
						msg2 = "Duplicate ID!";
						RGTextFieldID.setText("");
						break;
					}
				}
				JOptionPane.showMessageDialog(null, msg1, msg2, JOptionPane.PLAIN_MESSAGE);

			}
		});

		registerButton2.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (RGTextFieldID.getText().equals("") || RGTextFieldPW.getText().equals("")
						|| RGTextFieldPW2.getText().equals("") || RGTextFieldName.getText().equals("")
						|| RGTextFieldNick.getText().equals("") || RGTextFieldBD.getText().equals("")
						|| RGTextFieldTel.getText().equals(""))
				// RGTextFieldHP.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "Please fill in the blanks", "Insufficient values",
							JOptionPane.PLAIN_MESSAGE);
				} else if (RGTextFieldPW.getText().equals(RGTextFieldPW2.getText()) != true) {
					JOptionPane.showMessageDialog(null, "Please enter same password values", "Different PW",
							JOptionPane.PLAIN_MESSAGE);
				} else {
					DBSetInfo dbs = new DBSetInfo(RGTextFieldID.getText(), RGTextFieldPW.getText(),
							RGTextFieldName.getText(), RGTextFieldNick.getText(), RGTextFieldBD.getText(),
							RGTextFieldTel.getText(), RGTextFieldHP.getText());
					JOptionPane.showMessageDialog(null, "Successfully registered! Please login", "Registered",
							JOptionPane.PLAIN_MESSAGE);
					backLogin();
				}
			}
		});

		loginButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String id = IDTextField.getText();
				String pw = PWTextField.getText();
				PWGenerator pwg2 = new PWGenerator(pw);
				String ep = pwg2.getEncryptedPW();

				int cnt = 0;
				for (int i = 0; i < db.getUserNum(); i++) {
					if (user[i].id.equals(id) && user[i].pw.equals(ep)) {
						curUseridx = i;
						curUser = user[i].id;
						dbcur = new DBCurUser(curUser);
						login();
						break;
					} else
						cnt++;
				}
				if (cnt == db.getUserNum())
					JOptionPane.showMessageDialog(null, "Wrong ID or PW or not registered! Please login", "Error",
							JOptionPane.PLAIN_MESSAGE);
			}
		});
		RGPanelID.setLayout(new BoxLayout(RGPanelID, BoxLayout.X_AXIS));
		RGPanelID.setVisible(false);
		RGPanelID.setBounds(100, 40, 300, 40);

		RGPanelPW.setLayout(new BoxLayout(RGPanelPW, BoxLayout.X_AXIS));
		RGPanelPW.setVisible(false);
		RGPanelPW.setBounds(100, 90, 300, 40);

		RGPanelPW2.setLayout(new BoxLayout(RGPanelPW2, BoxLayout.X_AXIS));
		RGPanelPW2.setVisible(false);
		RGPanelPW2.setBounds(100, 140, 300, 40);

		RGPanelName.setLayout(new BoxLayout(RGPanelName, BoxLayout.X_AXIS));
		RGPanelName.setVisible(false);
		RGPanelName.setBounds(100, 190, 300, 40);

		RGPanelNick.setLayout(new BoxLayout(RGPanelNick, BoxLayout.X_AXIS));
		RGPanelNick.setVisible(false);
		RGPanelNick.setBounds(100, 240, 300, 40);

		RGPanelBD.setLayout(new BoxLayout(RGPanelBD, BoxLayout.X_AXIS));
		RGPanelBD.setVisible(false);
		RGPanelBD.setBounds(100, 290, 300, 40);

		RGPanelTel.setLayout(new BoxLayout(RGPanelTel, BoxLayout.X_AXIS));
		RGPanelTel.setVisible(false);
		RGPanelTel.setBounds(100, 340, 300, 40);

		RGPanelHP.setLayout(new BoxLayout(RGPanelHP, BoxLayout.X_AXIS));
		RGPanelHP.setVisible(false);
		RGPanelHP.setBounds(100, 390, 300, 40);

		RGPanelID.add(RGLabelID);
		RGPanelPW.add(RGLabelPW);
		RGPanelPW2.add(RGLabelPW2);
		RGPanelName.add(RGLabelName);
		RGPanelNick.add(RGLabelNick);
		RGPanelBD.add(RGLabelBD);
		RGPanelTel.add(RGLabelTel);
		RGPanelHP.add(RGLabelHP);

		RGPanelID.add(RGTextFieldID);
		RGPanelPW.add(RGTextFieldPW);
		RGPanelPW2.add(RGTextFieldPW2);
		RGPanelName.add(RGTextFieldName);
		RGPanelNick.add(RGTextFieldNick);
		RGPanelBD.add(RGTextFieldBD);
		RGPanelTel.add(RGTextFieldTel);
		RGPanelHP.add(RGTextFieldHP);

		add(RGPanelID);
		add(RGPanelPW);
		add(RGPanelPW2);
		add(RGPanelName);
		add(RGPanelNick);
		add(RGPanelBD);
		add(RGPanelTel);
		add(RGPanelHP);

		RGLabelMsg.setText("Satus Message");

		RGPanelMsg.setLayout(new BoxLayout(RGPanelMsg, BoxLayout.X_AXIS));
		RGPanelMsg.setVisible(false);
		RGPanelMsg.setBounds(100, 140, 300, 40);

		RGPanelMsg.add(RGLabelMsg);
		RGPanelMsg.add(RGTextFieldMsg);
		add(RGPanelMsg);

		dupliIDButton.setVisible(false);
		dupliIDButton.setBounds(410, 40, 80, 40);

		dupliIDButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// isdupli
			}
		});
		add(dupliIDButton);

		registerButton2.setVisible(false);
		registerButton2.setBounds(100, 450, 300, 40);

		add(registerButton2);
		
		

		myInfoButton.setBounds(0, 40, 30, 30);
		myInfoButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				myInfo();
			}
		});
		add(myInfoButton);
		
	
		addFriendButton.setBounds(30, 40, 30, 30);
		addFriendButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				addFriend();
			}
		});
		add(addFriendButton);
		
		addFriendButton.setVisible(false);
		myInfoButton.setVisible(false);
		
		
		chatPanel.setBounds(0, 30, 500, 650);
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
		
		chatTextField.setEditable(true);
		chatTextField.setPreferredSize(new Dimension(500, 50));
		chatTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println(chatTextField.getText());
				chatTextField.setText("");
			}
		});
		
		chatPanel.add(messageArea);
		chatPanel.add(chatTextField);
		chatPanel.setVisible(false);
		add(chatPanel);
	}

	public void paint(Graphics g) {
		screenImage = createImage(Messenger.SCREEN_WIDTH, Messenger.SCREEN_HEIGHT);
		screenGraphic = screenImage.getGraphics();
		screenDraw(screenGraphic);
		g.drawImage(screenImage, 0, 0, null);
	}

	public void screenDraw(Graphics g) {
		g.drawImage(background, 100, 100, null);

		paintComponents(g);
		this.repaint();
	}

	public void login() {
		// something happen if else
		// entermMain(lengthof(userlist)) = 4 users
		enterMain();
	}

	public void backMain() {
		backButtonRG.setVisible(false);
		
		RGPanelMsg.setVisible(false);
		RGPanelName.setVisible(false);
		RGPanelNick.setVisible(false);
		RGPanelBD.setVisible(false);
		RGPanelTel.setVisible(false);
		RGPanelHP.setVisible(false);
		RGPanelMsg.setVisible(false);
		
		for(int i = 0; i < dbcur.friendNumber; i++)
			panel[i].setVisible(true);
	}
	public void enterMain() {
		chatPanel.setVisible(false);

		backButtonInfo.setVisible(false);
		backButtonRG.setVisible(false);

		changeMessageButton.setVisible(false);
		addFriendButton.setVisible(true);
		changeMessageButton.setVisible(true);
		RGPanelMsg.setVisible(false);
		RGPanelName.setVisible(false);
		RGPanelNick.setVisible(false);
		RGPanelBD.setVisible(false);
		RGPanelTel.setVisible(false);
		RGPanelHP.setVisible(false);
		RGPanelMsg.setVisible(false);

		registerButton2.setVisible(false);
		backButtonInfo.setVisible(false);

		background = null;
		IDPanel.setVisible(false);
		PWPanel.setVisible(false);
		loginButton.setVisible(false);
		registerButton.setVisible(false);

		IDPanel.setVisible(false);
		PWPanel.setVisible(false);
		registerButton.setVisible(false);

		contentPane = getContentPane();

		if(api.getWhether().equals("0")) {
			buttonPanel.add(sunLabel);
		}
		else if(api.getWhether().equals("1") ||
				api.getWhether().equals("2") ||
				api.getWhether().equals("4") ||
				api.getWhether().equals("5") ||
				api.getWhether().equals("6") ) {
			buttonPanel.add(rainLabel);
		}
		else {
			buttonPanel.add(snowLabel);

		}
		mainPanel.setLayout(layout);
		contentPane.setLayout(new BorderLayout());

	
		panel = new JPanel[dbcur.friendNumber];
		onLabel = new JLabel[dbcur.friendNumber];
		userDB = new JButton[dbcur.friendNumber];
		chat = new JButton[dbcur.friendNumber];
		text = new JTextField[dbcur.friendNumber];
		
		for (int i = 0; i < dbcur.friendNumber; i++) {

					panel[i] = new JPanel();
			
					panel[i].setSize(new Dimension(550, 50));

					
					curF = dbcur.friends[i];
					int idx = 0;;
					for(int k = 0 ; k < db.getUserNum();k++) {
						if(curF.equals(db.uInfo[k].id)) {
							idx = k;
							break;
						}
					}
					
				
						onLabel[i] = new JLabel(on);
					

					userDB[i] = new JButton(curF);
					userDB[i].setName(curF);
					chat[i] = new JButton("chat");
					
					chat[i].addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent e) {
							chat(e.getComponent().getName());
						}
					});
					// JLabel label = new JLabel();
					text[i] = new JTextField(15);

					userDB[i].setBorderPainted(false);
					userDB[i].setContentAreaFilled(false);
					userDB[i].setFocusPainted(false);

					userDB[i].addMouseListener(new MouseAdapter() {
						@Override
						public void mouseEntered(MouseEvent e) {
							
						}

						@Override
						public void mouseExited(MouseEvent e) {
						}

						@Override
						public void mousePressed(MouseEvent e) {
							userInfo(e.getComponent().getName()); 
						}
					});
					
					
					
					text[i].setText(user[idx].message);
					
					panel[i].add(onLabel[i]);
					panel[i].add(userDB[i]);
					panel[i].add(text[i]);
					panel[i].add(chat[i]);
				
					gbc.gridx = 0;
					gbc.gridy = i;

					mainPanel.add(panel[i], gbc);
					
				
		}

		addFriendButton.setVisible(true);
		myInfoButton.setVisible(true);
		
		contentPane.add(menuBar, BorderLayout.NORTH);
		contentPane.add(new JScrollPane(mainPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		
		
		changeMessageButton.setVisible(false);
		addFriendButton.setVisible(true);
		myInfoButton.setVisible(true);
		
		setSize(500, 650);
		setVisible(true);

	}
	public void chat(String id) {
		for(int i = 0; i < dbcur.friendNumber; i++)
			panel[i].setVisible(false);
		
	
		
		addFriendButton.setVisible(false);
		myInfoButton.setVisible(false);

		chatTextField.setVisible(true);
		messageArea.setVisible(true);
		chatPanel.setVisible(true);
		backButtonInfo.setVisible(true);

		ExecutorService pool = Executors.newFixedThreadPool(500);
		pool.execute(new Handler());
		
	
		
	}
	private static class Handler implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				in = new Scanner(socket.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				out = new PrintWriter(socket.getOutputStream(), true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			while (in.hasNextLine()) {
				String line = in.nextLine();
				if (line.startsWith("SUBMITNAME")) {//최초 사용자 이름 입력받기
					out.println(getName2());
				} else if (line.startsWith("NAMEACCEPTED")) {//입력받은 이름따라 화면 타이틀 생성
					chatTextField.setEditable(true);
				} else if (line.startsWith("MESSAGE_WHISPER")) {//메세지 출력:귓속말일때 
						messageArea.append(line.substring(16) + "\n");
				} else if (line.startsWith("MESSAGE")) {//메세지 출력
					messageArea.append(line.substring(8) + "\n");
				}
			}
		}
		
	}
	private static String getName2() {//최초 화면 생성시 사용자 이름 생성 입력받는 화면
		return curUser;
	}
	public void addFriend() {
		
	}
	public void myInfo() {
		int idx = 0;;
		for(int k = 0 ; k < db.getUserNum();k++) {
			if(curUser.equals(db.uInfo[k].id)) {
				idx = k;
				break;
			}
		}
		
		for(int i = 0; i < dbcur.friendNumber; i++)
			panel[i].setVisible(false);
		
		backButtonInfo.setVisible(true);
		changeMessageButton.setVisible(true);
		RGPanelMsg.setVisible(true);
		RGPanelName.setVisible(true);
		RGPanelNick.setVisible(true);
		RGPanelBD.setVisible(true);
		RGPanelTel.setVisible(true);
		RGPanelHP.setVisible(true);
		RGPanelMsg.setVisible(true);
		
		RGTextFieldMsg.setText(db.uInfo[idx].message);
		RGTextFieldName.setText(db.uInfo[idx].name);
		RGTextFieldNick.setText(db.uInfo[idx].nickname);
		RGTextFieldBD.setText(db.uInfo[idx].birthday);
		RGTextFieldTel.setText(db.uInfo[idx].telephone);
		RGTextFieldHP.setText(db.uInfo[idx].homepage);
		RGTextFieldMsg.setText(db.uInfo[idx].message);
	
		
	}
	public void userInfo(String id) {
	//	curF;
		int idx = 0;;
		for(int k = 0 ; k < db.getUserNum();k++) {
			if(id.equals(db.uInfo[k].id)) {
				idx = k;
				break;
			}
		}
		addFriendButton.setVisible(false);
		changeMessageButton.setVisible(false);
		backButtonInfo.setVisible(true);
		
		RGPanelMsg.setVisible(true);
		RGPanelName.setVisible(true);
		RGPanelNick.setVisible(true);
		RGPanelBD.setVisible(true);
		RGPanelTel.setVisible(true);
		RGPanelHP.setVisible(true);
		RGPanelMsg.setVisible(true);
		
		RGTextFieldMsg.setText(db.uInfo[idx].message);
		RGTextFieldName.setText(db.uInfo[idx].name);
		RGTextFieldNick.setText(db.uInfo[idx].nickname);
		RGTextFieldBD.setText(db.uInfo[idx].birthday);
		RGTextFieldTel.setText(db.uInfo[idx].telephone);
		RGTextFieldHP.setText(db.uInfo[idx].homepage);
		RGTextFieldMsg.setText(db.uInfo[idx].message);
		
		for(int i = 0; i < dbcur.friendNumber; i++)
			panel[i].setVisible(false);
	}

	public void setUserInfo() {
		userInfoButton.setVisible(false);
		curUserMessage.setVisible(false);
		curUserPanel.setVisible(false);
		curUserLabelName.setVisible(false);

		RGPanelMsg.setVisible(true);
		RGPanelName.setVisible(true);
		RGPanelNick.setVisible(true);
		RGPanelBD.setVisible(true);
		RGPanelTel.setVisible(true);
		RGPanelHP.setVisible(true);

		int op = 2; // 나중에 유저 정보 업데이트 시에 아이디 패스워드 등 변경 안하는 옵션
		registerButton2.setText("OK");
		registerButton2.setVisible(true);

		backButtonInfo.setVisible(true);
	}

	public void enterRegister() {
		int op = 1; // 나중에 유저 정보 업데이트 시에 아이디 패스워드 등 변경 안하는 옵션

		background = null;
		IDPanel.setVisible(false);
		PWPanel.setVisible(false);
		loginButton.setVisible(false);
		registerButton.setVisible(false);

		IDPanel.setVisible(false);
		PWPanel.setVisible(false);
		registerButton.setVisible(false);

		RGPanelID.setVisible(true);
		RGPanelPW.setVisible(true);
		RGPanelPW2.setVisible(true);
		RGPanelName.setVisible(true);
		RGPanelNick.setVisible(true);
		RGPanelBD.setVisible(true);
		RGPanelTel.setVisible(true);
		RGPanelHP.setVisible(true);

		dupliIDButton.setVisible(true);
		registerButton2.setVisible(true);

		backButtonRG.setVisible(true);
	}

	public void backLogin() {
		background = new ImageIcon(Messenger.class.getResource("./images/page1.png")).getImage();

		IDPanel.setVisible(true);
		PWPanel.setVisible(true);
		loginButton.setVisible(true);
		registerButton.setVisible(true);

		IDPanel.setVisible(true);
		PWPanel.setVisible(true);
		registerButton.setVisible(true);

		RGPanelID.setVisible(false);
		RGPanelPW.setVisible(false);
		RGPanelPW2.setVisible(false);
		RGPanelName.setVisible(false);
		RGPanelNick.setVisible(false);
		RGPanelBD.setVisible(false);
		RGPanelTel.setVisible(false);
		RGPanelHP.setVisible(false);

		dupliIDButton.setVisible(false);
		registerButton2.setVisible(false);

		backButtonRG.setVisible(false);
	}

}

