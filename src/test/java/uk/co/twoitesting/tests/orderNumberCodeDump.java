package uk.co.twoitesting.tests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.co.twoitesting.basetests.baseTest;
import java.util.*;

import java.time.Duration;


public class orderNumberCodeDump extends baseTest {

    @Test
    public void orderNumberTest() {
        System.out.println("Starting test...");

        //Navigating Shop
        driver.findElement(By.linkText("Shop")).click();
        driver.findElement(By.linkText("Add to cart")).click();
        driver.findElement(By.linkText("View cart")).click();
        System.out.println("Added item to cart...");

        //Proceed to checkout
        driver.findElement(By.linkText("Proceed to checkout")).click();

        try {
            Thread.sleep(3000
            );
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Fill out the form

        driver.findElement(By.xpath("//*[@id=\"billing_first_name\"]")).click();
        driver.findElement(By.xpath("//*[@id=\"billing_first_name\"]")).clear();
        driver.findElement(By.xpath("//*[@id=\"billing_first_name\"]")).sendKeys("Chloe");

        driver.findElement(By.xpath("//*[@id=\"billing_last_name\"]")).click();
        driver.findElement(By.xpath("//*[@id=\"billing_last_name\"]")).clear();
        driver.findElement(By.xpath("//*[@id=\"billing_last_name\"]")).sendKeys("Urquhart");

        driver.findElement(By.xpath("//*[@id=\"billing_company\"]")).click();
        driver.findElement(By.xpath("//*[@id=\"billing_company\"]")).clear();
        driver.findElement(By.xpath("//*[@id=\"billing_company\"]")).sendKeys("2i");

        driver.findElement(By.xpath("//*[@id=\"billing_address_1\"]")).click();
        driver.findElement(By.xpath("//*[@id=\"billing_address_1\"]")).clear();
        driver.findElement(By.xpath("//*[@id=\"billing_address_1\"]")).sendKeys("123 Happy Street");

        driver.findElement(By.xpath("//*[@id=\"billing_city\"]")).click();
        driver.findElement(By.xpath("//*[@id=\"billing_city\"]")).clear();
        driver.findElement(By.xpath("//*[@id=\"billing_city\"]")).sendKeys("Manchester");

        driver.findElement(By.xpath("//*[@id=\"billing_postcode\"]")).click();
        driver.findElement(By.xpath("//*[@id=\"billing_postcode\"]")).clear();
        driver.findElement(By.xpath("//*[@id=\"billing_postcode\"]")).sendKeys("M5 5DB");

        driver.findElement(By.xpath("//*[@id=\"billing_phone\"]")).click();
        driver.findElement(By.xpath("//*[@id=\"billing_phone\"]")).clear();
        driver.findElement(By.xpath("//*[@id=\"billing_phone\"]")).sendKeys("01415731033");

        driver.findElement(By.xpath("//*[@id=\"billing_email\"]")).click();
        driver.findElement(By.xpath("//*[@id=\"billing_email\"]")).clear();
        driver.findElement(By.xpath("//*[@id=\"billing_email\"]")).sendKeys("chloe.urquhart@2itesting.com");

        //place the order

        System.out.println("Placing order...");
        driver.findElement(By.cssSelector("#place_order")).click();
        try {
            Thread.sleep(3000
            );
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Capturing order number

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement orderNumberElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("#post-6 > div > div > div > ul > li.woocommerce-order-overview__order.order > strong")
                )
        );

        String orderNumber = orderNumberElement.getText();
        System.out.println("Captured order number: " + orderNumber);

        //Navigating to My Orders

        driver.findElement(By.linkText("My account")).click();
        driver.findElement(By.linkText("Orders")).click();

        //Capturing order number from My Orders

        String orderId = orderNumber.replaceAll("\\D", "");
        WebElement orderLinkByHref = driver.findElement(By.xpath(
                "//table[contains(@class,'woocommerce-orders-table')]//td[contains(@class,'order-number')]//a[contains(@href, '/view-order/" + orderId + "/')]"
        ));

        String myOrdersText = orderLinkByHref.getText().trim();
        org.junit.jupiter.api.Assertions.assertTrue(
                myOrdersText.contains("#" + orderId),
                "Link href matched, but link text did not contain #" + orderId + " (was: " + myOrdersText + ")"
        );

        //Asserting the order number is correct

        if (myOrdersText.equals("#" + orderId)) {
            System.out.println("Order numbers match! Confirmation: " + orderNumber + " | My Orders: " + myOrdersText);
        } else {
            System.out.println(" Order numbers do NOT match! Confirmation: " + orderNumber + " | My Orders: " + myOrdersText);
        }

        //Done - Just need to organise into modules/POMs


    }
    }