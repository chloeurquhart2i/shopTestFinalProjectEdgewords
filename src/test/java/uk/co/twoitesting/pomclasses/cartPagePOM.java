package uk.co.twoitesting.pomclasses;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import uk.co.twoitesting.pomtests.pomTests;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.List;

public class cartPagePOM extends pomTests {
    private final WebDriverWait wait;
    public cartPagePOM(WebDriver driver){ this.driver = driver; this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); }

    public cartPagePOM applyCoupon(String code) {
        //Capturing elements and implicit waits to allow this
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#coupon_code"))).sendKeys(code);
        driver.findElement(By.cssSelector("button[value='Apply coupon']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("tr.cart-discount")));
        return this;
    }

    public checkoutPagePOM proceedToCheckout() {
        //Locators
        driver.findElement(By.linkText("Proceed to checkout")).click();
        return new checkoutPagePOM(driver);
    }

    /** Reads subtotal, discount, shipping, (tax if present) and total from the totals table. */
    public Totals readTotals() {
        String subtotalTxt = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".cart-subtotal > td"))).getText();

        String discountTxt = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".cart-discount > td"))).getText();

        // Shipping: either a bdi amount or a label like "Flat rate: £3.95"
        BigDecimal shipping;
        List<WebElement> shipAmt = driver.findElements(By.cssSelector("tr.shipping td .woocommerce-Price-amount bdi"));
        if (!shipAmt.isEmpty()) {
            shipping = toMoney(shipAmt.get(0).getText());
        } else {
            String shipLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("tr.shipping td label:nth-child(2)"))).getText();
            shipping = toMoney(shipLabel);
        }

        String totalTxt = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".order-total > td"))).getText();

        BigDecimal subtotal = toMoney(subtotalTxt);
        BigDecimal discount = toMoney(discountTxt).abs(); // UI shows "-£X.XX"
        BigDecimal totalUI  = toMoney(totalTxt);

        return new Totals(subtotal, discount, shipping, tax, totalUI);
    }

    // Used this method instead of ParseMoney as I couldn't get ParseMoney to work - so that the money is converted to a usable data chunk
    private BigDecimal toMoney(String s) {
        String cleaned = s.replace("£","")
                .replace(",","")
                .replace("−","-").replace("–","-")
                .replaceAll("[^0-9\\.-]","");
        if (cleaned.isEmpty()) throw new IllegalArgumentException("No money in: " + s);
        return new BigDecimal(cleaned).setScale(2, RoundingMode.HALF_UP);
    }

    public static class Totals {
        public final BigDecimal subtotal, discount, shipping, tax, totalUI;
        public Totals(BigDecimal sub, BigDecimal disc, BigDecimal ship, BigDecimal tax, BigDecimal total) {
            this.subtotal = sub; this.discount = disc; this.shipping = ship; this.tax = tax; this.totalUI = total;
        }
        public BigDecimal expected() {
            return subtotal.subtract(discount).add(shipping).add(tax).setScale(2, RoundingMode.HALF_UP);
        }
    }
}