package me.masonic.datamining.Backup;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Mason Project
 * 2017-7-8-0008
 */
public class Selenium {


    public static void main(String[] args) throws Exception {

        System.setProperty("webdriver.chrome.driver", "E:\\IdeaProjects\\MasonicSpider\\lib\\ChromeDriver\\chromedriver.exe");

        WebDriver dr = new ChromeDriver();

        //设置webdriver.chrome.driver属性
        //声明chromeoptions,主要是给chrome设置参数
        ChromeOptions options = new ChromeOptions();
        //设置user agent为iphone5
        options.addArguments("--user-agent=Apple Iphone 5");
        //实例化chrome对象，并加入选项
        WebDriver driver = new ChromeDriver(options);
        //打开百度
        testBrowser(dr);

    }

    public static void testBrowser(WebDriver driver) throws Exception
    {
        driver.get("http://www.iqiyi.com/a_19rrhau8s1.html");
        Thread.sleep(5000);
        // 浏览器最大化
        WebElement voteup = driver.findElement(By.cssSelector("#widget-voteup"));
        WebElement tv = driver.findElement(By.className("fenshu-r"));
        System.out.println(voteup.getText());
        System.out.println(tv.getText());
    }

}
