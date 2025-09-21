package uk.co.twoitesting.pomclasses;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import uk.co.twoitesting.pomtests.pomTests;

import java.time.Duration;

public class accountPagePOM extends pomTests {
    private final WebDriverWait wait;
    public accountPagePOM(WebDriver driver){ this.driver = driver; this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); }

    public accountPagePOM openOrders() {
        driver.findElement(By.linkText("My account")).click();
        driver.findElement(By.linkText("Orders")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.woocommerce-orders-table")));
        return this;
    }

    public String getOrderTextById(String orderIdDigits) {
        WebElement a = driver.findElement(By.xpath(
                "//table[contains(@class,'woocommerce-orders-table')]//td[contains(@class,'order-number')]//a[contains(@href, '/view-order/" + orderIdDigits + "/')]"
        ));
        return a.getText().trim();
    }
}