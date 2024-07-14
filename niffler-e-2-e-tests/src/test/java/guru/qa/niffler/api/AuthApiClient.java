package guru.qa.niffler.api;

import guru.qa.niffler.api.cookie.ThreadSafeCookieStore;
import guru.qa.niffler.api.interceptor.CodeInterceptor;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.oauth.TokenJson;
import guru.qa.niffler.utils.OAuthUtils;
import lombok.SneakyThrows;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AuthApiClient extends ApiClient {

    private final AuthApi authApi;

    public AuthApiClient() {
        super(CFG.authUrl(),
                true,
                JacksonConverterFactory.create(),
                HttpLoggingInterceptor.Level.BODY,
                new CodeInterceptor());

        this.authApi = retrofit.create(AuthApi.class);
    }

    @SneakyThrows
    public void doLogin(String username, String password) {
        String codeVerifier = OAuthUtils.generateCodeVerifier();
        String codeChallenge = OAuthUtils.generateCodeChallenge(codeVerifier);

        authApi.preRequest("code",
                        "client",
                        "openid",
                        CFG.frontUrl() + "/authorized",
                        codeChallenge,
                        "S256")
                .execute();

        authApi.login(ThreadSafeCookieStore.INSTANCE.getCookieValue("XSRF-TOKEN"),
                username,
                password);

        TokenJson response = authApi.token("Basic " + Base64.getEncoder().encodeToString("client:secret".getBytes(StandardCharsets.UTF_8)),
                        ApiLoginExtension.getCode(),
                        CFG.frontUrl() + "/authorized",
                        codeVerifier,
                        "authorization_code",
                        "client")
                .execute()
                .body();

        ApiLoginExtension.setToken(response.idToken());
    }
}
