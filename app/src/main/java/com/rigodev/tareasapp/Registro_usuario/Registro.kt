package com.rigodev.tareasapp.Registro_usuario

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rigodev.tareasapp.Login_usuario.Logeo_usuario
import com.rigodev.tareasapp.MainActivity
import com.rigodev.tareasapp.R

class Registro : AppCompatActivity() {
    var EtPasswordU: EditText? = null
    var EtPassword: EditText? = null
    var Btnregistrar: Button? = null

    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        setContentView(R.layout.activity_registro)

        InicializarVaiables()
        VerificarPasswordMaestra()


        Btnregistrar!!.setOnClickListener {
            val S_password = EtPasswordU!!.text.toString().trim { it <= ' ' }
            val S_C_password = EtPassword!!.text.toString().trim { it <= ' ' }

            //validar campos
            if (TextUtils.isEmpty(S_password)) {
                Toast.makeText(this@Registro, "Ingrese contraseña", Toast.LENGTH_SHORT).show()
            } else if (S_password.length < 6) {
                Toast.makeText(this@Registro, "Contraseña muy corta", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(S_C_password)) {
                Toast.makeText(this@Registro, "Confirme contraseña", Toast.LENGTH_SHORT).show()
            } else if (S_password != S_C_password) {
                Toast.makeText(this@Registro, "Las contraseñas no coinciden", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val editor = sharedPreferences!!.edit()
                editor.putString(KEY_PASSWORD, S_password)
                editor.putString(KEY_C_PASSWORD, S_C_password)
                editor.apply()

                Toast.makeText(this@Registro, "KEY PASSWORD$S_password", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@Registro, "KEY_C_PASSWORD$S_C_password", Toast.LENGTH_SHORT)
                    .show()

                val intent = Intent(this@Registro, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun InicializarVaiables() {
        EtPasswordU = findViewById(R.id.EtPasswordU)
        EtPassword = findViewById(R.id.EtPassword)
        Btnregistrar = findViewById(R.id.Btnregistrar)
        sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
    }

    private fun VerificarPasswordMaestra() {
        val mipassword = sharedPreferences!!.getString(KEY_PASSWORD, null)
        if (mipassword != null) {
            val intent = Intent(this@Registro, Logeo_usuario::class.java)
            startActivity(intent)
            finish()
        }
    }

    companion object {
        private const val SHARED_PREF = "mi_pref"
        private const val KEY_PASSWORD = "password"
        private const val KEY_C_PASSWORD = "c_password"
    }
}