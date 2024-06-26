package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selenide.$;


public class AuthorizationPage extends BasePage<AuthorizationPage> {
    public static final String URL = CFG.frontUrl();
    private final SelenideElement welcomeText = $(".main__header");
    private final SelenideElement logInButton = $(byTagAndText("a", "Login"));
    private final SelenideElement usernameField = $("input[name='username']");
    private final SelenideElement passwordField = $("input[name='password']");
    private final SelenideElement sigInButton = $(".form__submit");

    @Step("Click LogIn")
    public AuthorizationPage clickLogInButton() {
        logInButton.click();
        return this;
    }

    @Step("Fill in Username field")
    public AuthorizationPage setUsername(String username) {
        usernameField.sendKeys(username);
        return this;
    }


    @Step("Fill in Password field")
    public AuthorizationPage setPassword(String password) {
        passwordField.sendKeys(password);
        return this;
    }

    @Step("Click SignIn")
    public void clickSignInButton() {
        sigInButton.click();
    }

    @Override
    public AuthorizationPage checkPageLoaded() {
        welcomeText.should(text("Welcome to magic journey with Niffler. The coin keeper"));

        return this;
    }
}
