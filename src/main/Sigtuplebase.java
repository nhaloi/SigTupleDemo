package main;

import java.net.HttpURLConnection;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Sigtuplebase {
	public static WebDriver driver;
	public WebElement we;
	public String url;
	public String searchKeyword_propertext;
	public String searchKeyword_notproperlyformantedtext;
	public WebDriverWait wait;
	HttpURLConnection httpURLConnection = null;
	int respCode;


}