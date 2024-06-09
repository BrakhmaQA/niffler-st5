package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.io.IOException;

public class SpendApiClient extends ApiClient {

    private static final Config CFG = Config.getInstance();
    private SpendApi spendApi;
    private CategoryApi categoryApi;

    public SpendApiClient() {
        super(CFG.spendUrl());
        this.spendApi = retrofit.create(SpendApi.class);
    }

    public SpendJson createSpend(SpendJson spendJson) throws IOException {
        return spendApi.createSpend(spendJson)
                .execute()
                .body();
    }

    public CategoryJson addCategory(CategoryJson categoryJson) throws IOException {
        return categoryApi.createCategory(categoryJson)
                .execute()
                .body();
    }
}
