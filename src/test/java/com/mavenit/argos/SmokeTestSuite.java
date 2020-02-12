package com.mavenit.argos;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Random;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SmokeTestSuite extends Hooks {

    @Test
    public void verifyURL() throws FileNotFoundException {
        doSearch("puma");
        //to verify that we are getting searched text"puma" in the end of url
        String url = driver.getCurrentUrl();
        assertThat("Not got results of search term. ", url, endsWith("puma"));
        printSave("'verifyURL' Test Passed:-\n you can see 'puma' in the end of url: " + url+"\n\n");
    }

    @Test
    public void listProductsForStringName() throws InterruptedException, FileNotFoundException {
        //Collect a item to list - collect names only of all products of puma
        //loop and verify
        //list of products contain string "puma"
        doSearch("puma");
        Thread.sleep(3000);
        System.out.println("'listProductsForStringName' Test Passed - you can see product name 'puma' in all below products");
        List<WebElement> productWebElements = driver.findElements(By.cssSelector("a[data-test='component-product-card-title']"));
        for (WebElement indProduct : productWebElements) {
            String actual = indProduct.getText();
            assertThat(actual.toLowerCase(), containsString("puma"));
            System.out.println("The Name of the product is: "+(actual.toLowerCase()));
        }
    }

    @Test
    public void verifySearchTitle() throws InterruptedException, FileNotFoundException {
        doSearch("puma");
        Thread.sleep(4000);
        String actualTitle = driver.findElement(By.className("search-title__term")).getText();
        assertThat(actualTitle, is(equalToIgnoringCase("puma")));
        System.out.println("\n\n'verifySearchTitle' Test Passed - you can see search title name as: " + actualTitle + ", which matches to searched string as 'puma'\n\n");
    }

    @Test
    public void verifyProductRating() throws InterruptedException{
        // for example we have selected the products with 5 star rating
        doSearch("Smart watches");
        Thread.sleep(2000);
        driver.findElement(By.cssSelector("div[data-facet=\"customer rating\"]")).click();
        Thread.sleep(5000);
        driver.findElement(By.cssSelector("label[data-e2e=\"5-link\"]")).click();
        String selectedRating = "5.0";
        Thread.sleep(5000);
        System.out.println("'verifyProductRating' Test Passed - you can see all the below products has rating greater than or equal toe '5'");
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
    public void basketTest() throws InterruptedException {

        doSearch("tripods, monopods and cases");
        Thread.sleep(4000);
        List<WebElement> productWebElements = driver.findElements(By.cssSelector("a[data-test='component-product-card-title']"));
        if (productWebElements.size() == 0) {
            Assert.fail("No Products found with: " + "tripods, monopods and cases");
        }

        // TODO: 2020-02-08 this will be converted in future
        Random random = new Random();
        int randomNumber = random.nextInt(productWebElements.size()-1 );

        WebElement selectedElement = productWebElements.get(randomNumber);
        String selectedProductName = selectedElement.getText();
        selectedElement.click();
        Thread.sleep(2000);
        addToBasket();
        Thread.sleep(5000);
        goToBasket();
        Thread.sleep(2000);
        String actual = getProductNameInBasket();
        assertThat(actual, is(equalToIgnoringCase(selectedProductName)));

        System.out.println("\n\n'basketTest' Test Passed - Actual selected product & the product in the basket is same");
        System.out.println("\nTotal number of products are: " + productWebElements.size());
        System.out.println("Index number of random product is : " + randomNumber);
        System.out.println("\nActual selected product is: " + selectedProductName );
        System.out.println("Product in the Basket is:   " + actual);
    }

    @Test
    public void basketMultipleQuantityTest() throws InterruptedException {

        doSearch("tripods, monopods and cases");
        Thread.sleep(4000);
        List<WebElement> productWebElements = driver.findElements(By.cssSelector("a[data-test='component-product-card-title']"));
        List<WebElement> price = driver.findElements(By.cssSelector("div[class^=\"ProductCardstyles__PriceText-l8f8q8-14\"]"));
        if (productWebElements.size() == 0) {
            Assert.fail("No Products found with: " + "tripods, monopods and cases");
        }
        // TODO: 2020-02-08 this will be converted in future
        Random random = new Random();
        int randomNumber = random.nextInt(productWebElements.size()-1 );

        WebElement selectedElement = productWebElements.get(randomNumber);
        String selectedProductName = selectedElement.getText();
        String Price= price.get(randomNumber).getText().replace("£", "");
        double selectedProductPrice = Double.parseDouble(Price);

        selectedElement.click();
        Thread.sleep(2000);

        String selectedQty = quantityToSelect("5");
        double qtySelected = Double.parseDouble(selectedQty);
        double totalPrice = selectedProductPrice*qtySelected;
        String totalPriceOfSelectedProducts = String.format("%.2f", totalPrice);
        Thread.sleep(2000);

        addToBasket();
        Thread.sleep(4000);

        goToBasket();

        Thread.sleep(2000);
        String actualName = getProductNameInBasket();
        String actualPrice = getProductPriceInBasket();
        String totalPriceOfBasketProducts = actualPrice.replace("£", "");

        assertThat(actualName, is(equalToIgnoringCase(selectedProductName)));
        assertThat(totalPriceOfSelectedProducts,is(equalToIgnoringCase(totalPriceOfBasketProducts)));

        System.out.println("\n\n'basketMultipleQuantityTest' Test Passed: Actual selected product & the product in the basket is same and total price of selected products are matching");

        System.out.println("\nTotal price of Selected Products: £"+totalPriceOfSelectedProducts);
        System.out.println("Total price of Basket Products: £" +totalPriceOfBasketProducts);

        System.out.println("\nSingle price of each product is= £"+selectedProductPrice);
        System.out.println("The quantity selected is: "+qtySelected);

        System.out.println("\nTotal number of products are: " + productWebElements.size());
        System.out.println("Index number of random product is : " + randomNumber);

        System.out.println("\nActual selected product is: " + selectedProductName );
        System.out.println("Product in the Basket is:   " + actualName);
    }


    @Test
    public void basketMultipleProductTest() throws InterruptedException {
        int numberOfProductsBuying = 2;

        //adding first product

        doSearch("tripods, monopods and cases");
        Thread.sleep(4000);
        List<WebElement> productWebElements = driver.findElements(By.cssSelector("a[data-test='component-product-card-title']"));
        List<WebElement> price = driver.findElements(By.cssSelector("div[class^=\"ProductCardstyles__PriceText-l8f8q8-14\"]"));
        if (productWebElements.size() == 0) {
            Assert.fail("No Products found with: " + "tripods, monopods and cases");
        }
        // TODO: 2020-02-08 this will be converted in future
        Random random = new Random();
        int randomNumber = random.nextInt(productWebElements.size()-1 );

        WebElement selectedElement = productWebElements.get(randomNumber);
        String Price= price.get(randomNumber).getText().replace("£", "");
        double selectedProductPrice = Double.parseDouble(Price);

        selectedElement.click();
        Thread.sleep(2000);

        String selectedQty = quantityToSelect("2");
        double qtySelected = Double.parseDouble(selectedQty);
        double totalPrice = selectedProductPrice*qtySelected;
        Thread.sleep(2000);

        addToBasket();
        Thread.sleep(4000);
        continueShopping();
        Thread.sleep(4000);


        //adding second product
        doSearch("Laptops");
        Thread.sleep(3000);
        List<WebElement> productWebElements1 = driver.findElements(By.cssSelector("a[data-test='component-product-card-title']"));
        List<WebElement> price1 = driver.findElements(By.cssSelector("div[class^=\"ProductCardstyles__PriceText-l8f8q8-14\"]"));
        if (productWebElements1.size() == 0) {
            Assert.fail("No Products found with: " + "Laptops");
        }
        // TODO: 2020-02-08 this will be converted in future
        Random random1 = new Random();
        int randomNumber1 = random1.nextInt(productWebElements1.size()-1 );

        WebElement selectedElement1 = productWebElements1.get(randomNumber1);
        String selectedProductName1 = selectedElement1.getText();
        String Price1= price1.get(randomNumber1).getText().replace("£", "");
        double selectedProductPrice1 = Double.parseDouble(Price1);
        Thread.sleep(2000);
        selectedElement1.click();
        Thread.sleep(2000);

        String selectedQty1 = quantityToSelect("3");
        double qtySelected1 = Double.parseDouble(selectedQty1);
        double totalPrice1 = selectedProductPrice1*qtySelected1;
        Thread.sleep(2000);
        addToBasket();
        Thread.sleep(4000);
        goToBasket();

        double Total = totalPrice+totalPrice1;
        String subTotalActual = String.format("%.2f",Total);

        String subTotalExpected = (driver.findElement(By.cssSelector("div[data-e2e=\"basket-total-price\"]")).getText()).replace("£", "").replace(",","");
        assertThat(subTotalActual, is(equalToIgnoringCase(subTotalExpected)));


        List<WebElement> numberOfProducts = driver.findElements(By.cssSelector(".ProductCard__productLinePrice__3QC7V"));
        int numberOfProductsInBasket = numberOfProducts.size();
        assertEquals(numberOfProductsBuying,numberOfProductsInBasket);

        System.out.println("\n\n'basketMultipleProductTest' Test Passed: Number of products selected & number of products in the basket is same and total price of all products in the basket is correct");
        System.out.println("Actual number of products buying = "+numberOfProductsBuying);
        System.out.println("Actual number of products in basket = "+numberOfProductsInBasket);
        System.out.println("Actual total is: £"+subTotalActual);
        System.out.println("Expected total is: £"+subTotalExpected);
    }

    public void doSearch(String searchTerm) {
        driver.findElement(By.cssSelector("input[data-test='search-input']")).sendKeys(searchTerm);
        driver.findElement(By.cssSelector("button[type='submit']")).sendKeys(Keys.ENTER);
    }

    public String quantityToSelect(String qty) {
        new Select(driver.findElement(By.cssSelector("select[id=\"add-to-trolley-quantity\"]"))).selectByVisibleText(qty);
        return qty;
    }

    public void addToBasket() {
        driver.findElement(By.cssSelector("button[data-test='component-att-button']")).click();
    }

    public void continueShopping() {
        driver.findElement(By.cssSelector("button[data-test=\"component-att-button-continue\"]")).click();
    }

    public void goToBasket() {
        driver.findElement(By.cssSelector(".xs-row a[data-test='component-att-button-basket']")).click();
    }

    public String getProductNameInBasket(){
        return driver.findElement(By.cssSelector(".ProductCard__content__9U9b1.xsHidden.lgFlex .ProductCard__titleLink__1PgaZ")).getText();
    }

    public String getProductPriceInBasket(){
        return driver.findElement(By.cssSelector(".ProductCard__productLinePrice__3QC7V")).getText();
    }
    public void printSave(String print) throws FileNotFoundException {
        FileOutputStream f = new FileOutputStream("print.txt");
        System.setOut(new PrintStream(f));
        System.out.println(print);
    }
}





