package com.booking.scripts;

import java.io.File;

import java.util.HashMap;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.response.Response;
import utilities.CommonUtil;
import utilities.PropertiesRead;
import utilities.RestUtils;

public class Booking extends BaseClass {

	CommonUtil commonUtil = new CommonUtil();
	RestUtils restUtils = new RestUtils();
	public static String PROPERTIES_PATH = System.getProperty("user.dir") + File.separator + "properties/";
	static PropertiesRead prop = new PropertiesRead(PROPERTIES_PATH + File.separator + "Booking.properties");
	String jsonTestDataFilePath = System.getProperty("user.dir") + "\\testdata\\createBooking.json";
	String randomNumber = "";

	/**
	 * To verify data returned for existing booking matches
	 */
	 
	@Test
	public void verifyThatDataReturnedForExistingBookingMatches() {
	    log=extent.createTest("verifyThatDataReturnedForExistingBookingMatches");
	    System.out.println("TC1:- verify data returned for existing booking matches ");
		String bookingId = "";
		Response response = null;
		randomNumber = Integer.toString(commonUtil.generateRandomNumber());
		log.info("room Id :" + randomNumber);
		String newRoomJson = jsonTestDataWithNewRoomNum(randomNumber);

		/* Create booking */
		response = restUtils.postRequest(prop.getProperty("baseUrl"), newRoomJson);
		log.info("Booking details :" + response.asString());
		Assert.assertEquals(response.getStatusCode(), 201);

		/* get booking id */
		bookingId = response.jsonPath().get("bookingid").toString();
		/* get booking details by passing booking id */
		response = restUtils.getRequest(prop.getProperty("baseUrl") + bookingId);
		Assert.assertEquals(response.statusCode(), 200);
		log.info("Booking deatils by Booking ID :" + response.asString());

		/* To verify data returned from existing booking matches */
		HashMap<String, Object> actual = commonUtil.convertResponseToHashMap(response);
		HashMap<String, String> expected = commonUtil.expectedResult(bookingId, randomNumber);
		log.info("Verify data returned from existing booking matches");
		Assert.assertEquals(commonUtil.compareResults(actual, expected), true);
	}

	/**
	 * To verify That User Should Not Be Allowed To Create Bookings On Same Date
	 */

	@Test(priority = 2)
	public void verifyThatUserShouldNotBeAllowedToCreateBokingsOnSameDate() {
		System.out.println("TC2:-verify That User Should Not Be Allowed To Create Bookings On Same Date ");
	    log=extent.createTest("verifyThatUserShouldNotBeAllowedToCreateBokingsOnSameDate");
		Response response = null;
		String jsonTestData = jsonTestDataWithNewRoomNum(randomNumber);
		// Here first name is changed in the test data. i.e.., Michael to Amanda
		log.info("verify That User Should Not Be Allowed To Create Bookings On Same Date");
		response = restUtils.postRequest(prop.getProperty("baseUrl"), jsonTestData.replaceAll("Michael", "Amanda"));
		Assert.assertEquals(response.getStatusCode(), 409);

	}

	/**
	 * To Verify That User Should Not Be Allowed To Create Bookings If CheckOut Date
	 * Is Less Than CheckInDate With Time
	 */
	@Test(priority = 3)
	public void verifyThatUserShouldNotBeAllowedToCreateBokingsIfCheckOutDateIsLessThanCheckInDateWithTime() {
		System.out.println("TC3:-Verify That User Should Not Be Allowed To Create Bookings If CheckOut Date Is Less Than CheckInDate With Time");
	    log=extent.createTest("verifyThatUserShouldNotBeAllowedToCreateBokingsIfCheckOutDateIsLessThanCheckInDateWithTime");
		Response response = null;
		randomNumber = Integer.toString(commonUtil.generateRandomNumber());
		String jsonTestData = jsonTestDataWithNewRoomNum(randomNumber);
		log.info(
				"Verify That User Should Not Be Allowed To Create Bookings If CheckOut Date Is Less Than CheckInDate With Time");
		response = restUtils.postRequest(prop.getProperty("baseUrl"),
				jsonTestData.replaceAll("2020-02-17T15:05:18.016Z", "2020-01-17T05:05:18.016Z"));
		Assert.assertEquals(response.getStatusCode(), 409);
	}

	/**
	 * To Verify That User Should Not Be Allowed To Create Bookings If CheckOut Date
	 * Is Less Than CheckInDate
	 */
	@Test(priority = 4)
	public void verifyThatUserShouldNotBeAllowedToCreateBokingsIfCheckOutDateIsLessThanCheckInDate() {
		System.out.println("TC4:-To Verify That User Should Not Be Allowed To Create Bookings If CheckOut Date Is Less Than CheckInDate");
	    log=extent.createTest("verifyThatUserShouldNotBeAllowedToCreateBokingsIfCheckOutDateIsLessThanCheckInDate");
		Response response = null;
		randomNumber = Integer.toString(commonUtil.generateRandomNumber());
		String jsonTestData = jsonTestDataWithNewRoomNum(randomNumber);
		log.info("Verify That User Should Not Be Allowed To Create Bookings If CheckOut Date Is Less Than CheckInDate");
		response = restUtils.postRequest(prop.getProperty("baseUrl"),
				jsonTestData.replaceAll("2020-02-17T15:05:18.016Z", "2019-02-17"));
		Assert.assertEquals(response.getStatusCode(), 409);

	}

	/**
	 * To Verify That System Should Return Atleast Two Existing Bookings
	 */
	@Test(priority = 5)
	public void verifyThatSystemShouldReturnAtleastTwoExistingBookings() {
		System.out.println("TC5:-Verify That System Should Return Atleast Two Existing Bookings ");
	    log=extent.createTest("verifyThatSystemShouldReturnAtleastTwoExistingBookings");
		log.info("Verify That System Should Return Atleast Two Existing Bookings");
		String roomId = "1";
		boolean getresponse = getExistingbookingsByroomId(roomId);
		Assert.assertEquals(getresponse, true);
	}

	/**
	 * To Verify That System Should Not Return any Value For Invald RoomId
	 */
	@Test(priority = 6)
	public void verifyThatSystemShouldNotReturnanyValueForInvaldRoomId() {
		System.out.println("TC6:-Verify That System Should Not Return any Value For Invald RoomId ");
	    log=extent.createTest("verifyThatSystemShouldNotReturnanyValueForInvaldRoomId");
		log.info("Verify That System Should Not Return any Value For Invald RoomId");
		String roomId = "0000";
		getExistingbookingsByroomId(roomId);
		boolean getresponse = getExistingbookingsByroomId(roomId);
		Assert.assertEquals(getresponse, false);
	}
	
	/* To get Existing Booking id by room id */
	private boolean getExistingbookingsByroomId(String roomId) {
		boolean getresponse = true;
		int size = restUtils.getRequest(prop.getProperty("baseUrl") + prop.getProperty("getExistingBooking") + roomId)
				.path("bookings.size()");
		if (size < 2) {
			getresponse = false;
		}
		return getresponse;
	}

	/* To generate ramdom number for room id */
	private String jsonTestDataWithNewRoomNum(String randomNumber) {
		String jsonTestData = commonUtil.returnJsonAsString(jsonTestDataFilePath);
		String newRoomJson = jsonTestData.replaceAll("99", randomNumber);
		return newRoomJson;
	}
}
