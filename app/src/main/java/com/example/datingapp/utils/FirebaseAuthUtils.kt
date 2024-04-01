package com.example.datingapp.utils

import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthUtils {

    companion object{
        private lateinit var auth : FirebaseAuth

        fun getUid() : String{
            auth = FirebaseAuth.getInstance()
            var uid = auth.currentUser?.uid.toString()
            return uid
        }
    }
}