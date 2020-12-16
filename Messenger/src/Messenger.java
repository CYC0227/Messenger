import java.io.IOException;

import org.json.simple.parser.ParseException;

public class Messenger {

	public static final int SCREEN_WIDTH = 500;
	public static final int SCREEN_HEIGHT = 650;//화면크기
	
	public static void main(String[] args) throws IOException, ParseException {
		
		new ClientGUI();

	}

}
