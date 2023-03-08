import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrderingTest {
/*
1.	On product details page select "Add to cart" for product "Atomic Habits".
2.	Check Order Subtotal and Order Total is $99.85
3.  Select "Basket/Checkout" in cart pop-up
4.	Click "Checkout" button after redirect to cart page
5.	Set "test@user.com" as e-mail address
6.	After being redirected to multicheckout delivery address page check the following final review:
 Subtotal 21,12 €, Total 21,12 € and Tax 0,00 €

*
* */
static WebDriver driver;
    @BeforeClass
    public static void setup(){
        driver = new ChromeDriver();
    }


    @Test
    public void checkPricesBasket(){
        driver.get("https://www.bookdepository.com/");
        WebElement bookEl = waitForElementLocatedBy(driver, By.xpath("//div[@class='item-info']//a[contains(text(), ' Atomic Habits')]"));
        bookEl.click();

        String bookPrice = waitForElementLocatedBy(driver, By.cssSelector("div.price-info-wrap span.sale-price")).getText();

        WebElement addToBasketButton = waitForElementLocatedBy(driver, By.cssSelector("div.checkout-tools a.btn.btn-primary.add-to-basket"));
        addToBasketButton.click();

        String totalPrice = waitForElementLocatedBy(driver, By.cssSelector("b.pink-text.big.total")).getText();
        Assert.assertTrue("Order Subtotal and Order Total are not equal!", bookPrice.equals(totalPrice));
    }

    @Test
    public void checkPricesCheckout(){
        WebElement basketCheckoutButton = waitForElementLocatedBy(driver, By.linkText("Basket / Checkout"));
        basketCheckoutButton.click();

        WebElement checkoutButton = waitForElementLocatedBy(driver, By.linkText("Checkout"));
        checkoutButton.click();

        WebElement emailField = waitForElementLocatedBy(driver, By.name("emailAddress"));
        emailField.sendKeys("test@user.com");

        String checkoutSubtotal = waitForElementLocatedBy(driver, By.xpath("//div[contains(@aria-label, 'Sub-total')]//dd")).getText();
        String checkoutTotal = waitForElementLocatedBy(driver,By.xpath("//div[contains(@aria-label, 'Total')]//dd")).getText();
        String checkoutVAT = waitForElementLocatedBy(driver,By.xpath("//div[contains(@aria-label, 'VAT')]//dd")).getText();

        Assert.assertTrue("Checkout subtotal is incorrect!", checkoutSubtotal.equals("21,12 €"));
        Assert.assertTrue("Checkout total is incorrect!", checkoutTotal.equals("21,12 €"));
        Assert.assertTrue("Checkout VAT is incorrect!", checkoutVAT.equals("0,00 €"));

    }
    @AfterClass
    public static void tearDown(){
        driver.quit();
        driver = null;
    }


    private static WebElement waitForElementLocatedBy(WebDriver driver, By by) {
        WebElement element =  new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(by));
        return element;
    }


}
