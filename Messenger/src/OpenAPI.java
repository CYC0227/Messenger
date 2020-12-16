import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class OpenAPI {
	static String whether;
	
	String getWhether(){
		return whether;
	}
	OpenAPI() throws IOException, org.json.simple.parser.ParseException {
		StringBuilder urlBuilder = new StringBuilder(
				"http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst"); /* URL */
		urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8")
				+ "=FrhPm73mX4vxjVqcBCHKQ4fyQFTM%2F77yjaijoNCm6HyJUvYmRcMH%2FS2ScXKIXMls1hSrpD6ejb4Um9lFuihbWw%3D%3D"); /*
																													 * Service
																													 * Key
																													 */
		urlBuilder
				.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /* ��������ȣ */
		urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="
				+ URLEncoder.encode("10", "UTF-8")); /* �� ������ ��� �� */
		urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "="
				+ URLEncoder.encode("JSON", "UTF-8")); /* ��û�ڷ�����(XML/JSON)Default: XML */
		urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "="
				+ URLEncoder.encode("20201216", "UTF-8")); /* 15�� 12�� 1�� ��ǥ */
		urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "="
				+ URLEncoder.encode("1400", "UTF-8")); /* 06�� ��ǥ(���ô���) */
		urlBuilder.append(
				"&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode("62", "UTF-8")); /* ���������� X ��ǥ�� */
		urlBuilder.append(
				"&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode("125", "UTF-8")); /* �������� Y ��ǥ */
		URL url = new URL(urlBuilder.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");
		System.out.println("Response code: " + conn.getResponseCode());
		BufferedReader rd;
		if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
		conn.disconnect();

		String jsonInfo = sb.toString();
		
		JSONParser jsonParser = new JSONParser();

		// JSON�����͸� �־� JSON Object �� ����� �ش�.
		JSONObject jsonObject = (JSONObject)jsonParser.parse(jsonInfo);

		System.out.println("@@������ ����2�� ����@@");

		JSONObject response = (JSONObject) jsonObject.get("response");//parse from json
		JSONObject body = (JSONObject) response.get("body");
		JSONObject items = (JSONObject) body.get("items");

		JSONArray itemArray = (JSONArray) items.get("item");

		String str;
		for (int i = 0; i < itemArray.size(); i++) {
			JSONObject itemObject = (JSONObject) itemArray.get(i);
			if (i == 0) {
				System.out.println("��¥:");
				System.out.println(itemObject.get("baseDate"));

				System.out.println("���ؽð�:");
				System.out.println(itemObject.get("baseTime"));

				System.out.println("X,Y ��ǥ :");
				System.out.println(itemObject.get("nx") + " " + itemObject.get("ny"));
			}

			str = itemObject.get("category").toString();//parse by category
			if (str.equals("PTY")) {
				System.out.println("��������");
				System.out.println(itemObject.get("obsrValue"));
				whether = itemObject.get("obsrValue").toString();
			} else if (str.equals("REH")) {
				System.out.println("����");
				System.out.println(itemObject.get("obsrValue") + "%");

			} else if (str.equals("RN1")) {
				System.out.println("1�ð� ������");
				System.out.println(itemObject.get("obsrValue") +"mm");

			} else if (str.equals("T1H")) {
				System.out.println("���");
				System.out.println(itemObject.get("obsrValue") + "����");

			} else if (str.equals("UUU")) {
				System.out.println("�����ٶ�����");
				System.out.println(itemObject.get("obsrValue"));

			} else if (str.equals("VEC")) {
				System.out.println("ǳ��");
				System.out.println(itemObject.get("obsrValue"));

			} else if (str.equals("VVV")) {
				System.out.println("���Ϲٶ�����");
				System.out.println(itemObject.get("obsrValue"));

			} else if (str.equals("WSD")) {
				System.out.println("ǳ��");
				System.out.println(itemObject.get("obsrValue") + "m/s");
			}					

		}
	}
}
