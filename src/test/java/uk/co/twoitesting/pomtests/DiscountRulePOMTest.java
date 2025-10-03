package uk.co.twoitesting.pomtests;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import uk.co.twoitesting.basetests.baseTest;
import uk.co.twoitesting.pomclasses.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

public class DiscountRulePOMTest extends baseTest {

    @ParameterizedTest(name = "Coupon {0} @ rate {1}")
    @CsvFileSource(resources = "/coupons.csv", numLinesToSkip = 1)
    @Order(1)
    public void coupon_applies_expected_percentage(String couponCode, double discountRateFraction) {

        // Arrange: get to cart with one Belt in it
        homePagePOM homePage = new homePagePOM(driver);
        cartPagePOM cartPage = homePage.search("Belt")
                .addBeltToCartByButtonValue28()
                .viewCartFromNotice();

        //Clear the coupon bar
        cartPage.removeAllCouponsIfPresent();


        //Apply Coupon from csv
        cartPage.applyCoupon(couponCode);
        cartPage.assertCouponApplied(couponCode);


        //Read values from UI
        BigDecimal subtotalValue = cartPage.readSubtotal();
        BigDecimal discountFromUI = cartPage.readDiscountAmount();
        cartPagePOM.Totals totals = cartPage.readTotals();


        //Calculate expected discount from CSV rate
        BigDecimal expectedDiscount = subtotalValue
                .multiply(new BigDecimal(String.valueOf(discountRateFraction)))
                .setScale(2, RoundingMode.HALF_UP);

        //Assert coupon gave the correct percentage discount
        assertEquals(
                expectedDiscount, discountFromUI,
                "Coupon '" + couponCode + "' should discount " + (discountRateFraction * 100) + "% of subtotal"
        );

        //Assert total maths still lines up
        assertEquals(
                totals.expected(), totals.totalUI(),
                "Order total should equal subtotal - discount + shipping"
        );

        //log
        System.out.printf(
                "Coupon=%s | Subtotal=%s | ExpectedDiscount=%s | DiscountFromUI=%s | Shipping=%s | Total=%s%n",
                couponCode, subtotalValue, expectedDiscount, discountFromUI, totals.shipping(), totals.totalUI()
        );
    }
}
