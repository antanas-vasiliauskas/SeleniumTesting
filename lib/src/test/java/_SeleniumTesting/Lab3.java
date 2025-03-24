package _SeleniumTesting;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import java.io.IOException;
import java.nio.file.*;
import java.time.Duration;

public class Lab3 {
    private static String email;
    private static String password;
    private WebDriver driver;
    private WebDriverWait wait;

    // ----------- User Creation Process: --------------------------
    @BeforeClass
    public static void createUser() {

        WebDriver registrationDriver = new ChromeDriver();
        WebDriverWait registrationWait = new WebDriverWait(registrationDriver, Duration.ofSeconds(20));
        
    	// 1. Open the website: https://demowebshop.tricentis.com/
        registrationDriver.get("https://demowebshop.tricentis.com/");
        
    	// 2. Click "Log in"
        registrationDriver.findElement(By.linkText("Log in")).click();
        
    	// 3. Click "Register" in the "New Customer" section
        registrationDriver.findElement(By.xpath("//input[@value='Register']")).click();
        
    	// 4. Fill in the registration form fields
        String timestamp = String.valueOf(System.currentTimeMillis());
        email = "user" + timestamp + "@test.com";
        password = "Password123!";
        registrationDriver.findElement(By.id("FirstName")).sendKeys("Test");
        registrationDriver.findElement(By.id("LastName")).sendKeys("User");
        registrationDriver.findElement(By.id("Email")).sendKeys(email);
        registrationDriver.findElement(By.id("Password")).sendKeys(password);
        registrationDriver.findElement(By.id("ConfirmPassword")).sendKeys(password);
        registrationDriver.findElement(By.id("register-button")).click();
        
        // 6. Click "Continue"
        registrationWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='Continue']"))).click();
        registrationDriver.quit();
    }
    // -------------------------------------------------------------------

    // ----------- Test Scenarios: --------------------------
    @Before
    public void setup() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        // 1. Open the website: https://demowebshop.tricentis.com/
        driver.get("https://demowebshop.tricentis.com/");
        
        // 2. Click "Log in"
        driver.findElement(By.linkText("Log in")).click();
        
        // 3. Fill in the "Email" and "Password" fields and click "Log in"
        driver.findElement(By.id("Email")).sendKeys(email);
        driver.findElement(By.id("Password")).sendKeys(password);
        driver.findElement(By.xpath("//input[@value='Log in']")).click();
    }
    
    @Test
    public void test1() throws IOException {
        processOrder("D:\\eclipse\\workspace\\SeleniumTesting\\lib\\src\\main\\java\\data1.txt");
    }

    @Test
    public void test2() throws IOException {
        processOrder("D:\\eclipse\\workspace\\SeleniumTesting\\lib\\src\\main\\java\\data2.txt");
    }



    private void processOrder(String filename) throws IOException {
    	// 4. In the sidebar menu, select "Digital downloads"
        driver.findElement(By.linkText("Digital downloads")).click();
        
    	// 5. Add products to the cart by reading from a text file:
        for (String product : Files.readAllLines(Paths.get(filename))) {
            addProductToCart(product);
        }

    	// 6. Open "Shopping cart"
        driver.findElement(By.linkText("Shopping cart")).click();
        
    	// 7. Check "I agree" and click "Checkout"
        driver.findElement(By.id("termsofservice")).click();
        driver.findElement(By.id("checkout")).click();

    	// 8. In "Billing Address", select an existing address or fill in a new one, then click "Continue"
        fillBillingAddress();
        
    	// 9. In "Payment Method", click "Continue"
        wait.until(ExpectedConditions.elementToBeClickable(
        	By.xpath("//input[@onclick='PaymentMethod.save()']")
        )).click();
        
    	// 10. In "Payment Information", click "Continue"
        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//input[@onclick='PaymentInfo.save()']")
        )).click();
            
    	// 11. In "Confirm Order", click "Confirm"
        wait.until(ExpectedConditions.elementToBeClickable(
        	By.xpath("//input[@onclick='ConfirmOrder.save()']")
        )).click();
        
    	// 12. Ensure the order is successfully placed.
        WebElement confirmation = wait.until(ExpectedConditions.visibilityOfElementLocated(
        	By.xpath("//div[@class='section order-completed']//strong")
        ));
        Assert.assertTrue(confirmation.getText().contains("successfully processed"));
    }
    
    private void addProductToCart(String productName) {
        By productLocator = By.xpath(
            "//h2[@class='product-title']/a[text()='" + productName + "']/../..//input[@value='Add to cart']"
        );
        wait.until(ExpectedConditions.elementToBeClickable(productLocator)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//p[@class='content' and contains(., 'shopping cart')]")
        ));
        System.out.println("Lab3 tests passed");
    }

    private void fillBillingAddress() {
    	if (!driver.findElements(By.id("billing-address-select")).isEmpty()) {
            // Case: Existing address is saved
            WebElement addressSelect = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("billing-address-select")
            ));
            Select addressDropdown = new Select(addressSelect);
            addressDropdown.selectByIndex(0); // Select the first saved address
        } else {
            // Case: No saved address, fill in new address form
            WebElement countryElement = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("BillingNewAddress_CountryId")
            ));
            Select country = new Select(countryElement);
            country.selectByVisibleText("Lithuania");

            driver.findElement(By.id("BillingNewAddress_City")).sendKeys("Vilnius");
            driver.findElement(By.id("BillingNewAddress_Address1")).sendKeys("Gedimino pr. 1");
            driver.findElement(By.id("BillingNewAddress_ZipPostalCode")).sendKeys("01105");
            driver.findElement(By.id("BillingNewAddress_PhoneNumber")).sendKeys("+37060000000");
        }
        driver.findElement(By.xpath("//input[@onclick='Billing.save()']")).click();
    }


    
    @After
    public void tearDown() {
        driver.quit();
    }
}