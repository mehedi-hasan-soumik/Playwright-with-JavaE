package Utils;

import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import com.microsoft.playwright.Page;

public class ScreenshotUtil {

    public static String takeScreenshot(Page page, String testName) {

        String dirPath = "test-output/screenshots/";
        new File(dirPath).mkdirs();

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String path = dirPath + testName + "_" + timestamp + ".png";

        byte[] screenshotBytes = page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get(path))
                .setFullPage(false));

        return Base64.getEncoder().encodeToString(screenshotBytes);
    }
}