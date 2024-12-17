package com.rigodev.tareasapp.Detalle

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rigodev.tareasapp.Adaptador.RegistroAdapter
import com.rigodev.tareasapp.BaseDeDatos.BDHelper
import com.rigodev.tareasapp.BaseDeDatos.Constants
import com.rigodev.tareasapp.Modelo.Tareas
import com.rigodev.tareasapp.R
import java.util.Calendar
import java.util.Locale

class Detalle_registro : AppCompatActivity() {
    var D_Notavta: TextView? = null
    var D_Cliente: TextView? = null
    var D_Codigo: TextView? = null
    var D_Cantidad: TextView? = null
    var D_Parcial: TextView? = null
    var D_Restante: TextView? = null
    var D_Etapa: TextView? = null
    var D_Nota: TextView? = null
    var D_Inicio: TextView? = null
    var D_Termino: TextView? = null

    var Id_registro: String? = null
    var ids: Int = 0
    var contadorcito: Int = 0
    var helper: BDHelper? = null
    var dialog: Dialog? = null
    var tarea: MutableList<Tareas> = ArrayList()
    var adapter: RegistroAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        setContentView(R.layout.activity_detalle_registro)

        val actionBar = supportActionBar

        val intent = intent
        Id_registro = intent.getStringExtra("Nota_Vta")

        helper = BDHelper(this)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RegistroAdapter(tarea, this)
        recyclerView.adapter = adapter

        Inicializarvariable()
        MostrarInformacionRegistro()

        /*obtener titulo de registro*/
        val nota_venta =
            "N/V: " + Id_registro + " con: " + contadorcito + (if (contadorcito > 1) " elementos" else " elemento")
        checkNotNull(actionBar)
        actionBar.title = nota_venta
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)
    }

    private fun Inicializarvariable() {
        D_Notavta = findViewById(R.id.D_Notavta)
        D_Cliente = findViewById(R.id.D_Cliente)
        D_Codigo = findViewById(R.id.D_Codigo)
        D_Cantidad = findViewById(R.id.D_Cantidad)
        D_Parcial = findViewById(R.id.D_Parcial)
        D_Restante = findViewById(R.id.D_Restante)
        D_Etapa = findViewById(R.id.D_Etapa)
        D_Nota = findViewById(R.id.D_Nota)
        D_Inicio = findViewById(R.id.D_Inicio)
        D_Termino = findViewById(R.id.D_Termino)

        dialog = Dialog(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun MostrarInformacionRegistro() {
        val consulta =
            "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.C_NOTAVTA + " =\"" + Id_registro + "\""

        val db = helper!!.writableDatabase
        val cursor = db.rawQuery(consulta, null)

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") val notavta = cursor.getString(
                    cursor.getColumnIndex(
                        Constants.C_NOTAVTA
                    )
                )
                @SuppressLint("Range") val cliente = cursor.getString(
                    cursor.getColumnIndex(
                        Constants.C_CLIENTE
                    )
                )
                @SuppressLint("Range") val codigo = cursor.getString(
                    cursor.getColumnIndex(
                        Constants.C_CODIGO
                    )
                )
                @SuppressLint("Range") val cantidad = cursor.getString(
                    cursor.getColumnIndex(
                        Constants.C_CANTIDAD
                    )
                )
                @SuppressLint("Range") val parcial = cursor.getString(
                    cursor.getColumnIndex(
                        Constants.C_PARCIAL
                    )
                )
                @SuppressLint("Range") val restante = cursor.getString(
                    cursor.getColumnIndex(
                        Constants.C_RESTANTE
                    )
                )
                @SuppressLint("Range") val etapa =
                    cursor.getString(cursor.getColumnIndex(Constants.C_ETAPA))
                @SuppressLint("Range") val nota =
                    cursor.getString(cursor.getColumnIndex(Constants.C_NOTA))
                @SuppressLint("Range") val t_registro = cursor.getString(
                    cursor.getColumnIndex(
                        Constants.C_TIEMPO_REGISTRO
                    )
                )
                @SuppressLint("Range") val t_actualizacion = cursor.getString(
                    cursor.getColumnIndex(
                        Constants.C_TIEMPO_ACTUALIZACION
                    )
                )
                /* convertir tiempo a dia/mes/año*/
                /*tiempo registro*/
                val calendar_t_r = Calendar.getInstance(Locale.getDefault())
                calendar_t_r.timeInMillis = t_registro.toLong()
                val tiempo_registro = "" + DateFormat.format("dd/MM/yyyy hh:mm:aa", calendar_t_r)

                /*tiempo actualizacion*/
                val calendar_t_a = Calendar.getInstance(Locale.getDefault())
                calendar_t_a.timeInMillis = t_actualizacion.toLong()
                val tiempo_actualizacion =
                    "" + DateFormat.format("dd/MM/yyyy hh:mm:aa", calendar_t_a)

                tarea.add(
                    Tareas(
                        ids,
                        notavta,
                        cliente,
                        codigo,
                        cantidad,
                        parcial,
                        restante,
                        etapa,
                        nota,
                        tiempo_registro,
                        tiempo_actualizacion
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        contadorcito = tarea.size
        db.close()
        adapter!!.notifyDataSetChanged()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun mostrarDialogoCompartir(tarea: Tareas) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Compartir tarea")
        builder.setMessage("¿Desea compartir esta tarea?")

        builder.setPositiveButton("Compartir") { dialog, which ->
            val shareIntent = Intent()
            shareIntent.setAction(Intent.ACTION_SEND)
            shareIntent.putExtra(
                Intent.EXTRA_TEXT, """
     Nota de Venta: ${tarea.notavta}
     Cliente: ${tarea.cliente}
     Código: ${tarea.codigo}
     Cantidad: ${tarea.cantidad}
     Parcial: ${tarea.parcial}
     Restante: ${tarea.restante}
     Etapa: ${tarea.etapa}
     Nota: ${tarea.nota}
     Inicio: ${tarea.t_registro}
     Término: ${tarea.t_actualizacion}
     """.trimIndent()
            )

            shareIntent.setType("text/plain")
            startActivity(Intent.createChooser(shareIntent, "Compartir tarea vía"))
        }

        builder.setNegativeButton("Cancelar") { dialog, which -> dialog.dismiss() }

        val dialog = builder.create()
        dialog.show()
    }
}