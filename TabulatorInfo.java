package selenium.automation.assignment4;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TabulatorInfo {

	String filePath = "C://Users/Prasad/workspace/TabulatorAssignment4_Prasad/Properties/properties.txt";
	Properties property = null;
	InputStream inputStream = null;
	WebDriver driver = null;
	WebDriverWait wait = null;
	String homeUrl;
	String templateTableHeaderXpath = "//div[@id='tabulator-example']/div[1]/div/div[%d]/div[1]/div[1]";
	String templateCellDataXpath = "//div[@id='tabulator-example']/div[2]/div/div[%d]/div[%d]";

	public void executeTabulatorInfo() {
		WebElement nextBtn;

		initializer();
		invokeBrowser();
		scrollDown();
		readTableHeader();

		do {
			readTableData();
			nextBtn = driver
					.findElement(By
							.xpath("//div[@id='tabulator-example']/div[3]/span/button[3]"));
			nextBtn.click();
		} while (nextBtn.isEnabled());

		readTableData();
		driver.close();
	}

	public void initializer() {
		Properties property = new Properties();

		try {
			InputStream inputStream = new FileInputStream(new File(filePath));
			property.load(inputStream);

			String chromeDriverPath = property
					.getProperty("chrome.driver.path");
			String defaultWaitTimeString = property
					.getProperty("default.wait.time");
			Integer defaultWaitTime = new Integer(defaultWaitTimeString);

			System.setProperty("webdriver.chrome.driver", chromeDriverPath);
			driver = new ChromeDriver();
			wait = new WebDriverWait(driver, defaultWaitTime);

			homeUrl = property.getProperty("home.url");

		} catch (FileNotFoundException e) {
			System.out
					.println("FileNotFoundException occurred. Please check..");
		} catch (IOException e1) {
			System.out.println("IOException occured. Please check..");
		}
	}

	public void invokeBrowser() {
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.get(homeUrl);

	}

	public void scrollDown() {
		String nameXpath = String.format(templateTableHeaderXpath, 1);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(nameXpath)));

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,500)");

	}

	public void readTableHeader() {
		int columnCount = driver.findElements(
				By.xpath("//*[@id='tabulator-example']/div[1]/div/div")).size();
		for (int i = 1; i <= columnCount; i++) {
			if (i == 3) {
				continue;
			} else {
				String headerXpath = String.format(templateTableHeaderXpath, i);
				String tableHeader = driver.findElement(By.xpath(headerXpath))
						.getText();
				System.out.print(tableHeader + "\t");
			}
		}
		System.out.println();
	}

	public void readTableData() {
		int columnCount = driver.findElements(
				By.xpath("//*[@id='tabulator-example']/div[1]/div/div")).size();
		int rowCount = driver.findElements(
				By.xpath("//div[@id='tabulator-example']/div[2]/div/div"))
				.size();

		/*
		 * Read and print table data Here it will ignore the column number 3
		 * Also printing value of the title attribute for the column numbers 2,
		 * 5 and 8 Printing the text value for the rest of columns
		 */
		for (int i = 1; i <= rowCount; i++) {
			for (int j = 1; j <= columnCount; j++) {
				String cellDataXpath = String.format(templateCellDataXpath, i,
						j);
				WebElement cellData = driver.findElement(By
						.xpath(cellDataXpath));
				if (j == 3) {
					continue;
				} else if (j == 2 | j == 5 | j == 8) {
					System.out.print(cellData.getAttribute("title") + "\t");
				} else {
					System.out.print(cellData.getText() + "\t");

				}
			}
			System.out.println();
		}
	}
}
