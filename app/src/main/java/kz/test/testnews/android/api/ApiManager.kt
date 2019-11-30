package kz.test.testnews.android.api

import retrofit2.http.GET
import io.reactivex.Observable
import io.reactivex.Single
import kz.test.testnews.android.api.response.Comment
import kz.test.testnews.android.api.response.New
import org.json.JSONArray
import retrofit2.http.Path


interface ApiManager {
    @GET("/v0/newstories.json?print=pretty")
    fun getNews(): Observable<Array<Int>>

    @GET("v0/item/{newId}.json?print=pretty")
    fun getNewById(@Path("newId") newId: Int): Observable<New?>

    @GET("v0/item/{newId}.json?print=pretty")
    fun getComments(@Path("newId") newId: Int): Observable<Comment?>

    @GET("/v0/beststories.json?print=pretty")
    fun getBests(): Observable<Array<Int>>

    @GET("/v0/topstories.json?print=pretty")
    fun getTops(): Observable<Array<Int>>
}