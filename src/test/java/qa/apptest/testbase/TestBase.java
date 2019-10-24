package qa.apptest.testbase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.aeonbits.owner.ConfigFactory;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import io.github.bonigarcia.wdm.WebDriverManager;
import qa.apptest.testlisteners.WebEventListener;
import qa.apptest.utilities.EnvironmentConfig;

public class TestBase {
 
	static EnvironmentConfig configEnvironment;

	public static WebDriver driver;

	public static EventFiringWebDriver e_driver;
	public static WebEventListener eventListener;
	
	static Workbook book;
	static Sheet sheet;
	static JavascriptExecutor js;

	public static Logger log = Logger.getLogger(TestBase.class);
	

	@BeforeClass
	@Parameters({ "environment" })
	public WebDriver setup(String environemnt) {
		
		ConfigFactory.setProperty("env", environemnt);

		configEnvironment = ConfigFactory.create(EnvironmentConfig.class);
		
		if (configEnvironment.browser().equalsIgnoreCase("Chrome")) {
			// To Load Chrome driver Instance.
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			System.out.println("Chrome browser loaded");
			log.debug("Chrome browser loaded");
			
		} else if (configEnvironment.browser().equalsIgnoreCase("FireFox")) {
			// To Load FireFox driver Instance.
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			System.out.println("FireFox browser loaded");
			log.debug("FireFox browser loaded");
			
		} else if (configEnvironment.browser().equalsIgnoreCase("Safari")) {
			// To Load Safari driver Instance.
			driver = new SafariDriver();
			System.out.println("Safari browser loaded");
			log.debug("Safari browser loaded");
			
		}

		if (driver != null) {
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			driver.get(configEnvironment.appURL());
			log.debug("Getting Application URL: " + configEnvironment.appURL());
			e_driver = new EventFiringWebDriver(driver);
			eventListener = new WebEventListener();

			e_driver.register(eventListener);

			driver = e_driver;

			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);


		} else {
			
			if (driver != null) {
				
				driver.close();
				System.out.println("Browser closed");
				log.debug("Browser closed");
			}else {
				// Null browser Instance when close.
				driver = null;
				System.out.println("Browser set as NULL");
				log.debug("Browser set as NULL");
			}
			
		}
		return driver;

	}
	
	@AfterClass
	public void tearDown() {
		
		//childTest.log(Status.INFO, "User logged out.");		
		 driver.quit();
		log.debug("Browser quit successfully");
		// et.log(LogStatus.INFO, "Clicking on : " + locator);

	}
	
	public static Object[][] getTestData(String fileName, String sheetName) {

		FileInputStream file = null;

		try {

			String testDataPath = configEnvironment.testdataPath() + fileName + ".xlsx";

			System.out.println("########### EXCEL FILE PATH :" + testDataPath + " AND Excel Sheet name: " + sheetName
					+ "###########");
			log.debug("########### EXCEL FILE PATH :" + testDataPath + " AND Excel Sheet name: " + sheetName
					+ "###########");

			file = new FileInputStream(testDataPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			book = WorkbookFactory.create(file);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = book.getSheet(sheetName);
		Object[][] data = new Object[sheet.getLastRowNum()][sheet.getRow(0).getLastCellNum()];

		System.out.println(sheet.getLastRowNum() + "--------" + sheet.getRow(0).getLastCellNum());
		log.debug(sheet.getLastRowNum() + "--------" + sheet.getRow(0).getLastCellNum());

		for (int i = 0; i < sheet.getLastRowNum(); i++) {
			for (int k = 0; k < sheet.getRow(0).getLastCellNum(); k++) {
				data[i][k] = sheet.getRow(i + 1).getCell(k).toString();
				System.out.println("########### EXCEL DATA : " + data[i][k]);
				log.debug("########### EXCEL DATA : " + data[i][k]);
			}
		}
		return data;
	}
}
