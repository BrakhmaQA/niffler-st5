package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.model.SpendJson;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

public class SpendHttpExtension extends AbstractSpendExtension {
    private final SpendApiClient spendApiClient = new SpendApiClient("http://127.0.0.1:8093/", JacksonConverterFactory.create());

    @Override
    protected SpendJson createSpend(SpendJson spendJson) {
        try {
            return spendApiClient.createSpend(spendJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void removeSpend(SpendJson spend) {

    }
}
