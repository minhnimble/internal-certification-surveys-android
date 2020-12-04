package co.nimblehq.data.api.authenticator

import co.nimblehq.data.BuildConfig
import co.nimblehq.data.api.interceptor.AppRequestInterceptor
import co.nimblehq.data.api.providers.ConverterFactoryProvider
import co.nimblehq.data.api.providers.MoshiBuilderProvider
import co.nimblehq.data.api.providers.OkHttpClientProvider
import co.nimblehq.data.api.providers.RetrofitProvider
import co.nimblehq.data.api.request.helper.RequestHelper
import co.nimblehq.data.api.response.auth.OAuthAttributesResponse
import co.nimblehq.data.api.response.auth.OAuthDataResponse
import co.nimblehq.data.api.response.auth.OAuthResponse
import co.nimblehq.data.api.service.auth.AuthService
import co.nimblehq.data.lib.common.AUTHORIZATION_HEADER
import co.nimblehq.data.storage.SecureStorage
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.http.GET

@Suppress("IllegalIdentifier")
class AppRequestAuthenticatorTest {

    private lateinit var mockAuthService: AuthService
    private lateinit var mockSecureStorage: SecureStorage
    private lateinit var authenticator: AppRequestAuthenticator
    private lateinit var testApi: TestApi

    private val mockWebServer = MockWebServer()
    private val baseUrl = "/"
    private val httpUrl = mockWebServer.url(baseUrl)
    private val testData = TestData("Test Name")
    private val testDataJson = "{\"name\":\"${testData.name}\"}"
    private val authResponse = OAuthResponse(
        data = OAuthDataResponse(
            id = "id",
            type = "type",
            attributes = OAuthAttributesResponse(
                accessToken = "accessToken",
                refreshToken = "refreshToken",
                createdAt = 32503597200000, // (GMT): Tuesday, December 31, 2999 1:00:00 AM
                expiresIn = 7200,
                tokenType = "tokenType"
            )
        )
    )

    @Before
    fun setUp() {
        mockAuthService = mock()
        mockSecureStorage = object: SecureStorage {
            override var userAccessToken: String? = authResponse.data.attributes.accessToken
            override var userAccessTokenCreatedAt: Long? = authResponse.data.attributes.createdAt
            override var userAccessTokenExpiresIn: Long? = authResponse.data.attributes.expiresIn
            override var userRefreshToken: String? = authResponse.data.attributes.refreshToken
            override var userTokenType: String? = authResponse.data.attributes.tokenType
        }
        authenticator = AppRequestAuthenticator(
            mockSecureStorage,
            mockAuthService
        )
        testApi = createApi(TestApi::class.java, httpUrl.toString())
    }

    @Test
    fun `When a call is executed successfully, the response is also parsed succesfully`() {
        // Arrange
        val successResponse = MockResponse().setBody(testDataJson)
        mockWebServer.enqueue(successResponse)

        // Act
        val response = testApi.test().execute()
        mockWebServer.takeRequest()

        // Assert
        Assert.assertEquals(testData, response.body()!!)
    }

    @Test
    fun `When a call executes, the auth header is added in advanced by the interceptor`() {
        // Arrange
        val successResponse = MockResponse().setBody(testDataJson)
        mockWebServer.enqueue(successResponse)

        // Act
        testApi.test().execute()
        val recordedRequest = mockWebServer.takeRequest()
        val header = recordedRequest.getHeader(AUTHORIZATION_HEADER)

        // Assert
        Assert.assertEquals("${authResponse.data.attributes.tokenType} ${authResponse.data.attributes.accessToken}", header)
    }

    @Test
    fun `When a call fails with 401, the authenticator automatically refreshes token and triggers the call again successfully`() {
        // Arrange
        val invalidTokenResponse = MockResponse().setResponseCode(401) // Setup invalidTokenResponse
        val successResponse = MockResponse().setBody(testDataJson) // Setup successResponse

        whenever(
            mockAuthService.refreshToken(
                RequestHelper.refreshToken(mockSecureStorage.userRefreshToken ?: "")
            )
        ) doReturn Single.just(authResponse)

        mockWebServer.enqueue(invalidTokenResponse) // Enqueue 401 response
        mockWebServer.enqueue(successResponse) // Enqueue 200 success response

        // Act
        val response = testApi.test().execute()
        mockWebServer.takeRequest()
        val retryUnAuthorizedRequest = mockWebServer.takeRequest()
        val newHeader = retryUnAuthorizedRequest.getHeader(AUTHORIZATION_HEADER)

        // Assert
        Assert.assertEquals("${authResponse.data.attributes.tokenType} ${authResponse.data.attributes.accessToken}", newHeader)
        Assert.assertTrue(response.isSuccessful)
    }

    private fun <T> createApi(apiClass: Class<T>, baseUrl: String): T {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val httpClient = OkHttpClientProvider.getOkHttpClient(
            apiRequestInterceptor = AppRequestInterceptor(mockSecureStorage),
            httpLoggingInterceptor = loggingInterceptor,
            appRequestAuthenticator = authenticator
        )
        val moshi = MoshiBuilderProvider.moshiBuilder.build()
        val moshiFactory = ConverterFactoryProvider.getMoshiConverterFactory(moshi)
        val jsonApiFactory = ConverterFactoryProvider.getJsonApiConverterFactory(moshi)
        val retrofitBuilder = RetrofitProvider.getRetrofitBuilder(listOf(jsonApiFactory, moshiFactory), httpClient)
        return retrofitBuilder.baseUrl(baseUrl).build().create(apiClass)
    }
}

private interface TestApi {

    @GET("/test")
    fun test(): Call<TestData>
}

private data class TestData(val name: String)
