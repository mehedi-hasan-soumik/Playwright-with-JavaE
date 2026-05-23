package Base;

import java.lang.reflect.Method;
import java.util.Properties;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import Utils.ConfigReader;
import Utils.ExtentManager;
import Utils.ScreenshotUtil;

public class BaseTest {

    protected static ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
    protected static ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
    protected static ThreadLocal<BrowserContext> tlContext = new ThreadLocal<>();
    protected static ThreadLocal<Page> tlPage = new ThreadLocal<>();

    protected Properties prop;
    protected ExtentReports extent;
    protected ExtentTest test;

    public Page getPage() {
        return tlPage.get();
    }

    @BeforeMethod
    public void setUp(Method method) {
        prop = ConfigReader.init_prop();

        extent = ExtentManager.getInstance();
        test = extent.createTest(method.getName());

        Playwright playwright = Playwright.create();
        tlPlaywright.set(playwright);

        String browserName = prop.getProperty("browser").trim();
        boolean isHeadless = Boolean.parseBoolean(prop.getProperty("headless"));
        int slowMo = Integer.parseInt(prop.getProperty("slowmo"));

        Browser browser;
        switch (browserName.toLowerCase()) {
            case "chromium":
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(isHeadless).setSlowMo(slowMo));
                break;
            case "firefox":
                browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(isHeadless).setSlowMo(slowMo));
                break;
            case "webkit":
                browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(isHeadless).setSlowMo(slowMo));
                break;
            default:
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(isHeadless));
                break;
        }
        tlBrowser.set(browser);

        // config.properties থেকে credentials রিড করা হচ্ছে
        String authUser = prop.getProperty("auth.username");
        String authPass = prop.getProperty("auth.password");

        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions();

        // যদি প্রোপার্টিজ ফাইলে ইউজারনেম ও পাসওয়ার্ড দেওয়া থাকে, তবেই কেবল এটি সেট হবে
        if (authUser != null && !authUser.trim().isEmpty() && authPass != null) {
            contextOptions.setHttpCredentials(new com.microsoft.playwright.options.HttpCredentials(authUser.trim(), authPass));
            test.info("Basic Authentication credentials configured for the browser context.");
        }

        // অপশনসহ কনটেক্সট তৈরি করা হলো
        BrowserContext context = browser.newContext(contextOptions);
        tlContext.set(context);


        Page page = context.newPage();
        tlPage.set(page);

        int timeout = Integer.parseInt(prop.getProperty("timeout"));
        page.setDefaultTimeout(timeout);

        String baseURL = prop.getProperty("url");
        page.navigate(baseURL);

        test.info("Browser (" + browserName + ") launched and navigated to: " + baseURL);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                test.fail(result.getThrowable());
                String base64Screenshot = ScreenshotUtil.takeScreenshot(getPage(), result.getName());
                test.fail("Failure Screenshot",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                test.pass("Test Passed Successfully");
            } else if (result.getStatus() == ITestResult.SKIP) {
                test.skip("Test Skipped");
            }
        } catch (Exception e) {
            System.err.println("Error during report generation: " + e.getMessage());
        }

        if (getPage() != null) getPage().close();
        if (tlContext.get() != null) tlContext.get().close();
        if (tlBrowser.get() != null) tlBrowser.get().close();
        if (tlPlaywright.get() != null) tlPlaywright.get().close();

        tlPage.remove();
        tlContext.remove();
        tlBrowser.remove();
        tlPlaywright.remove();

        extent.flush();
    }
}