package com.booking.scripts;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;


public class BaseClass {
	public ExtentReports extent;
	public ExtentTest log;
	
	@BeforeSuite
	public void setup()
	{
	    ExtentHtmlReporter reporter=new ExtentHtmlReporter("./Reports/Booking.html");
	    extent = new ExtentReports();
	    extent.attachReporter(reporter);
	  }

	@AfterSuite
	public void tearDown()
	{
		extent.flush();
	}
}
