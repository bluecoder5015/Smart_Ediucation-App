package com.example.smart_education
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.smart_education.Retrofit.RetrofitInterface
import kotlinx.android.synthetic.main.activity_login2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity2 : AppCompatActivity() {

    var isRemember =false
    val BASE_URL = "http://192.168.43.208:3000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        var sharedPreferences= getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        isRemember = sharedPreferences.getBoolean("CHECKBOX",false)
        if(isRemember)
        {
            Intent(this@LoginActivity2,MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitInterface: RetrofitInterface = retrofit
            .create(RetrofitInterface::class.java)

        //event
        login_button.setOnClickListener {
                val email = email_login.editText?.text.toString()
                val password=password_login.editText?.text.toString()
                val ischeck = materialCheckBox.isChecked

            if(TextUtils.isEmpty(email))
            {
                Toast.makeText(this,"Email Can`t be Empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(password))
            {
                Toast.makeText(this,"Password Can`t be Empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val progress = ProgressDialog(this)
            progress.setMessage("Verifying Credentials :) ")
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progress.isIndeterminate = true
            progress.show()

            val map: HashMap<String?, String?> = HashMap()
            map["email"]=email
            map["password"]=password

            val call: Call<Void?>? = retrofitInterface.executeLogin(map)
            call!!.enqueue(object : Callback<Void?> {
                override fun onResponse(
                    call: Call<Void?>?,
                    response: Response<Void?>
                ) {
                    when {
                        response.code() == 200 -> {
                            //var result = response.body()
                             val editor: SharedPreferences.Editor = sharedPreferences.edit()
                             editor.putString("EMAIL",email)
                             editor.putString("PASSWORD",password)
                             editor.putBoolean("CHECKBOX",ischeck)
                             editor.apply()
                            Toast.makeText(
                                this@LoginActivity2, "Login Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            progress.dismiss()

                            Intent(this@LoginActivity2,MainActivity::class.java).also{
                                it.putExtra("Email",email)
                                startActivity(it)
                                finish()
                            }
                        }
                        response.code() == 404 -> {
                            Toast.makeText(
                                this@LoginActivity2, "Wrong Credentials",
                                Toast.LENGTH_LONG
                            ).show()
                            user_forgot.text = getString(R.string.forgot)
                            progress.dismiss()
                        }
                        response.code() == 400 -> {
                            Toast.makeText(
                                this@LoginActivity2, "Wrong Credentials",
                                Toast.LENGTH_LONG
                            ).show()
                            progress.dismiss()
                        }
                    }
                }

                override fun onFailure(
                    call: Call<Void?>?,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@LoginActivity2, t.message,
                        Toast.LENGTH_LONG
                    ).show()
                    progress.dismiss()
                }
            })
        }

        regis_page2.setOnClickListener {
            Intent(this@LoginActivity2,RegisterActivity2::class.java).also {
                startActivity(it)
            }
        }
    }
}