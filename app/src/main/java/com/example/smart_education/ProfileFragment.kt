package com.example.smart_education

import android.content.Intent
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
        val BASE_URL = "http://192.168.43.208:3000"
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val email = activity?.intent?.getStringExtra("EMAIL")
        val retrofitInterface: RetrofitInterface =
            retrofit.create(RetrofitInterface::class.java)
        edit_profile_save.setOnClickListener {
            val call: Call<Profile> = retrofitInterface.details(email)
            call!!.enqueue(object : Callback<Profile> {
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
                        context, "Try Again",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            })

        }
        profile_edit_btn.setOnClickListener {

            Intent(context,Editprofile::class.java).also {
                startActivity(it)
            }

        }
        profile_setting_btn.setOnClickListener{

            Intent(context,profile_setting::class.java).also {
                startActivity(it)
            }
        }
        profile_history_btn.setOnClickListener {

            Intent(context,Todo_history::class.java).also {
                startActivity(it)
            }
        }


    }


}