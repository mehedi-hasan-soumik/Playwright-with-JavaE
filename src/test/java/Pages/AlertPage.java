package Pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class AlertPage {
    private Page page;
    private final Locator jsPromptBtn;
    private final Locator result;

    public AlertPage(Page page){
        this.page = page;
        this.jsPromptBtn = page.locator("//button[@onclick='jsPrompt()']");
        this.result = page.locator("#result");
    }

    public Locator verifyAlertPage(){
        return jsPromptBtn;
    }

    public void clickJsPromptBtn(){
        jsPromptBtn.click();
    }

    public Locator verifyResult(){
        return result;
    }
}
