package Utils;

import com.microsoft.playwright.Locator;

import java.nio.file.Paths;

public class UploadUtil {

    public static void uploadFile(Locator locator, String filePath) {

        locator.setInputFiles(Paths.get(filePath));
    }
}