package com.example.smart_education.Retrofit

import com.example.smart_education.Todos
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitInterface {
        @POST("/login")
        fun executeLogin(@Body map: HashMap<String?, String?>?): Call<Void?>?

        @POST("/register")
        fun executeSignup(@Body map: HashMap<String?, String?>?): Call<Void?>?

        @POST("/todoinsert")
        fun executeTodoinsert(@Body map: HashMap<String?, String?>?): Call<Void?>?

        @POST("gettodos")
        fun executeGettodos(@Body map: HashMap<String?, String?>?): Call<Array<Todos>?>?

        @POST("/notdone/{email}")
        fun executeVerifyemail(@Body map:HashMap<String?,String?>?): Call<Void?>?

        @POST("/inserttime")
        fun executeTimeinsert(@Body map: HashMap<String?, String?>?): Call<Void?>?

}