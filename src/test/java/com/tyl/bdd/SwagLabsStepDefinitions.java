package com.tyl.bdd;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.Cucumber;
import org.junit.runner.RunWith;

public class SwagLabsStepDefinitions {

    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;
    private List priceList = new ArrayList<Double>();
    private List sortedPriceList = new ArrayList<Double>();

    @Before
    public void setUp() {
        Path currentRelativePath = Paths.get("");
        String absolutePath = Paths.get("").toAbsolutePath().toString();
        System.out.println("Driver path:" + absolutePath);
        System.setProperty("webdriver.chrome.driver", absolutePath + "/src/main/resources/chromedriver.exe");
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<String, Object>();
        driver.manage().window().maximize();//.setSize(new Dimension(764, 960));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Given("I visit the SwagLabs website")
    public void i_visit_the_swag_labs_website() {
        driver.get("https://www.saucedemo.com/");
        assertEquals("website not found", driver.getTitle(), "Swag Labs");
    }

    @When("I enter my user name {string} into the username field")
    public void i_enter_my_user_name_into_the_username_field(String userName) {
        driver.findElement(By.cssSelector("*[data-test=\"username\"]")).click();
        driver.findElement(By.cssSelector("*[data-test=\"username\"]")).sendKeys(userName);
        driver.findElement(By.cssSelector("form")).click();
    }

    @When("I enter my password {string} into the password field")
    public void i_enter_my_password_into_the_password_field(String password) {
        driver.findElement(By.cssSelector("*[data-test=\"password\"]")).click();
        driver.findElement(By.cssSelector("*[data-test=\"password\"]")).sendKeys(password);
        driver.findElement(By.cssSelector("form")).click();
    }

    @When("I click the login button")
    public void i_click_the_login_button() {
        driver.findElement(By.cssSelector("*[data-test=\"login-button\"]")).click();
    }

    @Then("I should successfully Login and see the inventory page")
    public void i_should_successfully_login_and_see_the_inventory_page() {
        // Write code here that turns the phrase above into concrete actions
        assertEquals(
                "could not find inventory page",
                driver.findElement(By.cssSelector(".title")).getText(),
                "PRODUCTS"
        );
    }

    @Given("I am on the inventory page as the user {string} with the password {string}")
    public void i_am_on_the_inventory_page_as_the_user_with_the_password(String userName, String Password) {
        this.i_visit_the_swag_labs_website();
        this.i_enter_my_user_name_into_the_username_field(userName);
        this.i_enter_my_password_into_the_password_field(Password);
        this.i_click_the_login_button();
        this.i_should_successfully_login_and_see_the_inventory_page();
    }

    @When("I sort items by high to low")
    public void i_sort_items_by_high_to_low() {
        // before I sort the items I need to organise the page by highest value to lowest
        // get current list and order them highest to lowest

        for (WebElement product : driver.findElements(By.className("inventory_item_price"))) {
            priceList.add(Double.parseDouble(product.getText().replace("$", "")));
        }
        //sort the price list high to low for later comparison and testing
        Collections.sort(priceList);
        Collections.sort(priceList, Collections.reverseOrder());

        // via web app sort product list highest to lowest
        Select dropDown = new Select(driver.findElement(By.className("product_sort_container")));
        dropDown.selectByValue("hilo");
        // get newly sorted product list

        for (WebElement product : driver.findElements(By.className("inventory_item_price"))) {
            sortedPriceList.add(Double.parseDouble(product.getText().replace("$", "")));
        }
    }

    @Then("The products should be reorganised by high to low")
    public void the_products_should_be_reorganised_by_high_to_low() {
        // compare product order with my own order list to make sure they are correct.
        assertEquals(priceList, sortedPriceList);
    }

    @When("I select the following products:")
    public void i_select_the_following_products(List<Map<String, String>> rows) {
        // based on what's in the data table pass select products for checkout
        for (Map<String, String> columns : rows) {
            columns.get("Product");
            for (WebElement inventoryItem : driver.findElements(By.className("inventory_item_description"))) {
                if (inventoryItem.findElement(By.className("inventory_item_name")).getText().equals(columns.get("Product"))) {
                    if (inventoryItem.findElement(By.className("btn")).getText().toLowerCase().equals("Add to cart".toLowerCase())) {
                        inventoryItem.findElement(By.className("btn")).click();
                    }
                }
            }
        }

        //check two items have been added to the basket
        assertEquals(Integer.parseInt(driver.findElement(By.className("shopping_cart_badge")).getText()), rows.size());

    }

    @When("I open the basket")
    public void i_open_the_basket() {
        // navigate and verify you are on the basket page
        driver.findElement(By.className("shopping_cart_badge")).click();
        assertEquals(driver.findElement(By.className("title")).getText(), "YOUR CART");
    }

    @Then("I should see my chosen products in the basket")
    public void i_should_see_my_chosen_products_in_the_basket(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            columns.get("Product");
            columns.get("cost");
            for (WebElement cartItem : driver.findElements(By.className("cart_list"))) {
                for (WebElement item : cartItem.findElements(By.className("cart_item"))) {
                    if (item.getText().equalsIgnoreCase(columns.get("Product").toLowerCase())) {
                        assertEquals(item.findElement(By.className("inventory_item_name")).getText().toLowerCase(), columns.get("Product").toLowerCase());
                        assertEquals(item.findElement(By.className("inventory_item_price")).getText(), "$".concat(columns.get("cost")));
                    }
                }
            }
        }
    }

    @When("click on the checkout button")
    public void click_on_the_checkout_button() {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.className("checkout_button")).click();
        assertEquals(driver.findElement(By.className("title")).getText(), "CHECKOUT: YOUR INFORMATION");
    }

    @When("enter my information:")
    public void enter_my_information(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            //firstname
            driver.findElement(By.id("first-name")).click();
            driver.findElement(By.id("first-name")).sendKeys(columns.get("FirstName"));
            //lastname
            driver.findElement(By.id("last-name")).click();
            driver.findElement(By.id("last-name")).sendKeys(columns.get("LastName"));
            //postcode
            driver.findElement(By.id("postal-code")).click();
            driver.findElement(By.id("postal-code")).sendKeys(columns.get("postcode"));
        }
    }

    @When("click the continue button")
    public void click_the_continue_button() {
        driver.findElement(By.id("continue")).click();
        assertEquals(driver.findElement(By.className("title")).getText(), "CHECKOUT: OVERVIEW");
    }

    @Then("I should see my chosen products in the overview page")
    public void i_should_see_my_chosen_products_in_the_overview_page(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            columns.get("Product");
            columns.get("cost");
            for (WebElement cartItem : driver.findElements(By.className("cart_list"))) {
                for (WebElement item : cartItem.findElements(By.className("cart_item"))) {
                    if (item.getText().equalsIgnoreCase(columns.get("Product").toLowerCase())) {
                        assertEquals(item.findElement(By.className("inventory_item_name")).getText().toLowerCase(), columns.get("Product").toLowerCase());
                        assertEquals(item.findElement(By.className("inventory_item_price")).getText(), "$".concat(columns.get("cost")));
                    }
                }
            }
        }
    }

    @Then("complete checkout by clicking the finish button")
    public void i_click_on_the_finish_button() {
        driver.findElement(By.id("finish")).click();
        assertEquals(driver.findElement(By.className("title")).getText(), "CHECKOUT: COMPLETE!");
        assertEquals(driver.findElement(By.className("complete-header")).getText(), "THANK YOU FOR YOUR ORDER");
    }

}
