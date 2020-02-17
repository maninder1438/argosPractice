package com.mavenit.argos;

import com.mavenit.argos.pages.HomePage;
import com.mavenit.argos.pages.ProductDescriptionPage;
import com.mavenit.argos.pages.ResultsPage;
import com.mavenit.argos.pages.TrolleyPage;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SmokeTestSuite extends Hooks {
    private HomePage homePage = new HomePage();
    private ResultsPage resultsPage = new ResultsPage();
    private TrolleyPage trolleyPage = new TrolleyPage();
    private ProductDescriptionPage productDescriptionPage = new ProductDescriptionPage();

    @Test
    public void searchTest() throws InterruptedException {
        String searchTerm = "puma";
        homePage.doSearch(searchTerm);
        assertThat("Not got results of search term. ", homePage.getCurrentUrl(), endsWith(searchTerm));
        List<String> actualProductList = resultsPage.getAllProductNames();
        for (String indProduct : actualProductList) {
            assertThat(indProduct.toLowerCase(), containsString(searchTerm));
        }
        assertThat(resultsPage.getSearchTitle(), is(equalToIgnoringCase(searchTerm)));
    }

    @Test
    public void basketTest() throws InterruptedException {
        homePage.doSearch("puma");
        String selectedProductName = resultsPage.selectAnyProduct();
        productDescriptionPage.addToTrolley();
        Thread.sleep(3000);
        productDescriptionPage.goToTrolley();
        Thread.sleep(2000);
        String actual = trolleyPage.getProductNameInTrolley();
        assertThat(actual, is(equalToIgnoringCase(selectedProductName)));
    }

    @Test
    public void verifyProductRating() throws InterruptedException {
        String searchTerm = "laptop";
        String selectRating = "5 or more";
        homePage.doSearch(searchTerm);
        String rating = resultsPage.selectProductRating(selectRating);
        String selectedRating = rating.replace(" or more", "");
        Thread.sleep(5000);
        List<String> actualRatingList = resultsPage.getAllProductRatings();
        for (String indRating : actualRatingList) {
            assertTrue("Current Products have different Rating ", Double.parseDouble(indRating) >= Double.parseDouble(selectedRating));
        }
    }

    @Test
    public void basketMultipleQuantityTest() throws InterruptedException {
        String searchTerm = "tripods, monopods and cases";
        String productQuantity = "5";
        homePage.doSearch(searchTerm);

        String selectedProductName = resultsPage.selectAnyProduct();
        double selectedProductPrice = Double.parseDouble(productDescriptionPage.productPrice());

        double qtySelected = Double.parseDouble(productDescriptionPage.quantityToSelect(productQuantity));
        String totalPriceOfSelectedProducts = String.format("%.2f", selectedProductPrice * qtySelected);

        productDescriptionPage.addToTrolley();
        productDescriptionPage.goToTrolley();

        String actualName = trolleyPage.getProductNameInTrolley();
        String totalPriceOfBasketProducts = trolleyPage.getProductPriceInTrolley().replace("£", "");
        assertThat(actualName, is(equalToIgnoringCase(selectedProductName)));
        assertThat(totalPriceOfSelectedProducts, is(equalToIgnoringCase(totalPriceOfBasketProducts)));
    }

    @Test
    public void basketMultipleProductTest() throws InterruptedException {
        byte numberOfProductsBuying = 2;
        String firstProductcategory = "tripods, monopods and cases";
        String secondProductCategory = "laptop";

        //adding first product
        homePage.doSearch(firstProductcategory);
        resultsPage.selectAnyProduct();
        double selectedProductPrice1 = Double.parseDouble(productDescriptionPage.productPrice());
        productDescriptionPage.addToTrolley();
        productDescriptionPage.continueShopping();

        //adding second product
        homePage.doSearch(secondProductCategory);
        resultsPage.selectAnyProduct();
        double selectedProductPrice2 = Double.parseDouble(productDescriptionPage.productPrice());
        productDescriptionPage.addToTrolley();
        productDescriptionPage.goToTrolley();

        String subTotalActual = String.format("%.2f", selectedProductPrice1 + selectedProductPrice2);
        String subTotalExpected = trolleyPage.trolleyTotalProductPrice();
        assertThat(subTotalActual, is(equalToIgnoringCase(subTotalExpected)));

        int numberOfProductsInBasket = trolleyPage.numberOfProductsinTrolley();
        assertEquals(numberOfProductsBuying, numberOfProductsInBasket);
    }

    @Test
    public void basketMultipleProductMultipleQuantityTest() throws InterruptedException {
        byte numberOfProductsBuying = 2;
        String firstProductCategory = "tripods, monopods and cases";
        String firstProductQuantity = "5";
        String secondProductCategory = "laptop";
        String secondProductQuantity = "2";

        //adding first product
        homePage.doSearch(firstProductCategory);
        resultsPage.selectAnyProduct();
        double selectedProductPrice1 = Double.parseDouble(productDescriptionPage.productPrice());
        double qtySelected1 = Double.parseDouble(productDescriptionPage.quantityToSelect(firstProductQuantity));
        double totalPriceFirstProduct = selectedProductPrice1 * qtySelected1;
        productDescriptionPage.addToTrolley();
        productDescriptionPage.continueShopping();

        //adding second product
        homePage.doSearch(secondProductCategory);
        resultsPage.selectAnyProduct();
        double selectedProductPrice2 = Double.parseDouble(productDescriptionPage.productPrice());
        double qtySelected2 = Double.parseDouble(productDescriptionPage.quantityToSelect(secondProductQuantity));
        double totalPriceOfSecondProduct = selectedProductPrice2 * qtySelected2;
        productDescriptionPage.addToTrolley();
        productDescriptionPage.goToTrolley();

        String totalActual = String.format("%.2f", totalPriceFirstProduct + totalPriceOfSecondProduct);
        String totalExpected = trolleyPage.trolleyTotalProductPrice();
        assertThat(totalActual, is(equalToIgnoringCase(totalExpected)));

        int numberOfProductsInBasket = trolleyPage.numberOfProductsinTrolley();
        assertEquals(numberOfProductsBuying, numberOfProductsInBasket);
    }
}











