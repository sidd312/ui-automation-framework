package test_user_flow;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import pageObjects.CouponPage;
import pageObjects.HomePage;
import pageObjects.SearchResultsPage;

public class Test_GoodRx_UI_Prices {
	protected static final String CHROMEDRIVERPATH = "src/test/resources/driverExecutables/chromedriverMac";
	WebDriver driver;

	public static final String DISCOUNTBUTTON = "GET FREE DISCOUNT";
	public static final String COUPONBUTTON = "GET FREE COUPON";
	public static final List<String> overCounterDrugs = new ArrayList<String>(Arrays.asList("Advil", "True Metrix"));
	public static final List<String> controlledDrugs = new ArrayList<String>(Arrays.asList("Vicodin"));
	public static ExtentReports extent;
	public static ExtentTest test;
	public static final Boolean replaceExistingReport = true;

	@BeforeSuite(alwaysRun = true)
	public void beforeSuite() {
		extent = new ExtentReports("src/test/resources/Report.html", replaceExistingReport);
		extent.loadConfig(new File("src/test/resources/extent-config.xml"));
	}

	@BeforeTest(alwaysRun = true, groups = { "regression" })
	public void initializeDriver(ITestContext itc) {
		System.setProperty("webdriver.chrome.driver", CHROMEDRIVERPATH);
		driver = new ChromeDriver();
		test = extent.startTest((this.getClass().getSimpleName() + " :: " + itc.getName()), itc.getName());
		test.assignCategory("Verify user flow on GoodRx production site");
		test.log(LogStatus.PASS, "Chrome browser launched successfully");
	}

	/**
	 * Verify the user flow of the GoodRx website
	 * 
	 * @param environmentURL
	 * @param drugName
	 * @throws InterruptedException
	 */
	@Parameters({ "environmentURL", "drugName" })
	@Test(groups = { "regression" })
	public void verifyUserFlow(String environmentURL, String drugName) throws InterruptedException {
		HomePage homePage = new HomePage(driver, environmentURL, test);
		SearchResultsPage searchPage = homePage.searchDrugName(drugName, overCounterDrugs);
		searchPage.acceptAlert();

		List<WebElement> priceRows = searchPage.getAllPriceRows();
		test.log(LogStatus.PASS, String.format("Going through %d price rows", priceRows.size()));
		for (int index = 0; index < priceRows.size(); index++) {
			WebElement priceRow = priceRows.get(index);
			if (searchPage.getButtonType(priceRow).equals(DISCOUNTBUTTON)
					|| searchPage.getButtonType(priceRow).equals(COUPONBUTTON)) {
				String searchPagePrice = searchPage.getDrugPrice(priceRow);
				String searchPageStoreName = searchPage.getStoreName(priceRow);
				test.log(LogStatus.PASS,
						String.format("Drug price for %s drug in the %s store name in the price rows page is %s",
								drugName, searchPageStoreName, searchPagePrice));
				CouponPage couponPage = searchPage.goToCouponsPage(priceRow, controlledDrugs);
				String couponPagePrice = couponPage.getDrugPrice();
				String couponPageStoreName = couponPage.getStoreName();
				test.log(LogStatus.PASS,
						String.format("Drug price for %s drug in the %s store name in the coupons page is %s", drugName,
								couponPageStoreName, couponPagePrice));
				couponPage.goBackToSearchResultsPage(index);
				if (!controlledDrugs.contains(drugName.toString().replace("\\", ""))) {
					Assert.assertEquals(couponPagePrice, searchPagePrice, "Coupon price does not match with price row");
					test.log(LogStatus.PASS,
							String.format("Coupon page price %s matches with the price rows page price for %s drug",
									couponPagePrice, searchPagePrice, drugName));
					Assert.assertEquals(couponPageStoreName, searchPageStoreName,
							"Coupon store name does not match with price row store name");
					test.log(LogStatus.PASS,
							String.format(
									"Coupon page store name %s matches with the price rows page store name for %s drug",
									couponPageStoreName, searchPageStoreName, drugName));
				}
			}
		}
	}

	@AfterTest(groups = { "regression" })
	public void cleanUp() {
		extent.endTest(test);
		extent.flush();
		test.log(LogStatus.PASS, "Chrome browser closed successfully.");
		extent.close();
		driver.close();
		driver.quit();
	}
}
