package com.mavenit.argos.pages;

import com.mavenit.argos.driver.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.List;

public class HomePage extends DriverFactory {
    ResultsPage resultsPage = new ResultsPage();
    public void doSearch(String searchTerm) throws InterruptedException {
        driver.findElement(By.cssSelector("input[data-test='search-input']")).sendKeys(searchTerm);
        driver.findElement(By.cssSelector("button[type='submit']")).sendKeys(Keys.ENTER);
        Thread.sleep(4000);
        if (driver.findElements(By.cssSelector("a[data-test='component-product-card-title']")).size() == 0)
        {
            throw new RuntimeException("Sorry, we couldn't find any products with the term " + searchTerm);
        }
    }
    public String getCurrentUrl() {
        return driver.getCurrentUrl();

    }
}
