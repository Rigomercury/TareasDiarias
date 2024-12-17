package com.rigodev.tareasapp.Login_usuario

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rigodev.tareasapp.MainActivity
import com.rigodev.tareasapp.R

class Logeo_usuario : AppCompatActivity() {
    var EtPassword: EditText? = null
    var BtnIngresarar: Button? = null
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        setContentView(R.layout.activity_logeo_usuario)

        InicializarVariable()

        BtnIngresarar!!.setOnClickListener {
            val S_password = EtPassword!!.text.toString().trim { it <= ' ' }
            val password_SP = sharedPreferences!!.getString(KEY_PASSWORD, null)
            if (S_password == "") {
                Toast.makeText(this@Logeo_usuario, "Campo es obligatorio", Toast.LENGTH_SHORT)
                    .show()
            } else if (S_password != password_SP) {
                Toast.makeText(
                    this@Logeo_usuario,
                    "La contraseña no es la correcta",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this@Logeo_usuario, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun InicializarVariable() {
        EtPassword = findViewById(R.id.EtPassword)
        BtnIngresarar = findViewById(R.id.BtnIngresarar)
        sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
    }

    companion object {
        private const val SHARED_PREF = "mi_pref"
        private const val KEY_PASSWORD = "password"
    }
}