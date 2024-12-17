package com.rigodev.tareasapp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.rigodev.tareasapp.Fragmentos.F_AcercaDe
import com.rigodev.tareasapp.Fragmentos.F_Ajustes
import com.rigodev.tareasapp.Fragmentos.F_Escanner
import com.rigodev.tareasapp.Fragmentos.F_Horas_Trabajadas
import com.rigodev.tareasapp.Fragmentos.F_Parcial
import com.rigodev.tareasapp.Fragmentos.F_Todas
import com.rigodev.tareasapp.Login_usuario.Logeo_usuario

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var drawer: DrawerLayout? = null
    private val inactivityHandler = Handler()
    private var inactivityTimer: CountDownTimer? = null
    var DobleToqueparaSalir: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.itemIconTintList = null

        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer?.addDrawerListener(toggle)
        toggle.syncState()

        /*fragmento al ejecutar la aplicacion*/
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                F_Parcial()
            ).commit()
            navigationView.setCheckedItem(R.id.Opcion_Parcial)
        }
        startInactivityTimer()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.Opcion_Asistencia) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                F_Horas_Trabajadas()
            ).commit()
        }
        if (id == R.id.Opcion_Escanear) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                F_Escanner()
            ).commit()
        }
        if (id == R.id.Opcion_Todas) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                F_Todas()
            ).commit()
        }
        if (id == R.id.Opcion_Parcial) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                F_Parcial()
            ).commit()
        }
        if (id == R.id.Opcion_Ajustes) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                F_Ajustes()
            ).commit()
        }
        if (id == R.id.Opcion_Acerca) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                F_AcercaDe()
            ).commit()
        }
        if (id == R.id.Opcion_Salir) {
            cierreSesion()
        }
        drawer!!.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        resetInactivityTimer()
    }

    private fun startInactivityTimer() {
        inactivityTimer = object : CountDownTimer(INACTIVITY_TIMEOUT, INACTIVITY_TIMEOUT) {
            override fun onTick(millisUntilFinished: Long) {
                // No necesitas hacer nada en cada tick.
            }

            override fun onFinish() {
                // El usuario ha estado inactivo durante 15 segundos, cierra la sesión aquí.
                // Puedes llamar a un método para cerrar la sesión del usuario.
                // Por ejemplo, logout();
                cierreParcial()
            }
        }.start()
    }

    private fun resetInactivityTimer() {
        if (inactivityTimer != null) {
            inactivityTimer!!.cancel()
        }
        startInactivityTimer()
    }

    private fun cierreSesion() {
        startActivity(Intent(this@MainActivity, Logeo_usuario::class.java))
        Toast.makeText(this, "Cerraste la aplicacion", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun cierreParcial() {
        finish()
    }

    companion object {
        private const val INACTIVITY_TIMEOUT: Long = 3000000
    }
}