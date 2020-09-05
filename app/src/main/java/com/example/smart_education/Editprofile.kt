package com.example.smart_education

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smart_education.Retrofit.RetrofitInterface
import kotlinx.android.synthetic.main.activity_editprofile.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_todo.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Editprofile : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)
        val BASE_URL = "http://192.168.43.208:3000"
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val email=""
        val retrofitInterface: RetrofitInterface =
            retrofit.create(RetrofitInterface::class.java)
        val map : HashMap<String?,String?> = HashMap()
        edit_profile_username.editText?.text.toString()
        map["organization"]=edit_profile_org.editText?.text.toString()
        map["class"]=edit_profile_std.editText?.text.toString()
        map["phone"]=edit_profile_userNumber.editText?.text.toString()
        map["profession"]=edit_profile_work.editText?.text.toString()
        map["email"]=email
        edit_profile_save.setOnClickListener {
            val call: Call<Void> = retrofitInterface.update(map)
            call!!.enqueue(object : Callback<Void> {
                override fun onResponse(
                    call: Call<Void>?,
                    response: Response<Void>
                ) {
                    if (response.code() == 200) {

                        Toast.makeText(
                            this@Editprofile, "Profile Updated",
                            Toast.LENGTH_SHORT
                        ).show()


                    } else if (response.code() == 404) {
                        Toast.makeText(
                            this@Editprofile, "Try Again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        this@Editprofile, "Try Again",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            })

        }

    }
}