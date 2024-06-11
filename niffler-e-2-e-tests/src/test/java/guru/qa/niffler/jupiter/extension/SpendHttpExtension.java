package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.ApiClient;
import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.model.SpendJson;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Date;

public class SpendHttpExtension extends AbstractSpendExtension {
    private final SpendApiClient spendApiClient = new SpendApiClient("http://127.0.0.1:8093/", JacksonConverterFactory.create());

    @Override
    protected SpendJson createSpend(Spend spend) {
        SpendJson spendJson = new SpendJson(
                null,
                new Date(),
                spend.category(),
                spend.currency(),
                spend.amount(),
                spend.description(),
                spend.username()
        );

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
