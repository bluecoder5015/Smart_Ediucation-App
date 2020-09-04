package com.example.smart_education

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_todo.*
import com.example.smart_education.MainActivity

class TodoFragment : Fragment(R.layout.fragment_todo) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todo_add.setOnClickListener {
            Intent(context,AddTodo::class.java).also{
                startActivity(it)
            }
        }
    }
}