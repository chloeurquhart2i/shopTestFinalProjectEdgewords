package uk.co.twoitesting.tests;


import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.twoitesting.basetests.baseTest;
import uk.co.twoitesting.utilities.StaticHelpers;

import java.awt.*;
import java.math.BigDecimal;

import static java.awt.SystemColor.text;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class discountTestCodeDump extends baseTest {

    private static final Logger log = LoggerFactory.getLogger(discountTestCodeDump.class);

    @Test
    public void discountTest() {
        System.out.println("Starting test...");
        System.out.println("Searching for product...");


        //Searching for and adding 'Belt' to cart

        StaticHelpers.waitForElementToBeClickableHelper(driver, By.cssSelector("#woocommerce-product-search-field-0"),7);
        driver.findElement(By.cssSelector("#woocommerce-product-search-field-0")).sendKeys("Belt" + Keys.ENTER);

        System.out.println("Adding item to cart");
        driver.findElement(By.cssSelector("button[value='28']")).click();


        //viewing the cart
        System.out.println("Viewing cart...");
        StaticHelpers.waitForElementToBeClickableHelper(driver, By.cssSelector("div[role='alert'] a[class='button wc-forward']"), 5);
        driver.findElement(By.cssSelector("div[role='alert'] a[class='button wc-forward']")).click();


        //applying coupon code 1 - edgewords
        System.out.println("Applying coupon code 'edgewords'...");
        driver.findElement(By.cssSelector("#coupon_code")).sendKeys("edgewords");
        driver.findElement(By.cssSelector("button[value='Apply coupon']")).click();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        boolean getResult; {

            //Here i will capture the elements from the table - convert them into a string, convert them into bigDecimal in order to calculate with

            String subtotalTxt = driver.findElement(
                    By.cssSelector(".cart-subtotal > td")
            ).getText();
            java.math.BigDecimal subtotal = new java.math.BigDecimal(
                    subtotalTxt.replace("£","").replace(",","")
                            .replace("−","-").replace("–","-")
                            .replaceAll("[^0-9\\.-]","")
            );

            String discountTxt = driver.findElement(
                    By.cssSelector(".cart-discount > td")
            ).getText();
            java.math.BigDecimal discount = new java.math.BigDecimal(
                    discountTxt.replace("£","").replace(",","")
                            .replace("−","-").replace("–","-")
                            .replaceAll("[^0-9\\.-]","")
            ).abs();

            java.util.List<org.openqa.selenium.WebElement> shipEls = driver.findElements(
                    By.cssSelector("label:nth-child(2)")
            );
            java.math.BigDecimal shipping;
            if (!shipEls.isEmpty()) {
                String shipTxt = shipEls.get(0).getText();
                shipping = new java.math.BigDecimal(
                        shipTxt.replace("£","").replace(",","")
                                .replace("−","-").replace("–","-")
                                .replaceAll("[^0-9\\.-]","")
                );
            } else {
                String shipLabelTxt = driver.findElement(
                        By.cssSelector("tr.shipping td label:nth-child(2)")
                ).getText(); // e.g. "Flat rate: £3.95"
                shipping = new java.math.BigDecimal(
                        shipLabelTxt.replace("£","").replace(",","")
                                .replace("−","-").replace("–","-")
                                .replaceAll("[^0-9\\.-]","")
                );
            }

            String totalTxt = driver.findElement(
                    By.cssSelector(".order-total > td")
            ).getText();
            java.math.BigDecimal totalUI = new java.math.BigDecimal(
                    totalTxt.replace("£","").replace(",","")
                            .replace("−","-").replace("–","-")
                            .replaceAll("[^0-9\\.-]","")
            );


            //Here i will assert if the coupon is working as expected

            java.math.BigDecimal expected = subtotal.subtract(discount).add(shipping)
                    .setScale(2, java.math.RoundingMode.HALF_UP);


            org.junit.jupiter.api.Assertions.assertTrue(
                    discount.compareTo(java.math.BigDecimal.ZERO) > 0,
                    "Expected a discount > 0 after applying the coupon"
            );

            org.junit.jupiter.api.Assertions.assertEquals(
                    expected, totalUI,                    "Order total should equal subtotal - discount + shipping + tax"
            );

            System.out.println("Coupon calculations working as expected");
        }










    }


}
