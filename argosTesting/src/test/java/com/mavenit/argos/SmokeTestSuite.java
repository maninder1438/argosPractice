package com.mavenit.argos;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;

public class SmokeTestSuite extends Hooks {

    @Test
    public void searchTest() throws InterruptedException {
        //Assert - 1
        // search - entering "puma" to the search bar and search for puma
        driver.findElement(By.cssSelector("input[data-test='search-input']")).sendKeys("puma");
        driver.findElement(By.cssSelector("button[type='submit']")).sendKeys(Keys.ENTER);

        //to verify that we are getting searched text"puma" in the end of url
        String url = driver.getCurrentUrl();
        assertThat("Not got results of search term. ", url, endsWith("puma"));

        //Assert -2
        //Collect a item to list - collect names only of all products of puma
        //loop and verify
        //product contains string "puma"

        List<WebElement> productWebElements = driver.findElements(By.cssSelector("a[data-test='component-product-card-title']"));

        for (WebElement indProduct : productWebElements) {
            String actual = indProduct.getText();
            assertThat(actual.toLowerCase(), containsString("puma"));
        }

        //Assert -3
        //to verify that we can see "puma" on the search title
        String actualTitle = driver.findElement(By.className("search-title__term")).getText();
        assertThat(actualTitle, is(equalToIgnoringCase("puma")));


        //    Assert -4
        //verify rating - search for puma and select rating
        //Collect all searched products to list - collect RATING ONLY of all products from list of products
        //loop and verify
        // for example we have selected the products with 4 or more rating
        driver.findElement(By.cssSelector("label[id=\"4 or more-label\"]")).click();
        String selectedRating = "4.0";
        Thread.sleep(4000);

        List<WebElement> ratingWebElements = driver.findElements(By.cssSelector("div[data-test=\"component-ratings\"]"));
        for (WebElement indProductRating : ratingWebElements) {
            String currentRating = indProductRating.getAttribute("data-star-rating");

            double currentRating1 = Double.parseDouble(currentRating);

            // now to verify that previously selected rating is same for all current products
            assertTrue("Current Products have different Rating ", Double.parseDouble(currentRating) > Double.parseDouble(selectedRating));

            System.out.printf("\nThe Rating of individual product is :" + "%.2f", currentRating1);
        }
    }

    @Test
    public void verifyBasketItem() throws InterruptedException {
        //Assert-5
        //search & select macbook air pro and select first product
        // add to basket and then verify the product is same

        driver.findElement(By.cssSelector("#searchTerm")).sendKeys("Apple MacBook Air");
        driver.findElement(By.cssSelector("button[type=\"submit\"]")).sendKeys(Keys.ENTER);
        Thread.sleep(2000);


        String productSelected = driver.findElement(By.cssSelector("a.ProductCardstyles__Title-l8f8q8-12.hllBXz[href=\"/product/1480021\"")).getText();
        driver.findElement(By.cssSelector("a.Buttonstyles__Button-q93iwm-2.bumLPc.btn-cta[href=\"/product/1480021\"")).click();
        Thread.sleep(3000);
        driver.findElement(By.cssSelector("button[data-test=\"component-att-button\"]")).click();
        Thread.sleep(5000);
        driver.findElement(By.cssSelector("a[data-test=\"component-att-button-basket\"]")).click();
        Thread.sleep(5000);
        String productBasket = driver.findElement(By.xpath("//*[@id=\"basket-content\"]/main/div[2]/section[1]/div[1]/ul/li/div/section/div/div/div[2]/div/div[1]/p/a/span")).getText();

        System.out.println("Actual selected product is: " + productSelected);
        System.out.println("Product in Basket is:       " + productBasket);
        assertThat(productBasket, is(equalToIgnoringCase(productSelected)));
    }
}






