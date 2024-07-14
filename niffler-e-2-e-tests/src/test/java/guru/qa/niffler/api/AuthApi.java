package guru.qa.niffler.api;

import guru.qa.niffler.model.oauth.TokenJson;
import retrofit2.Call;
import retrofit2.http.*;

public interface AuthApi {

    // Исходное состояние:
    // codeChallenge, codeVerifier

    // Первый запрос
    // GET http://127.0.0.1:9000/oauth2/authorize
    // ?response_type=code&
    // &client_id=client&
    // &scope=openid&
    // &redirect_uri=http://127.0.0.1:3000/authorized
    // &code_challenge=gJiudpoWRq5YtG6h-c0Rkl8tiV208h6cKWR1SKQx-nA
    // &code_challenge_method=S256

    @GET("/oauth2/authorize")
    Call<Void> preRequest(@Query("response_type") String responseType,
                          @Query("client_id") String clientId,
                          @Query("scope") String scope,
                          @Query(value = "redirect_uri", encoded = true) String redirectUri,
                          @Query("code_challenge") String codeChallenge,
                          @Query("code_challenge_method") String codeChallengeMethod);

    // Response
    // 302 Found -> http://127.0.0.1:9000/login
    // Set-Cookie: JSESSIONID=AD94ECBA2E1C991C53324FDBB6E7E477
    // Auto Redirect -> GET http://127.0.0.1:9000/login
    // 200 OK
    // Set-Cookie: XSRF-TOKEN=d8b75669-f999-4d7e-ab19-7787e8f32845;

    // Второй запрос
    // POST FUE http://127.0.0.1:9000/login
    // Cookies: XSRF-TOKEN=d8b75669-f999-4d7e-ab19-7787e8f32845 , JSESSIONID=AD94ECBA2E1C991C53324FDBB6E7E477
    // URL ENCODED FIELD: _csrf: d8b75669-f999-4d7e-ab19-7787e8f32845
    // URL ENCODED FIELD: username: duck
    // URL ENCODED FIELD: password: 12345

    @POST("/login")
    @FormUrlEncoded
    Call<Void> login(@Field("_csrf") String csrf,
                     @Field("username") String username,
                     @Field("v") String password);

    // Response
    // 302 Found -> http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=http://127.0.0.1:3000/authorized&code_challenge=gJiudpoWRq5YtG6h-c0Rkl8tiV208h6cKWR1SKQx-nA&code_challenge_method=S256&continue
    // Set-Cookie: JSESSIONID=B1DF37DB7A4F32FA7649AF4A097681E4
    // Set-Cookie: XSRF-TOKEN=
    // Auto Redirect -> GET http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=http://127.0.0.1:3000/authorized&code_challenge=gJiudpoWRq5YtG6h-c0Rkl8tiV208h6cKWR1SKQx-nA&code_challenge_method=S256&continue
    // 302 Found -> http://127.0.0.1:3000/authorized?code=9qjFh9EQITqjnZAY08FMtRsQYAe-m9NSajrswRML5RE2DJSjHOwHnHLsidNxLBUXFpAz_1nmd_g9g5DMKRHTP3zjXWq8m2h_3MfdgG1bGVdorVJBDGyPb8xP_atpJGiD
    // Auto Redirect -> GET http://127.0.0.1:3000/authorized?code=9qjFh9EQITqjnZAY08FMtRsQYAe-m9NSajrswRML5RE2DJSjHOwHnHLsidNxLBUXFpAz_1nmd_g9g5DMKRHTP3zjXWq8m2h_3MfdgG1bGVdorVJBDGyPb8xP_atpJGiD
    // 200 OK

    // Третий запрос
    // POST -> http://127.0.0.1:9000/oauth2/token
    // URL ENCODED FIELD: code: 9qjFh9EQITqjnZAY08FMtRsQYAe-m9NSajrswRML5RE2DJSjHOwHnHLsidNxLBUXFpAz_1nmd_g9g5DMKRHTP3zjXWq8m2h_3MfdgG1bGVdorVJBDGyPb8xP_atpJGiD
    // URL ENCODED FIELD: redirect_uri: http://127.0.0.1:3000/authorized
    // URL ENCODED FIELD: code_verifier: vgxpESqPi9n-DbuMSAcc2pvnKUJxGm4_WdrQlGHnYpU
    // URL ENCODED FIELD: grant_type: authorization_code
    // URL ENCODED FIELD: client_id=client
    // Header: Authorization: Basic Y2xpZW50OnNlY3JldA==

    @POST("/oauth2/token")
    @FormUrlEncoded
    Call<TokenJson> token(@Header("Authorization") String basicAuthorizationHeader,
                          @Field("code") String code,
                          @Field(value = "redirect_uri", encoded = true) String redirectUri,
                          @Field("code_verifier") String codeVerifier,
                          @Field("grant_type") String grantType,
                          @Field("client_id") String clientId);

    // Response
    // 200 OK
    //  {
    //      "access_token": "eyJraWQiOiI0NTFjYzVhOS1jNTdhLTRhM2EtOWZmNC1jMmVjZjY5Yzg3YTkiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJkdWNrIiwiYXVkIjoiY2xpZW50IiwibmJmIjoxNzA4MzY0MDI4LCJzY29wZSI6WyJvcGVuaWQiXSwiaXNzIjoiaHR0cDovLzEyNy4wLjAuMTo5MDAwIiwiZXhwIjoxNzA4MzY3NjI4LCJpYXQiOjE3MDgzNjQwMjh9.obuCCaRcAzGJaQ3HklXh5lRaSRzpAtSVcPJKxre4fOWe4_pM5lk8AWhO8JSdYdEe-vIbtaw_jlyIzHqPcoNeRYR4yNJ85bx05k4bs2q9MAYNsTIk2_qXq9Q-cF99XvcOYT0JZpGRYj-YvaeD3DdEooSuCYxtRHhJequLBmVnQ85vuERlBOQqMkf2YCANcxdY3y4fJeMIXURL4qt1J1GbB65qS3jw0Hiiaw3ulGvojDZ1wICuaN129B56SKj1Bgo1iPY421kXladDYgUZvkh-iKSOhLIFObgYEKR-wb-OVC9V0LI66co2JZ7639Q9yoj4v07QUxBhR8j66XLlTAW6RQ",
    //      "refresh_token": "GSbx5kp2pvCcPAduMozpm8FJ-aGLS7E7bkk8Xc4votWGMPrvoHwH1ZbCdTubQ4NkuFV9yePy3_1gvnczJ7QcP_hVG8g-Ey8TVcsl4gCPOgoyZbaAzFSScs3wwLX3Qz2w",
    //      "scope": "openid",
    //      "id_token": "eyJraWQiOiI0NTFjYzVhOS1jNTdhLTRhM2EtOWZmNC1jMmVjZjY5Yzg3YTkiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJkdWNrIiwiYXVkIjoiY2xpZW50IiwiYXpwIjoiY2xpZW50IiwiYXV0aF90aW1lIjoxNzA4MzY0MDI4LCJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjkwMDAiLCJleHAiOjE3MDgzNjU4MjgsImlhdCI6MTcwODM2NDAyOCwic2lkIjoic1lPWVdPbkJ6bE9mOXJWX01fQUMwQ2Q2dFlzeTByWm5hU3c1T2Q0TlBLUSJ9.BKbdUGY-q1dPv5RvriKHtDSXhZupG3vkzVFe0DeN_eTji73ib2TAmzUKchqiAbP5nP_SIQO7F3P6FjrIzlPMXZYwVCgu6BQRayrlevQ5BaFsQvZPDktyz0AcYv6q2I-FaVGpWeTVqVV33YQHJlgXXhopIaGIFyGLualP6q81W8_ggnyOEraTL7mSWLSDWHKb_bK6KRvcuwJDIQSgCURnH5c05Vp2W_1xJC5cbj_pcKHA1bIgjmzRnw6DCLaU-DUAfM6Dhd5dzMtK73hkMLJoyirLlgrGD5IOrxPQsXM_Othq4IwV68r_e_kyQuC-yPgzkPBOwwZIvNrS53xaTif39w",
    //      "token_type": "Bearer",
    //      "expires_in": 3599
    //  }
}
