package uk.co.twoitesting.pomclasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import uk.co.twoitesting.pomtests.pomTests;

public class shopPagePOM extends pomTests {
    public shopPagePOM(WebDriver driver){ this.driver = driver; }

    public shopPagePOM addFirstVisibleProductToCart() {
        //Add to cart Locator
        driver.findElement(By.linkText("Add to cart")).click();
        return this;
    }

    public shopPagePOM addBeltToCartByButtonValue28() {
        //Add to cart locator - Belt
        driver.findElement(By.cssSelector("button[value='28']")).click();
        return this;
    }

    public cartPagePOM viewCartFromNotice() {
        //View cart locator
        driver.findElement(By.linkText("View cart")).click();
        return new cartPagePOM(driver);
    }
}