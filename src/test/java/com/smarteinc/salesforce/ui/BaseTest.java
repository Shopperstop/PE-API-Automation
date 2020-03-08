package com.smarteinc.salesforce.ui;

import java.io.File;
import java.io.InputStream;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeTest;

import com.sun.jna.platform.FileUtils;


public class BaseTest {

	private WebDriver driver;
	private DesiredCapabilities desiredCapabilities;
	
	private static String currentDriver = "";
	private static String driverFile = "";
	
	@BeforeTest(alwaysRun = true)
	public void initializemanager()
	{
		setDriver("chrome");
		driver.get("http://www.google.com");
	}

	
	public WebDriver getDriver() {
		if (driver instanceof ChromeDriver) {
			return (ChromeDriver) driver;
		} else if (driver instanceof InternetExplorerDriver) {
			return (InternetExplorerDriver) driver;
		} else if (driver instanceof FirefoxDriver) {
			return (FirefoxDriver) driver;
		} 
			return driver;
	}
	
//	private static String getTempDriver(InputStream driverStream, String driverName) {
//		if (currentDriver == null || !currentDriver.equalsIgnoreCase(driverName)) {
//			try {
//				File tempDriverFile = File.createTempFile(driverName, "");
//				tempDriverFile.deleteOnExit();
//				FileUtils.copyInputStreamToFile(driverStream, tempDriverFile);
//				tempDriverFile.setExecutable(true);
//				tempDriverFile.setReadable(true);
//				driverFile = tempDriverFile.getPath();
//				currentDriver = driverName;
//				return driverFile;
//			} catch (Exception e) {
//				System.out.println(e.getMessage());
//				return "";
//			}
//		} else {
//			return driverFile;
//		}
//   }
	
	
	
	private final static String getChromeDriverLocation() {
		String osName = System.getProperty("os.name").toLowerCase();
		String driverResource = "";
		if (osName.startsWith("windows")) {
			driverResource = "src\\test\\resources\\driver\\chromedriver.exe";
		} 
		else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
			driverResource = "chromedriver";
		}
//		InputStream driverStream = WebConfigHandler.class.getResourceAsStream(driverResource);
//		return getTempDriver(driverStream, "CHROME");
		return driverResource;
		// return "";
	}
	
	protected void setDesiredCapabilites(String driverType) {
		switch (driverType.toLowerCase()) {
		case "chrome":
			desiredCapabilities = DesiredCapabilities.chrome();
			break;
		case "ie":
			this.desiredCapabilities = DesiredCapabilities.internetExplorer();
			break;
		case "firefox":
			this.desiredCapabilities = DesiredCapabilities.firefox();
			break;
		
			
		/*
		 * case "cloud": DesiredCapabilities cloudDesiredCapabilities = new
		 * DesiredCapabilities();
		 * 
		 * this.desiredCapabilities = cloudDesiredCapabilities; break;
		 */

		default:
			desiredCapabilities = DesiredCapabilities.chrome();
			break;
		}
	}
	
	public void setDriver(String driverType) {
		setDesiredCapabilites(driverType);
		//addDownloadCapability(driverType,getDesiredCapabilities());
		switch (driverType.toLowerCase()) {
		//case "chrome":
		case "chrome":
			String chromeDriverPath = "";
			try {
				chromeDriverPath = getChromeDriverLocation();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			System.setProperty("webdriver.chrome.driver", chromeDriverPath);
			driver = new ChromeDriver(getDesiredCapabilities());
			//driver = new ChromeDriver();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;

		case "firefox":
			String firefoxDriverPath = "";
			try {
				//firefoxDriverPath = getFirefoxDriverLocation();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			System.setProperty("webdriver.gecko.driver", firefoxDriverPath);
			driver = new FirefoxDriver(getDesiredCapabilities());
			break;

		case "ie":
			String ieDriverPath = "";
			try {
				//ieDriverPath = getIEDriverLocation();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			System.setProperty("webdriver.ie.driver", ieDriverPath);
			driver = new InternetExplorerDriver(getDesiredCapabilities());
			break;
	
		/*
		 * case "cloud": try{ driver = new RemoteWebDriver(new
		 * URL("http://"+"rashmiagrawal1"+":"+"rGs6EgvHXQF72svfYqyb"+"@"+
		 * "hub-cloud.browserstack.com"+"/wd/hub"), getDesiredCapabilities()); }
		 * catch (Exception e) { System.out.println(e.getMessage()); } break;
		 */
			
//		case "headlesschrome":
//			driver = SeleniumUtility.getHeadlessDriver();
//			break;
//	
			
			
		default:
			String defaultDriverPath = "";
			try {
				defaultDriverPath = getChromeDriverLocation();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			System.setProperty("webdriver.chrome.driver", defaultDriverPath);
			driver = new ChromeDriver(getDesiredCapabilities());
			break;
		}
	}
	
	public DesiredCapabilities getDesiredCapabilities() {
		return this.desiredCapabilities;
	}
	
	
	
}
