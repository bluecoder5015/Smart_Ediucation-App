package com.example.smart_education

import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smart_education.Retrofit.RetrofitInterface
import kotlinx.android.synthetic.main.activity_todo_history.*
import kotlinx.android.synthetic.main.fragment_todo.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Todo_history : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_history)


        var docpreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        val email = docpreferences?.getString("EMAIL","")
        //val BASE_URL = "http://192.168.43.208:3000"
        val BASE_URL = "https://smarteducationfinal.herokuapp.com/"

        val progress = ProgressDialog(this)
        progress.setMessage("Verifying Credentials :) ")
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progress.isIndeterminate = true
        progress.show()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitInterface: RetrofitInterface =
            retrofit.create(RetrofitInterface::class.java)

        val call: Call<Array<Todos>?>? = retrofitInterface.executeGetpending(email)
        call!!.enqueue(object : Callback<Array<Todos>?> {
            override fun onResponse(
                call: Call<Array<Todos>?>?,
                response: Response<Array<Todos>?>
            ) {
                if (response.code() == 200) {
                    val result: Array<Todos>? = response.body()
                    Toast.makeText(
                        this@Todo_history, "Tasks",
                        Toast.LENGTH_SHORT
                    ).show()

                    val adapt = result?.toList()?.let { TodoAdapter(it) }
                    rv_history.adapter = adapt
                    rv_history.layoutManager = LinearLayoutManager(this@Todo_history)
                    progress.dismiss()


                } else if (response.code() == 404) {
                    Toast.makeText(
                        this@Todo_history, "Try Again!",
                        Toast.LENGTH_SHORT
                    ).show()
                    progress.dismiss()
                }
            }

            override fun onFailure(call: Call<Array<Todos>?>, t: Throwable) {
                Toast.makeText(
                    this@Todo_history, "Try Again",
                    Toast.LENGTH_SHORT
                ).show()
                progress.dismiss()
            }
        })
    }
}