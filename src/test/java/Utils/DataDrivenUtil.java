package Utils;

import org.testng.annotations.DataProvider;

public class DataDrivenUtil {

    @DataProvider(name = "Auth Data")
    public Object[][] data(){
        return new Object[][]{
                {"mehedi", 1234},
                {"hasan", 123}
        };
    }
}
