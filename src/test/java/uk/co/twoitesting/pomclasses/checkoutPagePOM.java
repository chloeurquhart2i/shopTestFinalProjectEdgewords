package uk.co.twoitesting.pomclasses;


import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import uk.co.twoitesting.pomtests.pomTests;

import java.time.Duration;

public class checkoutPagePOM extends pomTests {
    private final WebDriverWait wait;
    public checkoutPagePOM(WebDriver driver){ this.driver = driver; this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); }

    private void set(By locator, String value){
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        el.click();
        el.sendKeys(Keys.chord(Keys.CONTROL, "a")); el.sendKeys(Keys.DELETE);
        if (!el.getAttribute("value").isEmpty()) el.clear();
        el.sendKeys(value);
    }

    public checkoutPagePOM fillBilling(
            String first, String last, String company,
            String addr1, String city, String postcode,
            String phone, String email
    ){
        set(By.id("billing_first_name"), first);
        set(By.id("billing_last_name"), last);
        set(By.id("billing_company"), company);
        set(By.id("billing_address_1"), addr1);
        set(By.id("billing_city"), city);
        set(By.id("billing_postcode"), postcode);
        set(By.id("billing_phone"), phone);
        set(By.id("billing_email"), email);
        return this;
    }

    public checkoutPagePOM placeOrder() {
        driver.findElement(By.cssSelector("#place_order")).click();
        return this;
    }

    public String captureOrderNumber() {
        WebElement orderNumberEl = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#post-6 ul.woocommerce-order-overview li.woocommerce-order-overview__order.order > strong")
        ));
        return orderNumberEl.getText().trim(); // e.g. "#15717"
    }
}
