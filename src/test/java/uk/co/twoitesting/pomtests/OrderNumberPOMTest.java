package uk.co.twoitesting.pomtests;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import uk.co.twoitesting.pomclasses.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderNumberPOMTest extends pomTests{

    @Test
    @Order(2)
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


        //Capture my orders order number
        accountPagePOM account = new accountPagePOM(driver).openOrders();
        String idDigits = orderNumber.replaceAll("\\D", ""); // "15717"
        String myOrdersText = account.getOrderTextById(idDigits);

        //Normalize both sides to digits-only
        String confirmationOrderId = orderNumber.replaceAll("\\D", "");
        String myOrdersPageOrderId = myOrdersText.replaceAll("\\D", "");


        //Assertion
        assertEquals(confirmationOrderId, myOrdersPageOrderId,
                "Order number differs between confirmation and My Orders");

        assertTrue(myOrdersText.trim().startsWith("#" + confirmationOrderId),
                "My orders text should contain #" + confirmationOrderId + " but was: " + myOrdersText);

        System.out.println("Order number matches across Confirmation ("
                + orderNumber + ") and My Orders (" + myOrdersText + ")");

    }
}

