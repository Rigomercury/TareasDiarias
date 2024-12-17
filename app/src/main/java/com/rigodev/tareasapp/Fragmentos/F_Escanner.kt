package com.rigodev.tareasapp.Fragmentos

import android.annotation.SuppressLint
import android.app.Dialog
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CompoundBarcodeView
import com.rigodev.tareasapp.Adaptador.CodeAdapter
import com.rigodev.tareasapp.BaseDeDatos.BDHelper
import com.rigodev.tareasapp.R
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

class F_Escanner : Fragment() {
    private var etCodigoEscanner: EditText? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: CodeAdapter? = null
    private var databaseHelper: BDHelper? = null
    private var FAB_ExportarRegistro: FloatingActionButton? = null
    private var dialog: Dialog? = null
    private var barcodeView: CompoundBarcodeView? = null
    private var mediaPlayer: MediaPlayer? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_f__escanner, container, false)

        etCodigoEscanner = view.findViewById(R.id.etCodigoEscanner)
        recyclerView = view.findViewById(R.id.recylerView_scanner)
        recyclerView?.setLayoutManager(LinearLayoutManager(context))
        FAB_ExportarRegistro = view.findViewById(R.id.FAB_Exportar)
        barcodeView = view.findViewById(R.id.barcode_scanner)

        databaseHelper = BDHelper(context)
        val codes: MutableList<String> = databaseHelper!!.obtenerTodosLosCodigosEscaneados()
        adapter = CodeAdapter(codes)
        recyclerView?.setAdapter(adapter)

        // Inicializa el Dialog aquí
        dialog = Dialog(requireContext())

        mediaPlayer = MediaPlayer.create(context, R.raw.bip_3)

        FAB_ExportarRegistro?.setOnClickListener{ compartirDatos() }

        val fabScanner = view.findViewById<FloatingActionButton>(R.id.FAB_Scanner)
        fabScanner.setOnClickListener {
            barcodeView?.resume()
            barcodeView?.decodeSingle(callback)
        }


        return view
    }

    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text != null) {
                val code = result.text
                etCodigoEscanner!!.setText(code)
                val timestamp = System.currentTimeMillis()
                databaseHelper!!.insertarCodigoEscaneado(code, timestamp.toString())
                adapter!!.addCode(code)
                //barcodeView.pause();
                if (mediaPlayer != null) {
                    mediaPlayer!!.start() // Reproducir sonido de beep
                }
            }
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {
        }
    }

    override fun onResume() {
        super.onResume()
        barcodeView!!.resume()
    }

    override fun onPause() {
        super.onPause()
        barcodeView!!.pause()
    }

    private fun compartirDatos() {
        dialog!!.setContentView(R.layout.cuadro_dialogo_exportar_registros)

        val Btn_Si = dialog!!.findViewById<Button>(R.id.Btn_Si)
        val Btn_Cancelar = dialog!!.findViewById<Button>(R.id.Btn_Cancelar)

        Btn_Si.setOnClickListener { exportarRegistros() }
        Btn_Cancelar.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()
        dialog!!.setCancelable(false)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun exportarRegistros() {
        // Nombre Carpeta
        val carpeta = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "Tareas App"
        )
        var carpetaCreada = false

        if (!carpeta.exists()) {
            // Si la carpeta no existe, creamos una nueva
            carpetaCreada = carpeta.mkdirs()
        }
        // Nombre del archivo
        val csvnombreArchivo = "RegistrosEan.csv"
        // Concatenar el nombre de la carpeta y del archivo
        val Carpeta_Archivo = "$carpeta/$csvnombreArchivo"

        // Obtener los registros que vamos a exportar
        val registroList: List<String> = adapter!!.codes

        try {
            val fw = FileWriter(Carpeta_Archivo)
            val bw = BufferedWriter(fw)
            for (registro in registroList) {
                bw.write(registro)
                bw.newLine()
            }
            bw.close()
            databaseHelper!!.eliminarRegistros()
            adapter!!.codes.clear()
            adapter!!.notifyDataSetChanged()
            dialog!!.dismiss()
        } catch (e: IOException) {
            Toast.makeText(activity, "Error grave: " + e.message, Toast.LENGTH_LONG).show()
        }
    }
}
