package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.CategoryApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CategoryHttpExtension extends AbstractCategoryExtension {

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://127.0.0.1:8093/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final CategoryApiClient categoryApiClient = new CategoryApiClient("http://127.0.0.1:8093/", JacksonConverterFactory.create());

    @Override
    protected CategoryJson createCategory(Category category) {
        CategoryJson categoryJson = new CategoryJson(
                null,
                category.category(),
                category.username()
        );

        try {
            return categoryApiClient.addCategory(categoryJson);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    protected void removeCategory(CategoryJson category) {

    }
}
