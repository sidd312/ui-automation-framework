package pageObjects;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.LogStatus;

public class CouponPage extends BaseUIPage {

	public static final By couponPagePrice = By.cssSelector(
			"div.dont-break:nth-child(7) div:nth-child(1) div.prescription-info div.price-info:nth-child(4) div.price.clearfix > span.value.pull-left");
	public static final By couponPageStoreName = By.cssSelector(
			"div.dont-break:nth-child(7) div:nth-child(1) div.prescription-info div.price-info:nth-child(4) div.explanation > strong:nth-child(1)");
	public static final By priceAlertAccept = By.xpath("//*[contains(text(),'No thanks, please don’t show again')]");
	String drugName;
	public List<String> overCounterDrugs;
	public List<String> controlledDrugs;

	public CouponPage(WebDriver inDriver, String inDrugName, List<String> inOverCounter, List<String> inControlled) {
		super(inDriver, test);
		drugName = inDrugName;
		tabs = new ArrayList<String>(inDriver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));
		overCounterDrugs = inOverCounter;
		controlledDrugs = inControlled;
	}

	public String getDrugPrice() {
		test.log(LogStatus.INFO, String.format("Getting the drug price in the coupons page for %s drug", drugName));
		return controlledDrugs.contains(drugName.toString().replace("\\", ""))
				? "No price for controlled drugs on the coupon page"
				: "$".concat(getWebElement(couponPagePrice).getText());
	}

	public String getStoreName() {
		test.log(LogStatus.INFO,
				String.format("Getting the drug store name in the coupons page for %s drug", drugName));
		return controlledDrugs.contains(drugName.toString().replace("\\", ""))
				? "No store name for controlled drugs on the coupon page"
				: getWebElement(couponPageStoreName).getText();
	}

	public void goBackToSearchResultsPage(int index) {
		driver.close();
		test.log(LogStatus.INFO,
				String.format("Going back to the search page from the coupons page for %s drug", drugName));
		driver.switchTo().window(tabs.get(0));
		if (index == 0 && !overCounterDrugs.contains(drugName.toString().replace("\\", ""))) {
			getWebElement(priceAlertAccept).click();
		}
	}
}
