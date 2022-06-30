package com.xff.basecore;

import com.xff.basecore.common.util.WebDriverHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class BaseCoreApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private WebDriverHelper webDriverHelper;

    @Test
    public void testWebDriver() {
        WebDriver driver = webDriverHelper.getWebDriver();
        System.out.println(driver.getPageSource());
        createPdf(driver);
    }

    private String createPdf(WebDriver driver) {
        String pdfUrl = "";
        InputStream in = null;
        PDDocument document = null;
        PDPageContentStream contentStream = null;
        try {
            driver.navigate().to("http://www.baidu.com");// 跳转页面
            String js = "";
            // js = MessageFormat.format(ADD_COOKIE_SCRIPT, token)
            ((JavascriptExecutor) driver).executeScript(js);// 添加cookie

            // 设置图片临时路径和pdf路径
            ApplicationHome applicationHome = new ApplicationHome(getClass());
            File jarF = applicationHome.getSource();
            final String outputPath = null == jarF ? applicationHome.toString() : jarF.getParentFile().toString();
            String picUrl = outputPath + File.separator + UUID.randomUUID().toString() + ".png";
            pdfUrl = outputPath + File.separator + UUID.randomUUID().toString() + ".pdf";

//        String old = driver.getWindowHandle();// 获取登录窗口的句柄
//        logger.info("====>获取登录句柄：{}", old);
//        logger.info("====》token：{}", driver.manage().getCookies());
//        ((JavascriptExecutor) driver).executeScript(MessageFormat.format(OPEN_TAB, path));// 打开网址
//
//        Set<String> tabs = driver.getWindowHandles();// 获取全部窗口所有句柄
//        tabs.remove(old);// 去除登录窗口的句柄
//        driver.switchTo().window(tabs.iterator().next());// 切换到新的窗口
//        logger.info("====》url地址信息：{}", driver.getCurrentUrl());

            //等待模板组件获取数据
            Thread.sleep(3000);
            Long width = (Long) ((JavascriptExecutor) driver).executeScript("return document.documentElement" +
                    ".scrollWidth");
            log.info("===>最大宽{}", width);
            Long height = (Long) ((JavascriptExecutor) driver).executeScript("return document.documentElement" +
                    ".scrollHeight");
            log.info("===>最大高{}", height);
            driver.manage().window().setSize(new Dimension(width.intValue(), height.intValue()));
            File screenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File picFile = new File(picUrl);
            FileUtils.copyFile(screenShot, picFile);
            document = new PDDocument();
            in = new FileInputStream(picUrl);
            BufferedImage bimg = ImageIO.read(in);
            float bimgWidth = bimg.getWidth();
            float bimgHeight = bimg.getHeight();
            PDPage page = new PDPage(new PDRectangle(bimgWidth, bimgHeight));
            document.addPage(page);

            PDImageXObject img = PDImageXObject.createFromFile(picUrl, document);
            contentStream = new PDPageContentStream(document, page);
            contentStream.drawImage(img, 0, 0);
            contentStream.close();
            document.save(pdfUrl);

            log.info("=====>pdf生成完成");
            // 删除图片
            if (picFile.delete()) {
                log.warn("====>图片删除完成");
            }
        } catch (Exception e) {
            log.error("生成pdf报告失败，错误信息：{}", e);
            return null;
        } finally {
            try {
                if (driver != null) {
                    driver.quit();
                }
                if (in != null) {
                    in.close();
                }
                if (document != null) {
                    document.close();
                }
                if (contentStream != null) {
                    contentStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return pdfUrl;
        }
    }

}
