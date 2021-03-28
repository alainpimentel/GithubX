package com.alainp.githubx.utilities

import com.google.common.util.concurrent.RateLimiter
import okhttp3.Interceptor
import okhttp3.Response

class RateLimitInterceptor : Interceptor {
    private val limiter: RateLimiter = RateLimiter.create(0.133)


    override fun intercept(chain: Interceptor.Chain): Response {
        limiter.acquire(1)
        return chain.proceed(chain.request())
    }
}