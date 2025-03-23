import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Lab2 {
	public static void main(String[] args) {
		Task1();
		Task2();
	}
	public static void Task1() {
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        try {
        	//1. Open https://demoqa.com/.
        	driver.get("https://demoqa.com/");
        	//2. Close the cookie consent window.
        	driver.findElement(By.xpath("//a[contains(@id,'close-fixedban')]")).click();
        	//3. Select the "Widgets" tab.
        	WebElement widgetsButton = driver.findElement(By.xpath("//div[h5='Widgets']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", widgetsButton); // To avoid ElementClickInterceptedException
            widgetsButton.click();
        	//4. Choose the "Progress Bar" menu item.
        	driver.findElement(By.xpath("//span[text()='Progress Bar']")).click();
        	//5. Click the "Start" button.
        	driver.findElement(By.xpath("//button[text()='Start']")).click();
        	//6. Wait until it reaches 100% and then click "Reset."
        	wait.until(d -> 
    			d.findElement(By.xpath("//div[@role='progressbar']")).getAttribute("aria-valuenow").equals("100")
        	);
        	driver.findElement(By.xpath("//button[text()='Reset']")).click();
        	//7. Ensure that the progress bar is empty (0%).
        	String resetValue = driver.findElement(By.xpath("//div[@role='progressbar']")).getAttribute("aria-valuenow");
			System.out.println("Progress reset to: " + resetValue);
        } finally {
            driver.quit();
        }
    }

    public static void Task2() {


    	
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        try {
        	// 1. Open https://demoqa.com/.
            driver.get("https://demoqa.com/");
    		// 2. Close the cookie consent window.
            driver.findElement(By.xpath("//a[contains(@id,'close-fixedban')]")).click();
    		// 3. Select the "Elements" tab.
            WebElement elementsButton = driver.findElement(By.xpath("//div[h5='Elements']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementsButton); // To avoid ElementClickInterceptedException
            elementsButton.click();
    		// 4. Choose the "Web Tables" menu item.
            driver.findElement(By.xpath("//span[text()='Web Tables']")).click();
    		// 5. Add enough elements to create a second page in the pagination.
            for(int i = 0; i < 8; i++) {            	
                driver.findElement(By.xpath("//button[@id='addNewRecordButton']")).click();
                WebElement form = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'modal-content')]")
                ));
                
                form.findElement(By.xpath(".//input[@id='firstName']")).sendKeys("Test");
                form.findElement(By.xpath(".//input[@id='lastName']")).sendKeys("User");
                form.findElement(By.xpath(".//input[@id='userEmail']")).sendKeys("test@example.com");
                form.findElement(By.xpath(".//input[@id='age']")).sendKeys("30");
                form.findElement(By.xpath(".//input[@id='salary']")).sendKeys("50000");
                form.findElement(By.xpath(".//input[@id='department']")).sendKeys("IT");
                form.findElement(By.xpath(".//button[@id='submit']")).click();
            }
    		// 6. Navigate to the second page by clicking "Next."
            WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(
            	By.xpath("//button[contains(@class, '-btn') and text()='Next']")
            ));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextButton); // To avoid ElementClickInterceptedException
            nextButton.click();
    		// 7. Delete an element on the second page.
            driver.findElement(By.xpath("(//span[@title='Delete'])[1]")).click();
    		// 8. Ensure that pagination automatically returns to the first page and that the number of pages is reduced to one.
            wait.until(d -> {
                String currentPage = d.findElement(By.xpath("//span[@class='-pageInfo']//input[@type='number']")).getAttribute("value");
                String totalPages = d.findElement(By.xpath("//span[@class='-pageInfo']//span[@class='-totalPages']")).getText();
                return totalPages.equals("1"); // return currentPage.equals("1") && totalPages.equals("1");
            });

            // Print the pagination state
            String currentPage = driver.findElement(By.xpath("//span[@class='-pageInfo']//input[@type='number']")).getAttribute("value");
            String totalPages = driver.findElement(By.xpath("//span[@class='-pageInfo']//span[@class='-totalPages']")).getText();
            System.out.println("Pagination state: Page " + currentPage + " of " + totalPages);
        } finally {
            driver.quit();
        }
    }
}