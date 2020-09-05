package com.example.smart_education.Retrofit

import com.example.smart_education.Todos
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitInterface {
        @POST("/login")
        fun executeLogin(@Body map: HashMap<String?, String?>?): Call<Void?>?

        @POST("/register")
        fun executeSignup(@Body map: HashMap<String?, String?>?): Call<Void?>?

        @POST("/todoinsert")
        fun executeTodoinsert(@Body map: HashMap<String?, String?>?): Call<Void?>?

        @GET("/done/{email}")
        fun executeGettodos(@Path("email") email:String): Call<Array<Todos>?>?

        @GET("/notdone/{email}")
        fun executeVerifyemail(@Path("email") email:String): Call<Void?>?



}