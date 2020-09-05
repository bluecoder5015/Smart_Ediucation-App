package com.example.smart_education

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_message.*


class MessageFragment : Fragment(R.layout.fragment_message) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email_button.setOnClickListener {
            if(email_message.editText?.text.toString()!="" && email_subject.editText?.text.toString()!="" && email_to.editText?.text.toString()!="")
            {
                sendEmail()
            }
            else
            {
                Toast.makeText(context,"Fill all details",Toast.LENGTH_LONG).show()
            }
        }
        sms_button.setOnClickListener {
            if(sms_to.editText?.text.toString()!="" && sms_subject.editText?.text.toString()!="" )
            {
                val smsManager = SmsManager.getDefault() as SmsManager
                smsManager.sendTextMessage(sms_to.editText?.text.toString(), null, sms_subject.editText?.text.toString(), null, null)
            }
            else
            {
                Toast.makeText(context,"Fill all details",Toast.LENGTH_LONG).show()
            }
        }

    }
    private fun sendEmail() {
        Log.i("Send email", "")
        val TO = email_to.editText?.text.toString()
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, email_subject.editText?.text.toString())
        emailIntent.putExtra(Intent.EXTRA_TEXT, email_message.editText?.text.toString())
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."))

        } catch (ex: ActivityNotFoundException) {


        }
    }
    fun sendSMS()
    {
        val uri = Uri.parse("smsto:"+sms_to.editText?.text.toString())
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.putExtra("sms_body", sms_subject.editText?.text.toString())
        startActivity(intent)
    }
}