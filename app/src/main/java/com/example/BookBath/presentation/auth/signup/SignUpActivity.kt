package com.example.BookBath.presentation.auth.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.BookBath.MainActivity
import com.example.BookBath.data.firebase.User
import com.example.BookBath.databinding.ActivitySignUpBinding
import com.example.BookBath.presentation.auth.signin.SignInActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding


    private lateinit var mDataseReference: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this);

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference()
        mDataseReference = mFirebaseInstance.getReference("User")


        iniView()
    }

    private fun iniView() {
        binding.apply{
            tbSignUp.setOnClickListener {
                startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
                finish()
            }
            btnRegister.setOnClickListener{
                register()
            }

        }
    }

    private fun register() {
        binding.apply{
            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            val confirmPass = edtPasswordConfirm.text.toString()

            if(name.trim() == ""){
                edtName.error = "Silahkan masukkan Nama"
                edtName.requestFocus()
            } else if(email.trim() == ""){
                edtEmail.error = "Silahkan masukkan Email"
                edtEmail.requestFocus()
            } else if(password.trim() == "") {
                edtPassword.error = "Silahkan masukkan Password"
                edtPassword.requestFocus()
            } else if(confirmPass != password) {
                edtPasswordConfirm.error = "Konfirmasi Password harus sesuai dengan Password"
                edtPasswordConfirm.requestFocus()
            } else {
                val userRegister = User(
                    name,
                    email,
                    password
                )
                saveUserAccount(email, userRegister)
                }
            }
        }

    private fun saveUserAccount(email: String, userRegister: User) {
        mDataseReference.child("email").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var user = snapshot.getValue(User::class.java)
                if(user?.email == null){
                    mDataseReference.child(email.replace(".", "|")).setValue(userRegister)
                    startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
                } else {
                    Toast.makeText(this@SignUpActivity, "Email Sudah di Gunakan", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignUpActivity, error.message, Toast.LENGTH_LONG).show()
                Log.i("registerError", error.message)
            }

        })
    }
}