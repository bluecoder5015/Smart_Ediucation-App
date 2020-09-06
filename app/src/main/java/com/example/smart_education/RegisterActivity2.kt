package com.example.smart_education

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.example.smart_education.Retrofit.RetrofitInterface
import kotlinx.android.synthetic.main.activity_register2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern

class RegisterActivity2 : AppCompatActivity() {
    //val BASE_URL = "http://192.168.43.208:3000"
    val BASE_URL = "https://smarteducationv1.herokuapp.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register2)

        val  USERNAME_PATTERN:String= "[a-zA-Z.\\s]+"

        var pattern: Pattern
        pattern = Pattern.compile(USERNAME_PATTERN)

        regis2_submit.setOnClickListener {

            val progress = ProgressDialog(this@RegisterActivity2)
            progress.setMessage("Verifying Credentials :) ")
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progress.isIndeterminate = true
            progress.show()
            val edit_email = regis_email.editText?.text.toString()
            val edit_name = regis_name.editText?.text.toString()
            val edit_password = regis_password.editText?.text.toString()
            val edit_phone = regis_phone.editText?.text.toString()
            val organisation = regis_organisation.editText?.text.toString()
            val  standard = regis_class.editText?.text.toString()



            when {
                (!pattern.matcher(edit_name).matches()) -> {
                    Toast.makeText(
                        this@RegisterActivity2,
                        "Enter Valid Name",
                        Toast.LENGTH_SHORT
                    ).show()
                    progress.dismiss()
                    return@setOnClickListener
                }
                (!Patterns.EMAIL_ADDRESS.matcher(edit_email).matches()) -> {
                    Toast.makeText(
                        this@RegisterActivity2,
                        "Invalid Email Address",
                        Toast.LENGTH_SHORT
                    ).show()
                    progress.dismiss()
                    return@setOnClickListener
                }
                (!Patterns.PHONE.matcher(edit_phone).matches()) -> {
                    Toast.makeText(
                        this@RegisterActivity2,
                        "Enter Valid Phone Number",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    progress.dismiss()
                    return@setOnClickListener
                }
                (edit_phone.length != 10) -> {
                    Toast.makeText(
                        this@RegisterActivity2,
                        "Enter Valid 10 Digit Phone Number",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    progress.dismiss()
                    return@setOnClickListener
                }

                (TextUtils.isEmpty(edit_password)) -> {
                    Toast.makeText(
                        this@RegisterActivity2,
                        "Password can`t be Empty",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    progress.dismiss()
                    return@setOnClickListener
                }
                (edit_password.length < 8) -> {
                    Toast.makeText(
                        this@RegisterActivity2,
                        "Enter minimum 8 digit password",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    progress.dismiss()
                    return@setOnClickListener
                }

            }


            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitInterface: RetrofitInterface =
                retrofit.create(RetrofitInterface::class.java)


            val map: HashMap<String?, String?> = HashMap()
            map["name"]=edit_name
            map["phone"]=edit_phone
            map["organization"]=organisation
            map["password"]=edit_password
            map["email"]=edit_email
            map["class"]=standard


            val call: Call<Void?>? = retrofitInterface.executeSignup(map)
            call!!.enqueue(object : Callback<Void?> {
                override fun onResponse(
                    call: Call<Void?>?,
                    response: Response<Void?>
                ) {
                    if (response.code() == 200) {
                        var sharedPreferences= getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putString("EMAIL",edit_email).apply()
                        //var result = response.body()
                        Toast.makeText(
                            this@RegisterActivity2, "Registered Sucessfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        progress.dismiss()
                        regis_email.editText?.text?.clear()
                        regis_name.editText?.text?.clear()
                        regis_password.editText?.text?.clear()
                        regis_organisation.editText?.text?.clear()
                        regis_class.editText?.text?.clear()
                        regis_phone.editText?.text?.clear()
                        finish()
                    } else if (response.code() == 400) {
                        Toast.makeText(
                            this@RegisterActivity2, "Please try Again!",
                            Toast.LENGTH_SHORT
                        ).show()
                        progress.dismiss()
                    }
                }

                override fun onFailure(call: Call<Void?>, t: Throwable) {
                    Toast.makeText(
                        this@RegisterActivity2, "Try Again!",
                        Toast.LENGTH_SHORT
                    ).show()
                    progress.dismiss()
                }
            })

        }
    }
}