package Utils;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.options.Cookie;

import java.util.List;
import java.util.Map;

public class CookieUtil {

    public static void addCookie(BrowserContext context, String url, String name, String value) {

        Cookie cookie = new Cookie(name, value)
                .setUrl(url);

        context.addCookies(List.of(cookie));
    }

    public static List<Cookie> getCookies(BrowserContext context) {
        return context.cookies();
    }

    public static void clearCookies(BrowserContext context) {
        context.clearCookies();
    }
}

