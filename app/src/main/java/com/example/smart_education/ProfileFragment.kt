package com.example.smart_education

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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