package uk.co.twoitesting.pomclasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import uk.co.twoitesting.pomtests.pomTests;

public class homePagePOM extends pomTests {
    public homePagePOM(WebDriver driver){ this.driver = driver; }

    public shopPagePOM goToShop() {
        //Locator for shop button
        driver.findElement(By.linkText("Shop")).click();
        return new shopPagePOM(driver);
    }

    public shopPagePOM search(String query) {
        //Locator to allow for entering an item into a function and searching for it, e.g. belt
        driver.findElement(By.cssSelector("#woocommerce-product-search-field-0")).sendKeys(query + org.openqa.selenium.Keys.ENTER);
        return new shopPagePOM(driver);
    }
}
