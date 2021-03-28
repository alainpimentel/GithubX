package com.alainp.githubx.api

import com.alainp.githubx.data.User
import com.alainp.githubx.data.UserDetail
import com.google.common.util.concurrent.RateLimiter
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface GithubService {

    @GET("/users")
    suspend fun getUsers(
        @Query("since") since: Long,
        @Query("perpage") perpage: Int = DEFAULT_LIMIT
    ): List<User>

    @GET("user")
    suspend fun getUserDetail(@Query("login") login: String): UserDetail

    companion object {
        private const val BASE_URL = "https://api.github.com/"
        private const val DEFAULT_LIMIT = 30

        fun create(): GithubService {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(RateLimitInterceptor())
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GithubService::class.java)
        }
    }
}

// TODO remove this and guave dependency
class RateLimitInterceptor : Interceptor {
    private val limiter: RateLimiter = RateLimiter.create(0.133)


    override fun intercept(chain: Interceptor.Chain): Response {
        limiter.acquire(1)
        return chain.proceed(chain.request())
    }
}