package testPackage;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.selenium.AxeBuilder;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.checkerframework.checker.units.qual.A;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class TestCases {

    WebDriver driver;
    AxeBuilder axeBuilder;
    Results axeResults;

    @BeforeMethod
    public void setup() {

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        axeBuilder = new AxeBuilder();
        driver.get("https://dequeuniversity.com/demo/mars");

    }

    @Test(priority = 1)
    public void testDequeWebpage() {

        //Ensure the number of radio buttons under “Let the Adventure Begin” is 5
        List<WebElement> radioButtons = driver.findElements(By.xpath("//fieldset//div//input[@type='radio']"));
        int total = radioButtons.size();
        System.out.println(total);
        Assert.assertEquals(total, 5);

        //Ensure that clicking “add a traveler” adds another select to the page
        new WebDriverWait(driver, Duration.ofSeconds(3)).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='icon']//parent::a[@class='add-traveler']")));
        driver.findElement(By.xpath("//*[@class='icon']//parent::a[@class='add-traveler']")).click();
        List<WebElement> travellers = driver.findElements(By.xpath("//*[text()='Traveler']"));
        System.out.println(travellers);
        Assert.assertEquals(travellers.size(), 2);

        //Ensure that when clicking the arrows under the video, the heading changes
        driver.findElement(By.xpath("//*[@class='vid-arrows nextvid']//i[@class='vid-next icon-video-right-arrow']")).click();
        WebElement element = driver.findElement(By.xpath("//*[@class='interior-container']//h3[@id='video-text']"));
        String videoText = element.getText();
        System.out.println(videoText);
        Assert.assertEquals(videoText, "Why Mars died");

    }

    @Test(priority = 2)

//    Accessibility test case:
    public void testAxeCoreAccessibility() {

        try {
            axeResults = axeBuilder .analyze(driver);
            System.out.println(axeResults.getViolations());
            Assert.assertTrue(axeResults.violationFree());

        }catch (RuntimeException e){
            tearDown();
        }
    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}
