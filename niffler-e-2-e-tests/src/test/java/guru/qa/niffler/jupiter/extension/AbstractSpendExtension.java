package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.annotation.Spends;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public abstract class AbstractSpendExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(AbstractSpendExtension.class);


    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        List<Spend> potentialSpend = new ArrayList<>();

        AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                Spends.class
        ).ifPresent(spends -> potentialSpend.addAll(Arrays.stream(spends.value()).toList()));

        AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                Spend.class
        ).ifPresent(potentialSpend::add);

        if (!potentialSpend.isEmpty()) {
            List<SpendJson> spendJsonList = new ArrayList<>();
            for (Spend spend : potentialSpend) {
                SpendJson spendJson = new SpendJson(
                        null,
                        new Date(),
                        spend.category(),
                        spend.currency(),
                        spend.amount(),
                        spend.description(),
                        spend.username()
                );
                spendJsonList.add(createSpend(spendJson));
            }
            extensionContext.getStore(NAMESPACE)
                    .put(extensionContext.getUniqueId(), spendJsonList);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {
        List<SpendJson> spendJsons = context.getStore(NAMESPACE).get(context.getUniqueId(), List.class);
        for (SpendJson spendJson : spendJsons) {
            removeSpend(spendJson);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        Class<?> type = parameterContext
                .getParameter()
                .getType();

        return type.isAssignableFrom(SpendJson.class) || type.isAssignableFrom(SpendJson[].class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        Class<?> type = parameterContext
                .getParameter()
                .getType();

        List<SpendJson> createdSpends = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), List.class);

        return type.isAssignableFrom(SpendJson.class) ? createdSpends.getFirst() : createdSpends.toArray(SpendJson[]::new);
    }

    protected abstract SpendJson createSpend(SpendJson spendJson);

    protected abstract void removeSpend(SpendJson spend);
}
