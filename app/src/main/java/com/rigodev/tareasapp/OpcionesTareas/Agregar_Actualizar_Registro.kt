package com.rigodev.tareasapp.OpcionesTareas

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rigodev.tareasapp.BaseDeDatos.BDHelper
import com.rigodev.tareasapp.MainActivity
import com.rigodev.tareasapp.R
import java.util.Locale

class Agregar_Actualizar_Registro : AppCompatActivity() {
    var EtNotaVenta: EditText? = null
    var EtCliente: EditText? = null
    var EtCantidad: EditText? = null
    var EtEtapa: EditText? = null
    var EtNota: EditText? = null
    var EtParcial: EditText? = null
    var EtCodigo: AutoCompleteTextView? = null
    var TvRestante: TextView? = null

    var notavta: String? = null
    var cliente: String? = null
    var codigo: String? = null
    var cantidad: String? = null
    var parcial: String? = null
    var restante: String? = null
    var etapa: String? = null
    var nota: String? = null
    var tiempo_registro: String? = null
    var tiempo_actualizacion: String? = null
    var ids: Int = 0
    private var MODO_EDICION = false

    private var bdHelper: BDHelper? = null

    private val opciones = arrayOf(
        "Thor",
        "Monzon",
        "Pagoda",
        "Viento 80",
        "Viento 100"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        setContentView(R.layout.activity_agregar_tarea)
        val actionBar = checkNotNull(supportActionBar)
        actionBar.title = ""

        InicializarVariable()
        ObtenerInformacion()

        EtCodigo!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                // Convertir el texto a mayúsculas solo si es diferente del texto actual
                val upperCaseText = s.toString().uppercase(Locale.getDefault())
                if (upperCaseText != s.toString()) {
                    EtCodigo!!.removeTextChangedListener(this)
                    EtCodigo!!.setText(upperCaseText)
                    EtCodigo!!.setSelection(upperCaseText.length)
                    EtCodigo!!.addTextChangedListener(this)
                }
            }
        })

        EtCliente!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(e: Editable) {
                val mayusculaLetrasTexto = e.toString().uppercase(Locale.getDefault())
                if (mayusculaLetrasTexto != e.toString()) {
                    EtCliente!!.removeTextChangedListener(this)
                    EtCliente!!.setText(mayusculaLetrasTexto)
                    EtCliente!!.setSelection(mayusculaLetrasTexto.length)
                    EtCliente!!.addTextChangedListener(this)
                }
            }
        })

        EtEtapa!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(e: Editable) {
                val mayusculaLetrasTexto = e.toString().uppercase(Locale.getDefault())
                if (mayusculaLetrasTexto != e.toString()) {
                    EtEtapa!!.removeTextChangedListener(this)
                    EtEtapa!!.setText(mayusculaLetrasTexto)
                    EtEtapa!!.setSelection(mayusculaLetrasTexto.length)
                    EtEtapa!!.addTextChangedListener(this)
                }
            }
        })

        EtNota!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(e: Editable) {
                val mayusculaLetrasTexto = e.toString().uppercase(Locale.getDefault())
                if (mayusculaLetrasTexto != e.toString()) {
                    EtNota!!.removeTextChangedListener(this)
                    EtNota!!.setText(mayusculaLetrasTexto)
                    EtNota!!.setSelection(mayusculaLetrasTexto.length)
                    EtNota!!.addTextChangedListener(this)
                }
            }
        })
    }

    private fun InicializarVariable() {
        EtNotaVenta = findViewById(R.id.EtNotaVenta)
        EtCliente = findViewById(R.id.EtCliente)
        EtCodigo = findViewById(R.id.EtCodigo)
        EtCantidad = findViewById(R.id.EtCantidad)
        EtEtapa = findViewById(R.id.EtEtapa)
        EtNota = findViewById(R.id.EtNota)
        EtParcial = findViewById(R.id.EtParcial)
        TvRestante = findViewById(R.id.TvRestante)

        bdHelper = BDHelper(this)

        // Crear un adaptador y configurarlo en el AutoCompleteTextView
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, opciones)
        EtCodigo?.setAdapter(adapter)
    }

    private fun ObtenerInformacion() {
        val intent = intent
        MODO_EDICION = intent.getBooleanExtra("MODO_EDICION", false)

        if (MODO_EDICION) {
            //Verdadero
            ids = intent.getIntExtra("IDS", -1)
            notavta = intent.getStringExtra("NOTA_VENTA")
            cliente = intent.getStringExtra("CLIENTE")
            codigo = intent.getStringExtra("CODIGO")
            cantidad = intent.getStringExtra("CANTIDAD")
            parcial = intent.getStringExtra("PARCIAL")
            restante = intent.getStringExtra("RESTANTE")
            etapa = intent.getStringExtra("ETAPA")
            nota = intent.getStringExtra("NOTA")
            tiempo_registro = intent.getStringExtra("T_REGISTRO")
            tiempo_actualizacion = intent.getStringExtra("T_ACTUALIZACION")

            /*seteamos la informacion en las vistas*/
            EtNotaVenta!!.setText(notavta)
            EtCliente!!.setText(cliente)
            EtCodigo!!.setText(codigo)
            EtCantidad!!.setText(cantidad)
            EtParcial!!.setText(parcial)
            TvRestante!!.text = restante
            EtEtapa!!.setText(etapa)
            EtNota!!.setText(nota)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun Agregar_Actualizar_R() {
        /*Obtener datos de entrada*/
        notavta = EtNotaVenta!!.text.toString().trim { it <= ' ' }
        cliente = EtCliente!!.text.toString().trim { it <= ' ' }
        codigo = EtCodigo!!.text.toString().trim { it <= ' ' }
        cantidad = EtCantidad!!.text.toString().trim { it <= ' ' }
        parcial = EtParcial!!.text.toString().trim { it <= ' ' }
        restante = TvRestante!!.text.toString().trim { it <= ' ' }
        etapa = EtEtapa!!.text.toString().trim { it <= ' ' }
        nota = EtNota!!.text.toString().trim { it <= ' ' }

        if (MODO_EDICION) {
            //Actualiza Registro
            /*Tiempo del dispositivo*/
            val tiempo_actual = "" + System.currentTimeMillis()
            bdHelper!!.actualizarRegistro(
                ids,
                notavta,
                cliente,
                codigo,
                cantidad,
                parcial,
                etapa,
                nota,
                tiempo_registro,
                tiempo_actual
            )
            Toast.makeText(this, "Actualizado con Exito", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@Agregar_Actualizar_Registro, MainActivity::class.java))
            finish()
        } else {
            //nuevo registro
            if (!notavta!!.isEmpty()) {
                /*Obtener tiempo dispositivo*/
                val tiempo = "" + System.currentTimeMillis()
                val id = bdHelper!!.insertarRegistro(
                    notavta,
                    cliente,
                    codigo,
                    cantidad,
                    parcial,
                    etapa,
                    nota,
                    tiempo,
                    tiempo
                )
                Toast.makeText(this, "Se guardo con exito", Toast.LENGTH_SHORT).show()

                EtCodigo!!.setText("")
                EtCantidad!!.setText("")
                EtParcial!!.setText("")
                //TvRestante!!.text = ""
                EtEtapa!!.setText("")
                EtNota!!.setText("")
                EtCodigo!!.requestFocus()
            } else {
                EtNotaVenta!!.error = "Campo obligatorio"
                EtNotaVenta!!.isFocusable = true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_guardar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.Guardar_Password) {
            Agregar_Actualizar_R()
        } else if (item.itemId == R.id.Volver) {
            startActivity(Intent(this@Agregar_Actualizar_Registro, MainActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}