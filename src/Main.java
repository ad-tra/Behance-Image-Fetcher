

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class Main {

private static Actions actions;
private static WebDriver driver;
private static int boardCount;


public static void main (String[] args) throws InterruptedException
{
	saveBoards("//put your file path here","https://www.behance.net/kiuchi");
}




public static void setWebDrivers(String profileURL) {
	System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir") + "\\webdrivers\\chromedriver.exe");
	driver = new ChromeDriver();
	actions = new Actions(driver);
	driver.get(profileURL);
	
	
}

public static void saveBoards(String filePath, String profileURL) throws InterruptedException
{
	setWebDrivers(profileURL);
	Thread.sleep(5000);
	
	WebElement imgThumb = driver.findElement(By.xpath("//div[2]/div/div/div[2]/a"));
	
 	boardCount= 2;
 	int counter= 0;	 
 	while (counter<4)
	{
 		try {
 			 //opens the image board in new window 
			 imgThumb = driver.findElement(By.xpath("//div["+ boardCount + "]/div/div/div[2]/a"));
			 Thread.sleep(500);
			 actions.keyDown(Keys.CONTROL).click(imgThumb).perform(); 
			 
			 //switches the driver to the newly opened window  
			 ArrayList<String> tabs = new ArrayList <String>(driver.getWindowHandles()); 
		     driver.switchTo().window(tabs.get(1));
		     
		     saveImages(filePath);
		     
		     //switch back to the main profile window
		     driver.close();
		     driver.switchTo().window(tabs.get(0));
		     boardCount++; counter =0;

		 	}
			
			catch(Exception e)
			{

				JavascriptExecutor js = (JavascriptExecutor)driver;
				js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
				System.out.println("Scrolled to infinty and beyond, counter is: " + counter);
				counter++;
				Thread.sleep(1000);
			}	  
			
		 }
}


public static void saveImages(String filePath)
{
	int imgCounter = 1;
	
	while(imgCounter>0){
	try {
		
		WebElement imageWebElement = driver.findElement(By.xpath("(//img[contains(@src, 'project_modules')])["+imgCounter+"]"));
		Thread.sleep(500);
		URL url = new URL(imageWebElement.getAttribute("src"));
		BufferedImage image = ImageIO.read(url);
		ImageIO.write(image, "jpg", new File(filePath+ "\\b" + (boardCount-1)+ "_"+ (imgCounter-1) +".jpg") );
		System.out.println("downloaded image" + (boardCount-1)+ "_" + (imgCounter-1));
		imgCounter++;
		
	}
	catch(Exception e)
		{
		System.out.println("finshed download of page" + (boardCount-1) );
		imgCounter=0;
		}
	}
 }


}
