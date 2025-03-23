import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration; // Import Duration for Selenium 4.x
import java.util.List;

public class Lab1 {
    public static void main(String[] args) {
        // Set the path to your ChromeDriver executable
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Use Duration for timeout

        try {
            // 1. Open the website
            driver.get("https://demowebshop.tricentis.com/");

            // 2. Click on 'Gift Cards' in the left menu
            driver.findElement(By.linkText("Gift Cards")).click();

            // 3. Select a product with a price higher than 99
            List<WebElement> giftcardProducts = driver.findElements(By.xpath("//div[contains(@class, 'product-item')]"));
            for (WebElement product : giftcardProducts) {
                String priceText = product.findElement(By.xpath(".//span[contains(@class, 'price')]")).getText();
                double price = Double.parseDouble(priceText.replace("$", "").replace(",", ""));
                if (price > 99) {
                	product.findElement(By.xpath(".//h2[contains(@class, 'product-title')]//a")).click();
                    break;
                }
            }

            // 4. Fill in the fields 'Recipient's Name:' and 'Your Name:'
            driver.findElement(By.xpath("//input[starts-with(@id, 'giftcard_') and contains(@id, '_RecipientName')]")).sendKeys("Recipient Name");
            driver.findElement(By.xpath("//input[starts-with(@id, 'giftcard_') and contains(@id, '_SenderName')]")).sendKeys("Your Name");

            // 5. Enter '5000' in the 'Qty' text field
            WebElement qtyField = driver.findElement(By.xpath("//input[starts-with(@id, 'addtocart_') and contains(@id, '_EnteredQuantity')]"));
            qtyField.clear();
            qtyField.sendKeys("5000");

            // 6. Click the 'Add to cart' button
            driver.findElement(By.xpath("//input[starts-with(@id, 'add-to-cart-button-')]")).click();
            wait.until(ExpectedConditions.textToBePresentInElementLocated(
            	By.xpath("//div[contains(@class, 'bar-notification') and contains(@class, 'success')]"), "shopping cart")
            );

            // 7. Click the 'Add to wish list' button
            driver.findElement(By.xpath("//input[starts-with(@id, 'add-to-wishlist-button-')]")).click();
            wait.until(ExpectedConditions.textToBePresentInElementLocated(
            	By.xpath("//div[contains(@class, 'bar-notification') and contains(@class, 'success')]"), "wishlist")
            );

            // 8. Click on 'Jewelry' in the left menu
            driver.findElement(By.linkText("Jewelry")).click();

            // 9. Click the 'Create Your Own Jewelry' link
            driver.findElement(By.linkText("Create Your Own Jewelry")).click();

            // 10. Select the following values:'Material' - 'Silver 1mm', 'Length in cm' - '80', 'Pendant' - 'Star'
            driver.findElement(By.xpath("//select[contains(@id, 'product_attribute_71_9_15')]")).sendKeys("Silver (1 mm)");
            WebElement lengthField = driver.findElement(By.xpath("//input[@id='product_attribute_71_10_16']"));
            lengthField.clear();
            lengthField.sendKeys("80");
            driver.findElement(By.xpath("//input[@id='product_attribute_71_11_17_50']")).click();

            // 11. Enter '26' in the 'Qty' text field
            WebElement jewelryQtyField = driver.findElement(By.xpath("//input[starts-with(@id, 'addtocart_') and contains(@id, '_EnteredQuantity')]"));
            jewelryQtyField.clear();
            jewelryQtyField.sendKeys("26");

            // 12. Click the 'Add to cart' button
            driver.findElement(By.xpath("//input[starts-with(@id, 'add-to-cart-button-')]")).click();
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//div[contains(@class, 'bar-notification') and contains(@class, 'success')]"), "shopping cart"));

            // 13. Click the 'Add to wish list' button
            driver.findElement(By.xpath("//input[starts-with(@id, 'add-to-wishlist-button-')]")).click();
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[contains(@class, 'bar-notification') and contains(@class, 'success')]"), "wishlist"));
            // 14. Click the 'Wishlist' link at the top of the page
            driver.findElement(By.linkText("Wishlist")).click();

            // 15. Check the 'Add to cart' checkboxes for both products
            List<WebElement> addToCartCheckboxes = driver.findElements(By.xpath("//td[contains(@class, 'add-to-cart')]//input[@type='checkbox']"));
            for (WebElement checkbox : addToCartCheckboxes) {
                checkbox.click();
            }

            // 16. Click the 'Add to cart' button
            driver.findElement(By.name("addtocartbutton")).click();
            wait.until(ExpectedConditions.urlContains("/cart"));

            // 17. Verify that the 'Sub-Total' value is '1002600.00'
            String subTotal = driver.findElement(By.xpath("//*[contains(@class, 'cart-total-right')]")).getText();
            System.out.println("subTotal: " + subTotal);
            if (subTotal.equals("1002600.00")) {
                System.out.println("Test passed: Sub-Total is correct");
            } else {
                System.out.println("Test failed: Sub-Total is incorrect");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}