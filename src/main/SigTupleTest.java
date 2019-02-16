package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import util.ResultTest;
import util.WriteTestData;

public class SigTupleTest extends Sigtuplebase {
	// Use the below logger
	final static Logger logger = Logger.getLogger(SigTupleTest.class);
	ResultTest resultTest;
	WriteTestData writeTestData;

	// User defined function to read the config.property file
	private Properties getParametersFromConfigFile() throws IOException {
		Properties prop = new Properties();
		String propFileName = "src/test/resources/config.properties";
		FileInputStream inputStream = new FileInputStream(propFileName);
		prop.load(inputStream);
		return prop;
	}

	@BeforeMethod
	public void init() throws IOException {
		logger.info("*****************This is init method**************************");

		ChromeOptions options = new ChromeOptions();
		options.addArguments("disable-infobars");
		System.setProperty("webdriver.chrome.driver", "src/main/selenium/chromedriver");
		driver = new ChromeDriver(options);
		driver.manage().window().maximize();

		// Read from Property File
		url = getParametersFromConfigFile().getProperty("url");
		searchKeyword_propertext = getParametersFromConfigFile().getProperty("searchKeyword1");
		searchKeyword_notproperlyformantedtext = getParametersFromConfigFile().getProperty("searchKeyword2");
		driver.get(url);
		wait = new WebDriverWait(driver, 10);

		logger.info("*****************Exit initialization**************************");
	}

	@AfterMethod
	public void closeBrowser() {
		driver.quit();
	}

	@Test
	void googleSearchForKeywordFromPropertyFileandCountResults() throws InterruptedException, IOException {

		logger.info(
				"*****************This is googleSearchForKeywordFromPropertyFileandCountResults method**************************");

		we = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("q")));
		we.sendKeys(searchKeyword_propertext);
		we.submit();
		WebElement weSearch = driver.findElement(By.id("resultStats"));
		String results = weSearch.getText();
		// Write results to an excel files
		resultTest = new ResultTest();
		resultTest.setTestcaseName("Search for :" + searchKeyword_propertext);
		resultTest.setTestcaseComment("Total Count:" + results);
		writeTestData = new WriteTestData();
		writeTestData.addResult(resultTest);
		writeTestData.addResultToFile();

		logger.info(
				"***************** Exit from  googleSearchForKeywordFromPropertyFileandCountResults method**************************");
	}

	@Test
	void googleSearchForKeywordAndCheckforLinksCount() throws InterruptedException {

		logger.info(
				"*****************This is googleSearchForKeywordAndCheckforLinksCount method**************************");
		we = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("q")));
		we.sendKeys(searchKeyword_propertext);
		we.submit();

		List<WebElement> searchLinks = driver.findElements(By.tagName("a"));
		int linksCount = searchLinks.size();
		// Write results to an excel files
		System.out.println("Total search Links " + linksCount);
		logger.info(
				"*****************Exit from  googleSearchForKeywordAndCheckforLinksCount method**************************");
	}

	@Test
	void googleSearchForKeywordAndCheckforLinks() throws InterruptedException {
		logger.info("*****************This is googleSearchForKeywordAndCheckforLinks method**************************");
		WebDriverWait wait = new WebDriverWait(driver, 10);
		WebElement we;
		we = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("q")));
		we.sendKeys(searchKeyword_propertext);
		we.submit();

		List<WebElement> searchLinks = driver.findElements(By.tagName("a"));

		Iterator<WebElement> iterator = searchLinks.iterator();

		while (iterator.hasNext()) {

			url = iterator.next().getAttribute("href");
			// Print the list of URLs
			System.out.println(url);

			if (url == null || url.isEmpty()) {
				System.out.println("URL is either not configured for anchor tag or it is empty");
				continue;
			}

			try {
				httpURLConnection = (HttpURLConnection) (new URL(url).openConnection());

				httpURLConnection.setRequestMethod("HEAD");

				httpURLConnection.connect();

				respCode = httpURLConnection.getResponseCode();

				if (respCode >= 200 && respCode <= 226) {
					System.out.println(url + " Got reposnse code of 2XX, Valid URLs");
				} else {
					System.out.println(url + " did not get a  response code of 2XX. Check the URLs ");
				}

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info(
				"*****************Exit from googleSearchForKeywordAndCheckforLinks method**************************");

	}

	@Test
	void googleSearchForKeywordNotProperlyFormated() throws InterruptedException {
		logger.info(
				"*****************This is googleSearchForKeywordNotProperlyFormated method**************************");

		String notproperlyformantedtextSearch = "Did you mean:";

		we = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("q")));
		we.sendKeys(searchKeyword_notproperlyformantedtext);
		we.submit();
		WebElement weSearch = driver.findElement(By.xpath("//span[@class='gL9Hy d2IKib']"));
		Assert.assertEquals(weSearch.getText(), notproperlyformantedtextSearch);
		weSearch = driver.findElement(By.xpath("//i[contains(text(),'Sigtuple')]"));
		weSearch.click();
		String getTitle = driver.getTitle();
		Assert.assertNotEquals(getTitle, getTitle.contains(searchKeyword_notproperlyformantedtext));

		logger.info("*****************Exit googleSearchForKeywordNotProperlyFormated method**************************");

	}


}
