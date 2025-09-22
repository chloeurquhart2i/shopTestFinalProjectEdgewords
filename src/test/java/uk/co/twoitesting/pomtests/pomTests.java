package uk.co.twoitesting.pomtests;
import org.junit.jupiter.api.Nested;
import uk.co.twoitesting.basetests.baseTest;
import org.junit.jupiter.api.Test;
import uk.co.twoitesting.pomtests.pomTests;
import uk.co.twoitesting.pomclasses.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

public class pomTests extends baseTest {
    @Test
    public void discount_applies_and_total_is_correct() {

        homePagePOM home = new homePagePOM(driver);
        cartPagePOM cart = home.search("Belt")
                .addBeltToCartByButtonValue28()
                .viewCartFromNotice();

        cart.applyCoupon("edgewords");
        cartPagePOM.Totals t = cart.readTotals();

        //Assertions - will fail if discount doesn't work
        assertTrue(t.discount.compareTo(BigDecimal.ZERO) > 0, "Expected discount > 0 after coupon");
        assertEquals(t.expected(), t.totalUI,
                    "Order total should equal subtotal - discount + shipping");

        //Totals for log
        System.out.printf("Subtotal=%s, " +
                        "Discount=%s, " +
                        "Shipping=%s, " +
                        "Total=%s," +
                        " Expected=%s%n",
                    t.subtotal, t.discount, t.shipping, t.totalUI, t.expected());
        }

    @Test
    public void order_number_matches_confirmation_and_orders() {
        homePagePOM home = new homePagePOM(driver);
        shopPagePOM shop = home.goToShop();

            //Add item
        cartPagePOM cart = shop.addFirstVisibleProductToCart()
                .viewCartFromNotice();
        checkoutPagePOM checkout = cart.proceedToCheckout();

        //Fill out form and place the order
        checkout.fillBilling(
                "Chloe", "Urquhart", "2i",
                "123 Happy Street", "Manchester", "M5 5DB",
                "01415731033", "chloe.urquhart@2itesting.com").placeOrder();

        // Capture order number from confirmation
        String orderNumber = checkout.captureOrderNumber();  // e.g. "#15717"
        System.out.println("Captured order number: " + orderNumber);

        // Open My Orders and find same order number
        accountPagePOM account = new accountPagePOM(driver).openOrders();
        String idDigits = orderNumber.replaceAll("\\D", ""); // "15717"
        String myOrdersText = account.getOrderTextById(idDigits);

        //Assertions
        assertTrue(myOrdersText.contains("#" + idDigits),
                "Link href matched, but link text did not contain #" + idDigits + " (was: " + myOrdersText + ")");
        assertEquals(orderNumber, myOrdersText,
                "Order number differs between confirmation and My Orders");

        System.out.println("Order numbers match! Confirmation: " + orderNumber + " My Orders: " + myOrdersText);

        // It appears that the order number confirmation will fail as the Expected is always different to the actual - usually one extra number is added.

        }
    }
