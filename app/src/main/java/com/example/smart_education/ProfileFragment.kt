package com.example.smart_education

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.smart_education.Retrofit.RetrofitInterface
import kotlinx.android.synthetic.main.activity_editprofile.*
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val BASE_URL = "http://192.168.43.208:3000"
        val BASE_URL = "https://smarteducationv1.herokuapp.com/"
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var docpreferences = activity?.getSharedPreferences("LOGIN",Context.MODE_PRIVATE)
        val email = docpreferences?.getString("EMAIL","")
        val retrofitInterface: RetrofitInterface =
            retrofit.create(RetrofitInterface::class.java)
        val call: Call<Profile> = retrofitInterface.details(email)
        call.enqueue(object : Callback<Profile> {
            override fun onResponse(
                call: Call<Profile>?,
                response: Response<Profile>
            ) {
                if (response.code() == 200) {
                    val res: Profile? =response.body()
                    if(res!=null)
                    {
                        profile_user_name.text=res.name
                        profile_user_work.text=res.profession
                    }



                } else if (response.code() == 404) {
                    Toast.makeText(
                        context, "Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Profile>, t: Throwable) {
                Toast.makeText(
                    context, "Try Again  Failed",
                    Toast.LENGTH_SHORT
                ).show()

            }
        })


        profile_edit_btn.setOnClickListener {

            var intent = Intent(activity, Editprofile::class.java)
            startActivity(intent)

        }
        profile_setting_btn.setOnClickListener{

            var intent = Intent(activity, profile_setting::class.java)
            startActivity(intent);
        }
        profile_history_btn.setOnClickListener {

            var intent = Intent(activity, Todo_history::class.java)
            startActivity(intent)
        }
        profile_logout_btn.setOnClickListener {
            val editor: SharedPreferences.Editor = docpreferences!!.edit()
                  editor.clear()
                  editor.apply()

                  Intent(context,LoginActivity2::class.java).also{
                      startActivity(it)
                  }
        }

    }


}