package com.example.smart_education

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smart_education.Retrofit.RetrofitInterface
import kotlinx.android.synthetic.main.fragment_todo.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class TodoFragment : Fragment(R.layout.fragment_todo){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var todolist:List<Todos> = listOf()
        var size=0
        val email = activity?.intent?.getStringExtra("EMAIL")

        val BASE_URL = "http://192.168.43.208:3000"

        val progress = ProgressDialog(context)
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

       /* val map: HashMap<String?, String?> = HashMap()
        map["email"] = email
        map["date"] = currentTime*/

        val call: Call<Array<Todos>?>? = email?.let { retrofitInterface.executeGetpending(it) }
        call!!.enqueue(object : Callback<Array<Todos>?> {
            override fun onResponse(
                call: Call<Array<Todos>?>?,
                response: Response<Array<Todos>?>
            ) {
                if (response.code() == 200) {
                    val result: Array<Todos>? = response.body()
                    Toast.makeText(
                        context, "Tasks",
                        Toast.LENGTH_SHORT
                    ).show()

                    todolist = result?.toList()!!
                    size = todolist.size
                    val adapt = TodoAdapter(todolist)
                    rv_todo.adapter = adapt
                    rv_todo.layoutManager = LinearLayoutManager(context)
                    progress.dismiss()


                } else if (response.code() == 404) {
                    Toast.makeText(
                        context, "Try Again!",
                        Toast.LENGTH_SHORT
                    ).show()
                    progress.dismiss()
                }
            }

            override fun onFailure(call: Call<Array<Todos>?>, t: Throwable) {
                Toast.makeText(
                    context, "Try Again",
                    Toast.LENGTH_SHORT
                ).show()
                progress.dismiss()
            }
        })

        //val adapter = TodoAdapter(todoList)
        // rv_todo.adapter =adapter
        // rv_todo.layoutManager = LinearLayoutManager(activity)

        todo_add.setOnClickListener {
            todo_add.visibility = View.GONE
            new_todo.visibility = View.VISIBLE
            done_todo.visibility = View.VISIBLE
            todo_time.visibility = View.VISIBLE
        }
        val formate = SimpleDateFormat("dd/MM/YYYY", Locale.getDefault())
        var date: String = ""

        refresh_todo.setOnClickListener {
            for(i in 0..size){
                if(todolist[i].ischecked)
                {
                    val title = todolist[i].title
                    val dat = todolist[i].time
                    val map2: HashMap<String?, String?> = HashMap()
                    map2["email"] = email
                    map2["title"] = title
                    map2["date"] = dat
                    val call2: Call<Void?>? = retrofitInterface.executeDone(map2)
                    call2!!.enqueue(object : Callback<Void?> {
                        override fun onResponse(
                            call: Call<Void?>?,
                            response: Response<Void?>
                        ) {
                            if (response.code() == 200) {
                                //var result = response.body()
                                Toast.makeText(
                                    context, "Task Added",
                                    Toast.LENGTH_SHORT
                                ).show()
                                progress.dismiss()

                                new_todo.text?.clear()

                            } else if (response.code() == 404) {
                                Toast.makeText(
                                    context, "Try Again!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                progress.dismiss()
                            }
                        }

                        override fun onFailure(call: Call<Void?>, t: Throwable) {
                            Toast.makeText(
                                context, "Try Again",
                                Toast.LENGTH_SHORT
                            ).show()
                            progress.dismiss()
                        }
                    })
                }
            }
        }
        todo_time.setOnClickListener {
            val now = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this.requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    date = formate.format(selectedDate.time)


                    Toast.makeText(context, "date : $date", Toast.LENGTH_SHORT).show()
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        done_todo.setOnClickListener {

            todo_add.visibility = View.VISIBLE
            new_todo.visibility = View.GONE
            todo_time.visibility = View.GONE
            done_todo.visibility = View.GONE

            val title = new_todo.text.toString()

            val map1: HashMap<String?, String?> = HashMap()
            map1["email"] = email
            map1["title"] = title
            map1["date"] = date

            val call1: Call<Void?>? = retrofitInterface.executeTodoinsert(map1)
            call1!!.enqueue(object : Callback<Void?> {
                override fun onResponse(
                    call: Call<Void?>?,
                    response: Response<Void?>
                ) {
                    if (response.code() == 200) {
                        //var result = response.body()
                        Toast.makeText(
                            context, "Task Added",
                            Toast.LENGTH_SHORT
                        ).show()
                        progress.dismiss()

                        new_todo.text?.clear()

                    } else if (response.code() == 404) {
                        Toast.makeText(
                            context, "Try Again!",
                            Toast.LENGTH_SHORT
                        ).show()
                        progress.dismiss()
                    }
                }

                override fun onFailure(call: Call<Void?>, t: Throwable) {
                    Toast.makeText(
                        context, "Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                    progress.dismiss()
                }
            })
        }
    }
}