package uk.co.twoitesting.tests;


import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import uk.co.twoitesting.basetests.baseTest;
import uk.co.twoitesting.utilities.StaticHelpers;

import java.awt.*;
import java.math.BigDecimal;

import static java.awt.SystemColor.text;


public class discountTestCodeDump extends baseTest {

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

        //Asserting that coupon code 1 works
        /* Psuedocode:
        Capture the subtotal
        Capture the total with coupon applied
        add the shipping fee
        assert total against 15% off + shipping fee
         */


        /*
        //applying coupon code 2 - 2idiscount
        System.out.println("Applying coupon code '2idiscount'...");
        driver.findElement(By.cssSelector("#coupon_code")).clear();
        driver.findElement(By.cssSelector("#coupon_code")).sendKeys("2idiscount");
        driver.findElement(By.cssSelector("button[value='Apply coupon']")).click();

        //Asserting that coupon code 2 works

         */





    }

/* Next steps:
    Capture the price with the new coupon code
    Assert that the coupon code is working as expected

    Split everything into Pom Classes
    Make a more streamlines test on Pom Tests
 */


}
