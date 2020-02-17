package com.mavenit.argos.pages;

import com.mavenit.argos.driver.DriverFactory;
import com.mavenit.argos.utils.Helpers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class ResultsPage extends DriverFactory {

    public String getSearchTitle() {
        return driver.findElement(By.className("search-title__term")).getText();
    }

    public List<String> getAllProductNames() {
        List<String> productNamesList = new ArrayList<>();
        List<WebElement> productWebElements = isProductsAvailable();
        for (WebElement indProduct : productWebElements) {
            String actual = indProduct.getText();
            productNamesList.add(actual);
        }
        return productNamesList;
    }

    public List<String> getAllProductRatings() {
        List<String> productRatingList = new ArrayList<>();
        List<WebElement> productWebElements = isProductRatingAvailable();
        for (WebElement indProduct : productWebElements) {
            String currentRating = indProduct.getAttribute("data-star-rating");
            productRatingList.add(currentRating);
        }
        return productRatingList;
    }

    public String selectAnyProduct() {
        List<WebElement> productWebElements = isProductsAvailable();
        int productSize = productWebElements.size();

        int randomNumber = new Helpers().randomNumberGenerator(productSize);

        WebElement selectedElement = productWebElements.get(randomNumber);
        String selectedProductName = selectedElement.getText();
        selectedElement.click();

        return selectedProductName;

    }

    private List<WebElement> isProductsAvailable() {
        List<WebElement> productWebElements = driver.findElements(By.cssSelector("a[data-test='component-product-card-title']"));
        if (productWebElements.size() == 0) {
            // fail("Zero products found .....");
            throw new RuntimeException("Zero products found .....");
        }
        return productWebElements;
    }

    private List<WebElement> isProductRatingAvailable() {
        List<WebElement> productWebElements = driver.findElements(By.cssSelector("div[data-test=\"component-ratings\"]"));
        if (productWebElements.size() == 0) {
            // fail("Zero products found .....");
            throw new RuntimeException("Zero products found .....");
        }
        return productWebElements;
    }

    public String selectProductRating(String selectRating) throws InterruptedException {
        Thread.sleep(3000);
        driver.findElement(By.cssSelector("div[data-facet=\"customer rating\"]")).click();
        Thread.sleep(4000);
        if (selectRating == "5 or more") {
            driver.findElement(By.cssSelector("label[data-e2e=\"5-link\"]")).click();
        } else if (selectRating == "4 or more") {
            driver.findElement(By.cssSelector("label[data-e2e=\"4 or more-link\"]")).click();
        } else if (selectRating == "3 or more") {
            driver.findElement(By.cssSelector("label[data-e2e=\"3 or more-link\"]")).click();
        } else if (selectRating == "2 or more") {
            driver.findElement(By.cssSelector("label[data-e2e=\"2 or more-link\"]")).click();
        } else if (selectRating == "1 or more") {
            driver.findElement(By.cssSelector("label[data-e2e=\"1 or more-link\"]")).click();
        }
        return selectRating;
    }
}
