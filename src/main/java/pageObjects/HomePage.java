package pageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class HomePage extends BaseUIPage {

	protected String environmentURL;

	public static final By searchBarLocator = By.xpath("//form[@class='form-group -large']//input[@name='drug-name']");
	public static final By searchResultsLocator = By.xpath("//ul[@class='typeahead dropdown-menu']/li");

	public HomePage(WebDriver inDriver, String inEnvironmentURL, ExtentTest test) {
		super(inDriver, test);
		environmentURL = inEnvironmentURL;
		driver.get(environmentURL);
		driver.manage().addCookie(new Cookie("grx_internal_user", "true"));
		driver.navigate().refresh();
		test.log(LogStatus.PASS, "Navigated to home page " + driver.getCurrentUrl() + " sucessfully.");
	}

	public SearchResultsPage searchDrugName(String drugName, List<String> overCounterDrugs) {
		WebElement searchBar = getWebElement(searchBarLocator);
		searchBar.sendKeys(drugName);
		test.log(LogStatus.INFO,
				String.format("Searching the drug name %s on the website %s", drugName, driver.getCurrentUrl()));
		List<WebElement> searchResults = getWebElements(searchResultsLocator);
		test.log(LogStatus.PASS, String.format("Search was successful for the drug named %s and resulted in %d entries",
				drugName, searchResults.size()));
		test.log(LogStatus.INFO,
				String.format("Clicking on the first entry %s in the search results", searchResults.get(0).getText()));
		searchResults.get(0).click();
		return new SearchResultsPage(driver, drugName, overCounterDrugs);
	}

}
