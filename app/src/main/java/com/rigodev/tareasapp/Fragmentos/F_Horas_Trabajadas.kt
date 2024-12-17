package com.rigodev.tareasapp.Fragmentos

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rigodev.tareasapp.Adaptador.Adaptador_Horas_Extras
import com.rigodev.tareasapp.BaseDeDatos.BDHelper
import com.rigodev.tareasapp.Modelo.HorasExtras
import com.rigodev.tareasapp.R
import java.text.ParseException
import java.text.SimpleDateFormat
import android.content.Intent
import android.text.format.DateFormat
import com.rigodev.tareasapp.BaseDeDatos.Constants
import com.rigodev.tareasapp.Modelo.Tareas
import java.util.Calendar
import java.util.Date
import java.util.Locale

class F_Horas_Trabajadas : Fragment() {
    private var editEntrada: EditText? = null
    private var editSalida: EditText? = null
    private var editFechaActual: EditText? = null
    private var resultados: TextView? = null
    private var bdHelper: BDHelper? = null
    private var btnCalcular: Button? = null
    private var btnHorasIn: Button? = null
    private var btnHorasOut: Button? = null
    private var reciclerView: RecyclerView? = null
    private var adapter: Adaptador_Horas_Extras? = null
    private var horasExt: MutableList<HorasExtras>?=null
    private var calculoHorasExtras: TextView? = null
    private var ids = 0
    private var ordenarFechaAsistenciaAsc: String = "FECHA ASC"


    @SuppressLint("MissingInflatedId", "CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_f__horas__trabajadas, container, false)


        editEntrada = view.findViewById(R.id.edit_entrada)
        editSalida = view.findViewById(R.id.edit_salida)
        btnCalcular = view.findViewById(R.id.btn_calcular)
        btnHorasIn = view.findViewById(R.id.btn_ingresar_hora_entrada)
        btnHorasOut = view.findViewById(R.id.btn_ingresar_hora_Salida)
        resultados = view.findViewById(R.id.tvCalculosHorasExtras)
        editFechaActual = view.findViewById(R.id.editFechaActual)
        reciclerView = view.findViewById(R.id.recylerView_horasExtras)
        calculoHorasExtras = view.findViewById(R.id.tvCalculosHorasExtras)

        bdHelper = BDHelper(requireContext())

        reciclerView?.layoutManager = LinearLayoutManager(context)

        horasExt = bdHelper?.obtenerHoras(ordenarFechaAsistenciaAsc)
        adapter = Adaptador_Horas_Extras(horasExt?: mutableListOf(), { horaExtra ->
            mostrarDialogoEliminar(horaExtra)
        },bdHelper!!)

        reciclerView?.adapter = adapter

        actualizarSumatoriaHorasExtras()

        val fechaActual = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(Date())
        editFechaActual?.setText(fechaActual)


        btnCalcular?.setOnClickListener{
            calcularHorasExtras()
            CargarRegistros()

        }
        btnHorasIn?.setOnClickListener{
            editEntrada?.setText(SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()))
        }
        btnHorasOut?.setOnClickListener{
            editSalida?.setText(SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()))
        }

        return view

    }

    private fun rellenarCampos(horaExtra: HorasExtras) {
        ids = horaExtra.ids
        editEntrada?.setText(horaExtra.horaEntrada)
        editSalida?.setText(horaExtra.horaSalida)
        editFechaActual?.setText(horaExtra.fechaActual)
    }

    private fun mostrarDialogoEliminar(horaExtra: HorasExtras) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.cuadro_dialogo_editar_eliminar)

        val btnEliminar: Button = dialog.findViewById(R.id.Btn_Eliminar_Registro)
        val btnEditar: Button = dialog.findViewById(R.id.Btn_Editar_Registro)

        btnEliminar.setOnClickListener {
            adapter?.removeItem(horaExtra)
            dialog.dismiss()
        }

        btnEditar.setOnClickListener {
            rellenarCampos(horaExtra)
            dialog.dismiss()
        }

        dialog.show()
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun calcularHorasExtras() {
        val entradaStr = editEntrada?.text.toString()
        val salidaStr = editSalida?.text.toString()
        val fechaActual = editFechaActual?.text.toString()

        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val sdfFecha = SimpleDateFormat("dd/MM/yy", Locale.getDefault()) // Asegúrate de que el formato coincida con la fecha actual

        try {
            val horaEntrada = sdf.parse(entradaStr)
            val horaSalida = sdf.parse(salidaStr)
            val fecha = sdfFecha.parse(fechaActual)

            if (horaEntrada != null && horaSalida != null && fecha != null) {
                val diferenciaMillis = horaSalida.time - horaEntrada.time
                val diferenciaHoras = diferenciaMillis / (1000 * 60 * 60).toDouble()

                val calendar = Calendar.getInstance()
                calendar.time = fecha
                val diaSemana = calendar.get(Calendar.DAY_OF_WEEK)

                val horasExtras = when (diaSemana) {
                    Calendar.SATURDAY, Calendar.SUNDAY -> {
                        // Si es sábado o domingo, todas las horas son extra
                        diferenciaHoras
                    }
                    else -> {
                        // Si es entre lunes y viernes, calcula las horas extras si es necesario
                        if (diferenciaHoras > HORAS_LABORALES_DIARIAS) {
                            diferenciaHoras - HORAS_LABORALES_DIARIAS
                        } else {
                            0.0
                        }
                    }
                }

                // Convertir horasExtras a String para asignar a resultados.text
                resultados?.text = "Horas Extras: ${String.format("%.1f", horasExtras)}"


                // Guardar las horas en la base de datos
                bdHelper?.insertarHoras(ids, entradaStr, salidaStr, fechaActual, String.format("%.1f", horasExtras))
                limpiarDatos()

            } else {
                resultados?.text = "Error en la entrada de datos"
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            resultados?.text = "Error en la entrada de datos"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun limpiarDatos() {
        editEntrada?.setText("")
        editSalida?.setText("")
    }

    private fun CargarRegistros() {
        //bdHelper?.eliminarRegistrosAsistencia()
        val horas = bdHelper?.obtenerHoras(ordenarFechaAsistenciaAsc) ?: emptyList()
        adapter?.let{
            it.horasExt.clear()
            it.horasExt.addAll(horas)
            it.notifyDataSetChanged()
        }
        actualizarSumatoriaHorasExtras()
    }

    private fun actualizarSumatoriaHorasExtras() {
        val totalHorasExtras = horasExt?.sumOf { it.horasExtras.toDoubleOrNull() ?: 0.0 } ?: 0.0
        calculoHorasExtras?.text = String.format("%.2f", totalHorasExtras)
    }


    companion object {
        private const val HORAS_LABORALES_DIARIAS = 9.5 // Horas laborales estándar
    }


}