package uk.co.twoitesting.pomclasses;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class homePagePOM {
        private WebDriver driver;

        public homePagePOM(WebDriver driver) {
            this.driver = driver;
            PageFactory.initElements(driver, this);
        }

        //Locators
        @FindBy(partiallinkText = "login")
        private WebElement loginlink;

        //Service methods
        public void goLogin(){loginlink.click();}
    }

}
