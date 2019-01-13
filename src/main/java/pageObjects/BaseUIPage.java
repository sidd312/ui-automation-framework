package pageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.ExtentTest;

public class BaseUIPage {
	protected WebDriver driver;
	protected WebDriverWait wait;
	private static final int TIMEOUT = 5;
	private static final int POLLING = 100;
	protected List<String> tabs;
	public static ExtentTest test;

	public BaseUIPage(WebDriver inDriver, ExtentTest inTest) {
		driver = inDriver;
		wait = new WebDriverWait(driver, TIMEOUT, POLLING);
		test = inTest;
	}

	protected WebElement getWebElement(By locator) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		return driver.findElement(locator);
	}

	protected List<WebElement> getWebElements(By locator) {
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
		return driver.findElements(locator);
	}

	protected WebElement getWebElementFromElement(WebElement sourceElement, By locator) throws InterruptedException {
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		return sourceElement.findElement(locator);
	}

}