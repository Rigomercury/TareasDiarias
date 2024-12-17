package com.rigodev.tareasapp.Fragmentos

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rigodev.tareasapp.Adaptador.Adaptador_Tareas
import com.rigodev.tareasapp.BaseDeDatos.BDHelper
import com.rigodev.tareasapp.BaseDeDatos.Constants
import com.rigodev.tareasapp.OpcionesTareas.Agregar_Actualizar_Registro
import com.rigodev.tareasapp.R

class F_Parcial : Fragment() {
    var helper: BDHelper? = null
    var FAB_AgregarPassword: FloatingActionButton? = null
    var recylerView_Registros: RecyclerView? = null
    var dialog: Dialog? = null
    var dialog_ordenar: Dialog? = null
    var ordenarNuevo: String = Constants.C_TIEMPO_REGISTRO + " DESC"
    var ordenarPasados: String = Constants.C_TIEMPO_REGISTRO + " ASC"
    var ordenarTituloAsc: String = Constants.C_IDS + " ASC"
    var ordenarTituloDes: String = Constants.C_IDS + " DESC"

    var EstadoOrden: String = ordenarTituloAsc

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        val view = inflater.inflate(R.layout.fragment_f__parcial, container, false)

        recylerView_Registros = view.findViewById(R.id.recylerView_Registros_parcial)
        FAB_AgregarPassword = view.findViewById(R.id.FAB_AgregarPassword)
        helper = BDHelper(activity)
        dialog = Dialog(requireActivity())
        dialog_ordenar = Dialog(requireActivity())

        CargarRegistros(ordenarTituloAsc)

        FAB_AgregarPassword?.setOnClickListener{
            val intent = Intent(activity, Agregar_Actualizar_Registro::class.java)
            intent.putExtra("MODO_EDICION", false)
            startActivity(intent)
        }

        return view
    }

    private fun CargarRegistros(orderby: String) {
        EstadoOrden = orderby
        val adaptadorPassword = Adaptador_Tareas(
            requireActivity(), helper!!.ObtenerTodosLosRegistros(
                2,
                orderby
            )
        )

        recylerView_Registros!!.adapter = adaptadorPassword
    }

    private fun BuscarRegistros(consulta: String) {
        val adaptadorPassword = Adaptador_Tareas(requireActivity(), helper!!.BuscarRegistros(consulta))
        recylerView_Registros!!.adapter = adaptadorPassword
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragmento_todos, menu)

        val item = menu.findItem(R.id.menu_Buscar_registros)
        val searchView = item.actionView as SearchView?
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                BuscarRegistros(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                BuscarRegistros(newText)
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_ordenar_registro) {
            OrdenarRegistros()
            return true
        }
        if (id == R.id.menu_Numero_registros) {
            Visualizar_Total_Registros()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        CargarRegistros(EstadoOrden)
        super.onResume()
    }

    private fun Visualizar_Total_Registros() {
        dialog!!.setContentView(R.layout.cuadro_dialogo_total_registros)

        val Total = dialog!!.findViewById<TextView>(R.id.Total)
        val BtnEntendidoTotal = dialog!!.findViewById<Button>(R.id.BtnEntendidoTotal)

        //Obtener el valor entero de registros
        val total = helper!!.ObtenerNumeroRegistros()
        //convertir a cadena el nmero de registros
        val total_string = total.toString()
        Total.text = total_string

        BtnEntendidoTotal.setOnClickListener { dialog!!.dismiss() }

        dialog!!.show()
        dialog!!.setCancelable(false)
    }

    private fun OrdenarRegistros() {
        dialog_ordenar!!.setContentView(R.layout.cuadro_dialogo_ordenar_registros)

        val Btn_nuevos = dialog_ordenar!!.findViewById<Button>(R.id.Btn_nuevos)
        val Btn_Pasados = dialog_ordenar!!.findViewById<Button>(R.id.Btn_Pasados)
        val Btn_Asc = dialog_ordenar!!.findViewById<Button>(R.id.Btn_Asc)
        val Btn_Desc = dialog_ordenar!!.findViewById<Button>(R.id.Btn_Desc)

        Btn_nuevos.setOnClickListener {
            CargarRegistros(ordenarNuevo)
            dialog_ordenar!!.dismiss()
        }
        Btn_Pasados.setOnClickListener {
            CargarRegistros(ordenarPasados)
            dialog_ordenar!!.dismiss()
        }
        Btn_Asc.setOnClickListener {
            CargarRegistros(ordenarTituloAsc)
            dialog_ordenar!!.dismiss()
        }
        Btn_Desc.setOnClickListener {
            CargarRegistros(ordenarTituloDes)
            dialog_ordenar!!.dismiss()
        }

        dialog_ordenar!!.show()
        dialog_ordenar!!.setCancelable(true)
    }

    private fun compartirDatos() {
        val numeroTelefono = "+56991831880"
        val lineaHorizontal = " " // Puedes ajustar la longitud según tus necesidades

        // Texto que deseas compartir
        val mensaje = "DATOS SOLICITADOS PARA TOMAR CITAS POR MAIL:"

        // Crear un intent para abrir WhatsApp y compartir el mensaje
        val sendIntent = Intent()
        sendIntent.setAction(Intent.ACTION_VIEW)
        val uri = "whatsapp://send?phone=$numeroTelefono&text=$mensaje"
        sendIntent.setData(Uri.parse(uri))
        startActivity(sendIntent)
    }
}