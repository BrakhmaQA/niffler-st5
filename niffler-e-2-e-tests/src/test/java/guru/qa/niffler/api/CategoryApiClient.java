package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;

public class CategoryApiClient extends ApiClient {

    private final CategoryApi categoryApi;

    public CategoryApiClient() {
        super(CFG.spendUrl());
        this.categoryApi = retrofit.create(CategoryApi.class);
    }

    public CategoryJson addCategory(CategoryJson category) throws Exception {
        return categoryApi.createCategory(category)
                .execute()
                .body();
    }
}
