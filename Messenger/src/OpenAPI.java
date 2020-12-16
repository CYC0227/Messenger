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
				.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /* 페이지번호 */
		urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="
				+ URLEncoder.encode("10", "UTF-8")); /* 한 페이지 결과 수 */
		urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "="
				+ URLEncoder.encode("JSON", "UTF-8")); /* 요청자료형식(XML/JSON)Default: XML */
		urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "="
				+ URLEncoder.encode("20201216", "UTF-8")); /* 15년 12월 1일 발표 */
		urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "="
				+ URLEncoder.encode("1400", "UTF-8")); /* 06시 발표(정시단위) */
		urlBuilder.append(
				"&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode("62", "UTF-8")); /* 예보지점의 X 좌표값 */
		urlBuilder.append(
				"&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode("125", "UTF-8")); /* 예보지점 Y 좌표 */
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

		// JSON데이터를 넣어 JSON Object 로 만들어 준다.
		JSONObject jsonObject = (JSONObject)jsonParser.parse(jsonInfo);

		System.out.println("@@강남구 개포2동 날씨@@");

		JSONObject response = (JSONObject) jsonObject.get("response");//parse from json
		JSONObject body = (JSONObject) response.get("body");
		JSONObject items = (JSONObject) body.get("items");

		JSONArray itemArray = (JSONArray) items.get("item");

		String str;
		for (int i = 0; i < itemArray.size(); i++) {
			JSONObject itemObject = (JSONObject) itemArray.get(i);
			if (i == 0) {
				System.out.println("날짜:");
				System.out.println(itemObject.get("baseDate"));

				System.out.println("기준시간:");
				System.out.println(itemObject.get("baseTime"));

				System.out.println("X,Y 좌표 :");
				System.out.println(itemObject.get("nx") + " " + itemObject.get("ny"));
			}

			str = itemObject.get("category").toString();//parse by category
			if (str.equals("PTY")) {
				System.out.println("강수형태");
				System.out.println(itemObject.get("obsrValue"));
				whether = itemObject.get("obsrValue").toString();
			} else if (str.equals("REH")) {
				System.out.println("습도");
				System.out.println(itemObject.get("obsrValue") + "%");

			} else if (str.equals("RN1")) {
				System.out.println("1시간 강수량");
				System.out.println(itemObject.get("obsrValue") +"mm");

			} else if (str.equals("T1H")) {
				System.out.println("기온");
				System.out.println(itemObject.get("obsrValue") + "도씨");

			} else if (str.equals("UUU")) {
				System.out.println("동서바람성분");
				System.out.println(itemObject.get("obsrValue"));

			} else if (str.equals("VEC")) {
				System.out.println("풍향");
				System.out.println(itemObject.get("obsrValue"));

			} else if (str.equals("VVV")) {
				System.out.println("남북바람성분");
				System.out.println(itemObject.get("obsrValue"));

			} else if (str.equals("WSD")) {
				System.out.println("풍속");
				System.out.println(itemObject.get("obsrValue") + "m/s");
			}					

		}
	}
}
