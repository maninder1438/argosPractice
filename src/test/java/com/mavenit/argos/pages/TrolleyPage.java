package com.mavenit.argos.pages;

import com.mavenit.argos.driver.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class TrolleyPage extends DriverFactory {

    public String getProductNameInTrolley() throws InterruptedException {
        Thread.sleep(2500);
        return driver.findElement(By.cssSelector(".ProductCard__content__9U9b1.xsHidden.lgFlex .ProductCard__titleLink__1PgaZ")).getText();
    }

    public String getProductPriceInTrolley() {
        return driver.findElement(By.cssSelector(".ProductCard__productLinePrice__3QC7V")).getText();
    }

    public int numberOfProductsinTrolley() {
        List<WebElement> numberOfProducts = driver.findElements(By.cssSelector(".ProductCard__productLinePrice__3QC7V"));
        int numberOfProductsInBasket = numberOfProducts.size();
        return numberOfProductsInBasket;
    }
    public String trolleyTotalProductPrice() {
        String trolleyTotal = (driver.findElement(By.cssSelector("div[data-e2e=\"basket-total-price\"]")).getText()).replace("£", "").replace(",", "");
        return trolleyTotal;
    }
}
