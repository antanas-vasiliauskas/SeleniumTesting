package _SeleniumTesting;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import java.nio.file.*;
import java.time.Duration;
import java.util.*;

public class Lab4 {
    private static WebDriver driver;
    private static WebDriverWait wait;

    @Before
    public void setupTest() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Test
    public void testCompareListLimit() {
    	try {
			// 1. Open the website: https://demowebshop.tricentis.com/
	    	driver.get("https://demowebshop.tricentis.com/");
	    	// 2. Read 5 book names from books.txt ("Computing and Internet", "Copy of Computing and Internet EX", "Fiction", "Fiction EX", "Health Book")
	    	List<String> booksToAdd = Files.readAllLines(Paths.get("D:\\eclipse\\workspace\\SeleniumTesting\\lib\\src\\main\\java\\books.txt"));
	    	// 3. For each book:
	        for (String book : booksToAdd) {
	        	// - Click "Books" in the navigation toolbar
	            driver.findElement(By.linkText("Books")).click();

	        	// - Locate book from the list and click on it
	            WebElement bookItem = driver.findElement(By.xpath("//h2[@class='product-title']/a[text()='" + book + "']/../.."));
	            bookItem.findElement(By.className("product-title")).click();
	            
	            //   - Click "Add to compare list"
	            driver.findElement(By.xpath("//input[@value='Add to compare list']")).click();
	            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("compare-products-page")));
	        }
	        
	        // 4. After all 5 books added, verify:
	    	// - The compare list contains 4 items of the 4 books that were added.
	    	// - The first book that was added is not present in the list.
	        List<WebElement> compareItems = driver.findElements(
	        	By.xpath("//tr[@class='product-name']//td[@class='a-center']/a")
	        );
	        List<String> compareList = new ArrayList<>();
	        for (WebElement item : compareItems) {
	            compareList.add(item.getText().trim());
	        }
	        // compare list contains 4 books
	        Assert.assertEquals("Should have exactly 4 items", 4, compareList.size());
	        
	        // last four books present
	        for (int i = 1; i < booksToAdd.size(); i++) {
	        	Assert.assertTrue("Book " + booksToAdd.get(i) + " missing", compareList.contains(booksToAdd.get(i)));
	        }
	        // first book not present
	        Assert.assertFalse("First book should not be present", compareList.contains(booksToAdd.get(0)));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @After
    public void tearDown() {
    	driver.quit();
    }
}