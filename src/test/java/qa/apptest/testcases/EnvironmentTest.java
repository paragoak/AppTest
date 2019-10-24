package qa.apptest.testcases;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import qa.apptest.testbase.TestBase;

public class EnvironmentTest extends TestBase {

	@Test
	public void functionalTest() {

		assertTrue(driver.getTitle().contains("DataSense"));

	}

}
