package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.JavascriptExecutor;

import static com.codeborne.selenide.Selenide.webdriver;

public abstract class BaseComponent<T extends BaseComponent<?>> {
    protected final SelenideElement self;
    protected final JavascriptExecutor jsExecutor = (JavascriptExecutor) webdriver();

    protected BaseComponent(SelenideElement self) {
        this.self = self;
    }
}
