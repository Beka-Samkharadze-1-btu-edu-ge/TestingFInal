package com.browserstack;

import com.browserstack.SeleniumTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.HashMap;
public class BStackDemoTest extends SeleniumTest {
    @Test
    public void addProductToCart() throws Exception {
        String username = System.getenv("BROWSERSTACK_USERNAME");
        String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
        String buildName = System.getenv("BROWSERSTACK_BUILD_NAME");
        String local = System.getenv("BROWSERSTACK_LOCAL");
        String localIdentifier = System.getenv("BROWSERSTACK_LOCAL_IDENTIFIER");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "Chrome");
        HashMap<String, Object> browserstackOptions = new HashMap<String, Object>();
        browserstackOptions.put("os", "Windows");
        browserstackOptions.put("osVersion", "10");
        browserstackOptions.put("sessionName", "BStack Build Name: " + buildName);
        browserstackOptions.put("local", local);
        browserstackOptions.put("localIdentifier", localIdentifier);
        browserstackOptions.put("seleniumVersion", "4.0.0");
        capabilities.setCapability("bstack:options", browserstackOptions);

        WebDriver driver = new RemoteWebDriver(new URL("https://" + username + ":" + accessKey + "@hub.browserstack.com/wd/hub"), capabilities);

        // navigate to bstackdemo
        driver.get("https://www.bstackdemo.com");

        // Check the title
        Assert.assertTrue(driver.getTitle().matches("StackDemo"));

        // Save the text of the product for later verify
        String productOnScreenText = driver.findElement(By.xpath("//*[@id=\"1\"]/p")).getText();
        // Click on add to cart button
        driver.findElement(By.xpath("//*[@id=\"1\"]/div[4]")).click();

        // See if the cart is opened or not
        Assert.assertTrue(driver.findElement(By.cssSelector(".float\\-cart__content")).isDisplayed());

        // Check the product inside the cart is same as of the main page
        String productOnCartText = driver.findElement(By.xpath("//*[@id=\"__next\"]/div/div/div[2]/div[2]/div[2]/div/div[3]/p[1]")).getText();
        Assert.assertEquals(productOnScreenText, productOnCartText);

        // Quit the driver after the test is complete
        driver.quit();
    }
}