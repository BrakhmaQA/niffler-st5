package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public abstract class SpendExtension implements BeforeEachCallback, AfterEachCallback {
    protected abstract SpendJson createSpend(SpendJson spend);

    protected abstract void removeSpend(SpendJson spend);

    @Override
    public void afterEach(ExtensionContext context) {

    }

    @Override
    public void beforeEach(ExtensionContext context) {

    }
}
