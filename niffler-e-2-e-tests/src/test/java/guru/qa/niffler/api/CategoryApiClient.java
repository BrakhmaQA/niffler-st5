package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import retrofit2.Converter;

import java.io.IOException;

public class CategoryApiClient extends ApiClient {

    private static final Config CFG = Config.getInstance();

    private final CategoryApi categoryApi;

    public CategoryApiClient() {
        super(CFG.spendUrl());
        this.categoryApi = retrofit.create(CategoryApi.class);
    }

    public CategoryApiClient(String baseUrl, Converter.Factory converter) {
        super(baseUrl, converter);
        this.categoryApi = retrofit.create(CategoryApi.class);
    }

    public CategoryJson addCategory(CategoryJson category) throws IOException {
        return categoryApi.createCategory(category)
                .execute()
                .body();
    }
}
