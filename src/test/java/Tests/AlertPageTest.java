package Tests;

import Base.BaseTest;
import Pages.AlertPage;
import Pages.HomePage;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;



public class AlertPageTest extends BaseTest {


    @Test
    public void TestAlert(){
        HomePage homePage = new HomePage(getPage());
        AlertPage alertPage = new AlertPage(getPage());
        assertThat(homePage.verifyPage()).isVisible();
        homePage.clickJsAlertLink();
        assertThat(alertPage.verifyAlertPage()).isVisible();
        getPage().onDialog(dialog -> dialog.accept("Hello"));
        alertPage.clickJsPromptBtn();
        assertThat(alertPage.verifyResult()).containsText("You entered: Hello");
    }
}
