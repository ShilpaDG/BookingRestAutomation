package utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class RestUtils {

	/* post request 
	 * @input url, Json
	 * @Output response
	 * */
	public Response postRequest(String url, String jsonData) {

		try {
			Response response = RestAssured.given().contentType("application/json").body(jsonData).post(url);
			return response;

		} catch (Exception e) {

			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}

	}

	/* get request 
	 * @input url
	 * @Output response
	 * */
	public Response getRequest(String url) {

		try {

			Response response = RestAssured.given().contentType("application/json").get(url);
			return response;
		}

		catch (Exception e) {

			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}

	}
}
