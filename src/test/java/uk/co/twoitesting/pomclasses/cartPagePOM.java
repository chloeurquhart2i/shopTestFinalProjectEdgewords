package uk.co.twoitesting.pomclasses;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import uk.co.twoitesting.pomtests.pomTests;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class cartPagePOM extends pomTests {
    private final WebDriverWait wait;

    public cartPagePOM(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

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

    //Helper Read Subtotal Only
    public BigDecimal readSubtotal() {
        String subtotalText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart-subtotal > td"))).getText();
        return toMoney(subtotalText);
    }

    //Helper Read Discount amount (sums all discounts if more than one)
    public BigDecimal readDiscountAmount() {
        List<WebElement> discountCells = driver.findElements(By.cssSelector("tr.cart-discount td"));
        BigDecimal discountTotal = BigDecimal.ZERO;
        for (WebElement discountCell : discountCells) {
            BigDecimal discountValue = toMoney(discountCell.getText()).abs();
            discountTotal = discountTotal.add(discountValue);
        }
        return discountTotal;
    }

    //Get the applied coupon text, e.g. "edgewords"
    public String readAppliedCouponLabel() {
        String discountRowText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("tr.cart-discount"))).getText().trim();
        return discountRowText.replace("Coupon ", "").trim();
    }

    //Read subtotal, discount, shipping and total from the table
    public Totals readTotals() {
        String subtotalText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart-subtotal > td"))).getText();
        String discountText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart-discount > td"))).getText();


        //capture shipping label price
        BigDecimal shippingCost;
        List<WebElement> shippingPriceElements = driver.findElements(By.cssSelector("tr.shipping td .woocommerce-Price-amount bdi"));
        String shippingLabelText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("tr.shipping td label:nth-child(2)"))).getText();
        shippingCost = toMoney(shippingLabelText);

        String totalText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".order-total > td"))).getText();

        BigDecimal subtotal = toMoney(subtotalText);
        BigDecimal discount = toMoney(discountText).abs();
        BigDecimal totalfromUI = toMoney(totalText);

        return new Totals(subtotal, discount, shippingCost, totalfromUI);

    }

    public List<String> getAppliedCouponCodes() {

        // Find all rows in the cart totals table that represent applied coupons
        List<WebElement> couponRows = driver.findElements(By.cssSelector("tr.cart-discount"));

        List<String> appliedCodes = new ArrayList<>();

        for (WebElement couponRow : couponRows) {

            // Example row text: "Coupon: edgewords -£16.50"
            String rowText = couponRow.getText().trim();

            // Remove the "Coupon:" prefix, leaving "edgewords -£16.50"
            String cleanedText = rowText.replace("Coupon:", "").trim();

            // If there is extra text after the code (like "-£16.50"), strip it off
            int firstSpaceIndex = cleanedText.indexOf(' ');
            String couponCode = (firstSpaceIndex > 0)
                    ? cleanedText.substring(0, firstSpaceIndex).trim()
                    : cleanedText;

            appliedCodes.add(couponCode.toLowerCase());
        }

        return appliedCodes;
    }

    public void assertCouponApplied(String expectedCode) {
        List<String> codes = getAppliedCouponCodes();
        org.junit.jupiter.api.Assertions.assertTrue(
                codes.contains(expectedCode.toLowerCase()),
                "Expected coupon '" + expectedCode + "' to be applied, but saw: " + codes
        );
    }


    public void removeAllCouponsIfPresent() {
        List<WebElement> removeLinks = driver.findElements(By.cssSelector("a.woocommerce-remove-coupon"));
        if (!removeLinks.isEmpty()) {
            for (WebElement link : removeLinks) {
                link.click();
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("tr.cart-discount")));
            }
        }
    }

    private BigDecimal toMoney(String moneyString) {
        if (moneyString == null || moneyString.isBlank()) {
            throw new IllegalArgumentException("Money string cannot be null or blank");
        }

        // Normalize common formatting issues
        String normalised = moneyString
                .replace("£", "")        // strip currency symbol
                .replace(",", "")        // strip thousand separators
                .replace("−", "-")       // replace special minus symbols
                .replace("–", "-")       // replace en-dash with normal minus
                .trim();

        // Keep only digits, decimal point, and minus sign
        String numericOnly = normalised.replaceAll("[^0-9.-]", "");

        if (numericOnly.isEmpty()) {
            throw new IllegalArgumentException("No numeric value found in: " + moneyString);
        }

        return new BigDecimal(numericOnly).setScale(2, RoundingMode.HALF_UP);
    }

    public record Totals(BigDecimal subtotal, BigDecimal discount, BigDecimal shipping, BigDecimal totalUI) {
        public BigDecimal expected() {
            return subtotal.subtract(discount).add(shipping).setScale(2, RoundingMode.HALF_UP);
        }
    }

}







































    /* /** Reads subtotal, discount, shipping, (tax if present) and total from the totals table.
    public Totals readTotals() {
        String subtotalTxt = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".cart-subtotal > td"))).getText();

        String discountTxt = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".cart-discount > td"))).getText();

        // Shipping: either a bdi amount or a label like "Flat rate: £3.95"
        BigDecimal shipping;
        List<WebElement> shipAmt = driver.findElements(By.cssSelector("tr.shipping td .woocommerce-Price-amount bdi"));
        if (!shipAmt.isEmpty()) {
            shipping = toMoney(shipAmt.getFirst().getText());
        } else {
            String shipLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("tr.shipping td label:nth-child(2)"))).getText();
            shipping = toMoney(shipLabel);
        }

        String totalTxt = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".order-total > td"))).getText();

        BigDecimal subtotal = toMoney(subtotalTxt);
        BigDecimal discount = toMoney(discountTxt).abs();
        BigDecimal totalUI  = toMoney(totalTxt);

        return new Totals(subtotal, discount, shipping, totalUI);
    }

    // Used this method instead of ParseMoney as I couldn't get ParseMoney to work - so that the money is converted to a usable data chunk
    private BigDecimal toMoney(String s) {
        String cleaned = s.replace("£","")
                .replace(",","")
                .replace("−","-").replace("–","-")
                .replaceAll("[^0-9.-]","");
        if (cleaned.isEmpty()) throw new IllegalArgumentException("No money in: " + s);
        return new BigDecimal(cleaned).setScale(2, RoundingMode.HALF_UP);
    }

    public record Totals(BigDecimal subtotal, BigDecimal discount, BigDecimal shipping, BigDecimal totalUI) {

        public BigDecimal expected() {
                return subtotal.subtract(discount).add(shipping).setScale(2, RoundingMode.HALF_UP);
            }
        } */
