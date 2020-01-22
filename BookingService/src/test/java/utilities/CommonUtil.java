package utilities;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Random;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;

public class CommonUtil {

	Response response = null;
	private Logger log = LoggerFactory.getLogger(CommonUtil.class);

	/* to convert object to string */
	public String checkTheTypeOfInstanceAndReturnAsString(Object data) {

		String convertedString = "";
		if (data instanceof LinkedHashMap) {

			LinkedHashMap map = (LinkedHashMap) data;
			convertedString = map.toString();

		} else if (data instanceof Integer) {

			convertedString = String.valueOf(data);
		} else if (data instanceof String) {

			convertedString = String.valueOf(data);
		} else if (data instanceof Object) {

			convertedString = data.toString();
		}

		return convertedString;
	}

	/* to compare hash maps */
	public boolean compareResults(HashMap<String, Object> actual, HashMap<String, String> expected) {

		boolean isdataMatching = true;

		Iterator<String> itr = actual.keySet().iterator();

		while (itr.hasNext()) {
			String key = itr.next();

			if (!(expected.containsValue(checkTheTypeOfInstanceAndReturnAsString(actual.get(key))))) {
				isdataMatching = false;
				log.info("Mismatch");
				break;
			}
		}

		return isdataMatching;
	}

	/*
	 * This method will read the JSON file and return the content as string
	 */

	public String returnJsonAsString(String jsonPath) {
		InputStream fis;
		BufferedInputStream bis;
		DataInputStream dis;
		String jsonData = "";
		try {
			File initialFile = new File(jsonPath);
			fis = new FileInputStream(initialFile);

			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);
			while (dis.available() != 0) {
				jsonData = jsonData + dis.readLine().trim();
			}
			dis.close();
			bis.close();
			fis.close();
		} catch (IOException ioexception) {
			log.error("User is getting this exception" + ioexception.getMessage()
					+ " in reading json from this " + jsonPath);

		} catch (Exception e) {

		}

		return jsonData;
	}

	/** to generate random number */
	public int generateRandomNumber() {
		Random rand = new Random();
		int randNum = rand.nextInt(1000);
		return randNum;

	}

	/** to generate random number */
	public HashMap<String, Object> convertResponseToHashMap(Response response) {
		HashMap<String, Object> actual = null;
		JSONObject resultsFromGetResponse = null;
		resultsFromGetResponse = new JSONObject(response.asString());
		try {
			return actual = new ObjectMapper().readValue(resultsFromGetResponse.toString(), HashMap.class);
		} catch (Exception e) {
			log.error("Error while converting response to hash map"+e.getMessage());
			e.printStackTrace();
			return null;
		}

	}

	/** Expected result in hashmap */
	public HashMap<String, String> expectedResult(String bookingId, String roomId) {

		HashMap<String, String> expectedResults = new HashMap<String, String>();

		expectedResults.put("firstname", "Michael");
		expectedResults.put("depositpaid", "true");
		expectedResults.put("bookingid", bookingId);
		expectedResults.put("roomid", roomId);
		expectedResults.put("bookingdates", "{checkin=2020-01-17, checkout=2020-02-17}");
		expectedResults.put("lastname", "Hooks");
		return expectedResults;

	}
}
