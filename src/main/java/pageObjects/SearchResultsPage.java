package pageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.relevantcodes.extentreports.LogStatus;

public class SearchResultsPage extends BaseUIPage {

	protected String drugName;
	public static final By alertAccept = By.xpath("//*[contains(text(),'OK, Got It')]");
	public static final By priceRowsLocator = By.cssSelector("div[data-qa='price_row']");
	public static final By buttons = By.cssSelector("#uat-expanded-row-action-button");
	public static final By pricing = By.cssSelector("div._23KRZMzKphkBE5Z4csw0iB");
	public static final By storeName = By.cssSelector("div._1SZkznNasg64z9SBxpKXnT");
	public static final By getFreeCoupon = By.xpath("//a[@class='s1x134y5-1 hIuaUP']");
	public static final By proceedToDiscount = By.cssSelector("#uat-attestation-accept-button");
	public static final By brandDropDownSelector = By.cssSelector("#uat-dropdown-brand");
	public static final By availableBrands = By.xpath("//div[@class='yy3ke2-2 bxnelb']//div");
	public static final By alreadySelectedItem = By
			.xpath("//i[@class='_29KgpnFGZjY1SoP6oqJKA0 _1Zy1O784pbKnd_6EWmzS8H yy3ke2-4 iTJGAA']");
	public static final By drugFormDropDownSelector = By.cssSelector("#uat-dropdown-container-form");
	public static final By availableDrugFormsDosage = By.cssSelector("div.yy3ke2-3.joKMUN");
	public static final By disabledDosage = By.xpath("//span[@class='s5rsu99-4 gLrUwY' and @type='dosage']");
	public static final By disabledDrugForm = By.xpath("//span[@class='s5rsu99-4 fdaySl']");
	public static final By drugDosageDropDownSelector = By.cssSelector("#uat-dropdown-container-dosage");
	public static final String searchByText = "//div[contains(text(),\'%s\')]";
	public List<String> overCounterDrugs;

	public SearchResultsPage(WebDriver inDriver, String inDrugName, List<String> inOverCounter) {
		super(inDriver, test);
		drugName = inDrugName;
		overCounterDrugs = inOverCounter;
	}

	public CouponPage goToCouponsPage(WebElement priceRow, List<String> controlledDrugs) throws InterruptedException {
		test.log(LogStatus.INFO, String.format("Checking the price for %s drug on the coupons page", drugName));
		getWebElementFromElement(priceRow, buttons).click();
		test.log(LogStatus.INFO, String.format(
				"Clicking on the get free coupon or get free discount link to go to the coupon page or the modal for %s drug",
				drugName));
		if (overCounterDrugs.contains(drugName.toString().replace("\\", ""))) {
			driver.switchTo().activeElement();
			try {
				getWebElement(getFreeCoupon).click();
				test.log(LogStatus.INFO, String
						.format("Clicking on the get free coupon link to go to the coupon page for %s drug", drugName));
			} catch (TimeoutException e) {
				getWebElement(proceedToDiscount).click();
				test.log(LogStatus.INFO, String.format(
						"Clicking on the proceed to discount link to go to the coupon page for %s drug", drugName));
			}
		}
		return new CouponPage(driver, drugName, overCounterDrugs, controlledDrugs);
	}

	public List<WebElement> getAllPriceRows() {
		List<WebElement> priceRows = getWebElements(priceRowsLocator);
		test.log(LogStatus.PASS, String.format("Got %d price rows for %s drug", priceRows.size(), drugName));
		return priceRows;
	}

	public String getButtonType(WebElement priceRow) throws InterruptedException {
		return getWebElementFromElement(priceRow, buttons).getText();
	}

	public String getStoreName(WebElement priceRow) throws InterruptedException {
		test.log(LogStatus.INFO, String.format("Getting the store name in the price rows page for %s drug", drugName));
		return getWebElementFromElement(priceRow, storeName).getText();
	}

	public String getDrugPrice(WebElement priceRow) throws InterruptedException {
		test.log(LogStatus.INFO, String.format("Getting the drug price in the price rows page for %s drug", drugName));
		return getWebElementFromElement(priceRow, pricing).getText();
	}

	public void acceptAlert() {
		try {
			getWebElement(alertAccept).click();
		} catch (NoSuchElementException ex) {
		} catch (TimeoutException e) {
		}
	}

	public void refreshPage() {
		driver.navigate().refresh();
	}

	public void scrollToTop() {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(document.body.scrollHeight, 0)");
	}

}
