package com.testcases;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.omg.CORBA.DynAnyPackage.Invalid;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BrokenLinkTest {

	
	private static WebDriver driver = null;
	private static int invalidUrlCount;
	private int brokenLinkCount;
	private int workingLinkCount;

	String homePage = "https://www.nordicnaturals.com";

	@Test

	public void FindBrokenLinksTest() {

		System.setProperty("webdriver.chrome.driver", ".\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

		driver.get(homePage);

		// Find total No of links on page and print In console.
		List<WebElement> allLinks = driver.findElements(By.tagName("a"));
		System.out.println("Total Number of initial Links are: " + allLinks.size());

		// loop through each link

		for (WebElement link : allLinks) {
			String url = link.getAttribute("href");

			if (url != null & isUrlValid(url)) {
				verifyLinks(url);
			}
		}

		System.out.println("Total number of broken links are: " + brokenLinkCount);
		System.out.println("Total number of working links are: " + workingLinkCount);
		System.out.println("Total number of invalid links are: " + invalidUrlCount);

		driver.quit();

	}

	public static boolean isUrlValid(String url) {

		try {
			URL urlObject = new URL(url);
			urlObject.toURI();
			return true;
		} catch (MalformedURLException e) {
			invalidUrlCount++;
			return false;
		} catch (URISyntaxException e) {
			return false;
		}
	}

	private void verifyLinks(String url) {

		try {
			URL linkUrl = new URL(url);
			HttpURLConnection htttpUrlConnection = (HttpURLConnection) linkUrl.openConnection();
			htttpUrlConnection.setRequestMethod("GET");
			htttpUrlConnection.connect();
			int response = htttpUrlConnection.getResponseCode();
			if (response != 200) {
				System.out.println(url + " - " + htttpUrlConnection.getResponseMessage() + " ---> broken link");
				brokenLinkCount++;
			}

			else {
				// System.out.println(url + " - " + htttpUrlConnection.getResponseMessage() );
				workingLinkCount++;
			}

		} catch (MalformedURLException e) {

			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();

		}
	}

}
