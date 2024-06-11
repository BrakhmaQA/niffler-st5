package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Converter;

import java.io.IOException;

public class SpendApiClient extends ApiClient {

    private static final Config CFG = Config.getInstance();
    private SpendApi spendApi;

    public SpendApiClient() {
        super(CFG.spendUrl());
        this.spendApi = retrofit.create(SpendApi.class);
    }

    public SpendApiClient(String baseUrl, Converter.Factory converter) {
        super(baseUrl, converter);
        this.spendApi = retrofit.create(SpendApi.class);
    }

    public SpendJson createSpend(SpendJson spendJson) throws IOException {
        return spendApi.createSpend(spendJson)
                .execute()
                .body();
    }
}
