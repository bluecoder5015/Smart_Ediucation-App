package com.example.smart_education.Retrofit

import com.example.smart_education.Topics
import com.example.smart_education.Profile
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

         @POST("/done")
        fun executeDone(@Body map: HashMap<String?, String?>?): Call<Void?>?

        @GET("/done/{email}")
        fun executeGettodos(@Path("email") email:String): Call<Array<Todos>?>?

        @GET("/notdone/{email}")
        fun executeGetpending(@Path("email") email:String?): Call<Array<Todos>?>?

        @GET("/subject/{subject}/{email}")
        fun executeTopics(@Path("subject") subject:String?,@Path("email") email:String?): Call<Array<Topics>?>?

        @POST("/subject")
        fun executeAddTopics(@Body map: HashMap<String?, String?>?): Call<Void?>?

        @POST("/update")
        fun update(@Body map: HashMap<String?, String?>?):Call<Void>

        @GET("/details/{email}")
        fun details(@Path ("email") email:String?):Call<Profile>

}