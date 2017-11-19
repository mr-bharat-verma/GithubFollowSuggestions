package com.githubfollow.github

import com.githubfollow.model.GithubUser
import com.githubfollow.network.RetrofitUtils
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.http.GET
import retrofit2.http.Query

class GithubClient {
    private val githubApi: GithubApi

    companion object {
        const val BASE_URL = "https://api.github.com"
    }

    interface GithubApi {
        @GET("users")
        fun getUsers(@Query("since") since: Int): Observable<List<GithubUser>>
    }

    init {
        githubApi = RetrofitUtils.createRetrofitApi(GithubApi::class.java, BASE_URL)
    }

    fun getUsers(since: Int): Observable<List<GithubUser>> {
        return githubApi.getUsers(since)
                .subscribeOn(Schedulers.io())
    }
}