package com.xff.basecore.common.util;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * WebDriverHelper.
 *
 * @author wang_fei
 * @since 2022/6/30 10:09
 */
@Component
@Slf4j
public class WebDriverHelper {

    /**
     * 从配置获取浏览器，默认使用chrome
     */
    @Value("${browser.name:chrome}")
    private String BROWSER;

    /**
     * 获取浏览器驱动对象
     */
    public WebDriver getWebDriver() {
        if (BROWSER.equalsIgnoreCase("firefox")) {
            return getFireFoxDriver();
        }
        return getChromebDriver();
    }

    /**
     * arm火狐浏览器
     */
    private WebDriver getFireFoxDriver() {
        //本地调式用下载地址http://npm.taobao.org/mirrors/geckodriver/
        // System.setProperty("webdriver.gecko.driver","D:\\myworkSpace\\webdriver\\geckodriver.exe");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("--headless");
//        firefoxOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36
//        (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36");
        firefoxOptions.addArguments("--disable-gpu");

        firefoxOptions.addArguments("--window-size=1928,1080");
        firefoxOptions.addArguments("--disable-blink-features=AutomationControlled");
        firefoxOptions.addArguments("--start-maximized");
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("general.useragent.override", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36");
        firefoxOptions.setProfile(profile);

        log.info("开始获取浏览器");

        return new FirefoxDriver(firefoxOptions);
    }

    /**
     * x86谷歌浏览器
     */
    private WebDriver getChromebDriver() {
        //本地调试的时候下载本地浏览器对应的driver: 下载地址http://chromedriver.storage.googleapis.com/index.html
        System.setProperty("webdriver.chrome.driver", "D:\\code\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--profile-directory=" + UUID.randomUUID());
        chromeOptions.setHeadless(true);

        // 基础参数设置
        chromeOptions.addArguments("--silent");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--ignore-certificate-errors");
        chromeOptions.addArguments("--allow-running-insecure-content");

        // 优化参数设置
        chromeOptions.addArguments("--incognito");
        chromeOptions.addArguments("--disable-images");
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--disable-plugins");
        chromeOptions.addArguments("--disable-infobars");
        chromeOptions.addArguments("--lang=zh_CN.UTF-8");
        chromeOptions.addArguments("--disable-javascript");
        chromeOptions.addArguments("--window-size=1928,1080");

        chromeOptions.addArguments("--test-type", "--ignore-certificate-errors");
        // 浏览器设置
        chromeOptions.addArguments("user-agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko)" +
                " Chrome/87.0.4280.88 Safari/537.36");
        return new ChromeDriver(chromeOptions);
    }

}
