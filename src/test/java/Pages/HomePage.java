package Pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HomePage {
    private Page page;
    private final Locator jsAlertLink;

    public HomePage(Page page){
        this.page = page;
        this.jsAlertLink = page.locator("//a[@href='/javascript_alerts']");
    }


    public Locator verifyPage(){
        return jsAlertLink;
    }

    public void clickJsAlertLink(){
        jsAlertLink.click();
    }
}
