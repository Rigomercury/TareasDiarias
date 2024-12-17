package com.rigodev.tareasapp.Fragmentos

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.text.InputType
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.opencsv.CSVReader
import com.rigodev.tareasapp.BaseDeDatos.BDHelper
import com.rigodev.tareasapp.BaseDeDatos.Constants
import com.rigodev.tareasapp.Login_usuario.Logeo_usuario
import com.rigodev.tareasapp.MainActivity
import com.rigodev.tareasapp.Modelo.Tareas
import com.rigodev.tareasapp.R
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class F_Ajustes : Fragment() {
    private var Eliminar_todos_registros: TextView? = null
    private var Exportar_Archivo: TextView? = null
    private var Importar_Archivo: TextView? = null
    private var Cambiar_Password_Maestra: TextView? = null
    private var dialog: Dialog? = null
    private var cambiar_maestra: Dialog? = null

    var bdHelper: BDHelper? = null

    var ordenarTituloAsc: String = Constants.C_NOTAVTA + " ASC"

    var sharedPreferences: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        val view = inflater.inflate(R.layout.fragment_f__ajustes, container, false)

        Eliminar_todos_registros = view.findViewById(R.id.Eliminar_todos_registros)
        Exportar_Archivo = view.findViewById(R.id.Exportar_Archivo)
        Importar_Archivo = view.findViewById(R.id.Importar_Archivo)
        Cambiar_Password_Maestra = view.findViewById(R.id.Cambiar_Password_Maestra)


        dialog = Dialog(requireActivity())
        cambiar_maestra = Dialog(requireActivity())
        bdHelper = BDHelper(activity)
        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)


        Eliminar_todos_registros?.setOnClickListener{ Dialog_Eliminar_Registros() }

        Exportar_Archivo?.setOnClickListener{ ExportarRegistro() }

        Importar_Archivo?.setOnClickListener{
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("¿Importar CSV?")
            builder.setMessage("Se eliminaron todos los registros actuales")
            builder.setPositiveButton("Continuar") { dialogInterface, i ->
                bdHelper!!.EliminarTodosRegistros()
                ImportarRegistro()
            }
            builder.setNegativeButton("Cancelar") { dialogInterface, i ->
                Toast.makeText(
                    activity,
                    "Importacion Cancelada",
                    Toast.LENGTH_SHORT
                ).show()
            }
            builder.create().show()
        }

        Cambiar_Password_Maestra?.setOnClickListener{ //Toast.makeText(getActivity(), "Cambiar contraseña maestra", Toast.LENGTH_SHORT).show();
            Cambiar_maestra()
        }

        return view
    }


    private fun Dialog_Eliminar_Registros() {
        dialog!!.setContentView(R.layout.cuadro_dialogo_aliminar_todos_registros)

        val Btn_Si = dialog!!.findViewById<Button>(R.id.Btn_Si)
        val Btn_Cancelar = dialog!!.findViewById<Button>(R.id.Btn_Cancelar)

        Btn_Si.setOnClickListener {
            bdHelper!!.EliminarTodosRegistros()
            startActivity(Intent(activity, MainActivity::class.java))
            Toast.makeText(activity, "Se han elimnado todos los registros", Toast.LENGTH_SHORT)
                .show()
            dialog!!.dismiss()
        }
        Btn_Cancelar.setOnClickListener { dialog!!.dismiss() }

        dialog!!.show()
        dialog!!.setCancelable(false)
    }

    private fun ExportarRegistro() {
        //Nombre Carpeta
        val carpeta = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "Tareas App"
        )
        var carpetaCreada = false

        if (!carpeta.exists()) {
            //si la carpeta no existe creamos una nueva
            carpetaCreada = carpeta.mkdirs()
        }
        //Nombre del archivo
        val csvnombreArchivo = "Registros.csv"
        //Concatenar el nombre de la carpeta y del archivo
        val Carpeta_Archivo = "$carpeta/$csvnombreArchivo"

        //Obtener el registro que vamos a exportar
        var registroList = ArrayList<Tareas>()
        registroList.clear()
        registroList = bdHelper!!.ObtenerTodosLosRegistros(1, ordenarTituloAsc)

        try {
            //Escribir en el archivo
            val fileWriter = FileWriter(Carpeta_Archivo)
            for (i in registroList.indices) {
                fileWriter.append("").append(registroList[i].notavta)
                fileWriter.append(",")
                fileWriter.append("").append(registroList[i].cliente)
                fileWriter.append(",")
                fileWriter.append("").append(registroList[i].codigo)
                fileWriter.append(",")
                fileWriter.append("").append(registroList[i].cantidad)
                fileWriter.append(",")
                fileWriter.append("").append(registroList[i].etapa)
                fileWriter.append(",")
                fileWriter.append("").append(registroList[i].nota)
                fileWriter.append(",")
                fileWriter.append("").append(registroList[i].t_registro)
                fileWriter.append(",")
                fileWriter.append("").append(registroList[i].t_actualizacion)
                fileWriter.append("\n")
            }
            fileWriter.flush()
            fileWriter.close()
            Toast.makeText(activity, "Se ha exportado el archivo", Toast.LENGTH_LONG).show()
            startActivity(Intent(activity, MainActivity::class.java))
        } catch (e: Exception) {
            Toast.makeText(activity, "Grave  Error: " + e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun ImportarRegistro() {
        //Establecer Ruta
        val Carpeta_Archivo =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/Tareas App/" + "Registros.csv"
        val file = File(Carpeta_Archivo)
        if (file.exists()) {
            try {
                val csvReader = CSVReader(FileReader(file.absoluteFile))
                var nextLine: Array<String?>
                while ((csvReader.readNext().also { nextLine = it }) != null) {
                    val notavta = nextLine[1]
                    val cliente = nextLine[2]
                    val codigo = nextLine[3]
                    val cantidad = nextLine[4]
                    val parcial = nextLine[5]
                    val restante = nextLine[6]
                    val etapa = nextLine[7]
                    val nota = nextLine[8]
                    val tiempoR = nextLine[9]
                    val tiempoA = nextLine[10]

                    val id = bdHelper!!.insertarRegistro(
                        notavta,
                        cliente,
                        codigo,
                        cantidad,
                        parcial,
                        etapa,
                        nota,
                        tiempoR,
                        tiempoA
                    )
                }
                Toast.makeText(activity, "Archivo importado con exito", Toast.LENGTH_SHORT).show()
                startActivity(Intent(activity, MainActivity::class.java))
            } catch (e: Exception) {
                Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(activity, "No existe Respaldo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun Cambiar_maestra() {
        val password_maestra_recuperada = sharedPreferences!!.getString(KEY_PASSWORD, null)

        //Conexion con el cuadro de dialogo
        cambiar_maestra!!.setContentView(R.layout.cuadro_dialogo_password_maestra)

        //inicializar las vistas
        val Txt_password_maestra =
            cambiar_maestra!!.findViewById<TextView>(R.id.Txt_password_maestra)
        val Password_maestra = cambiar_maestra!!.findViewById<EditText>(R.id.Password_maestra)
        val Et_nuevo_password_maestra =
            cambiar_maestra!!.findViewById<EditText>(R.id.Et_nuevo_password_maestra)
        val Et_C_nuevo_password_maestra =
            cambiar_maestra!!.findViewById<EditText>(R.id.Et_C_nuevo_password_maestra)
        val Btn_cambiar_password_maestra =
            cambiar_maestra!!.findViewById<Button>(R.id.Btn_cambiar_password_maestra)
        val Btn_cancelar_password_maestra =
            cambiar_maestra!!.findViewById<Button>(R.id.Btn_cancelar_password_maestra)

        Btn_cancelar_password_maestra.setOnClickListener {
            Toast.makeText(activity, "Cancelar Modificacion de clave maestra", Toast.LENGTH_SHORT)
                .show()
            startActivity(Intent(activity, MainActivity::class.java))
        }
        Btn_cambiar_password_maestra.setOnClickListener {
            val S_nuevo_password = Et_nuevo_password_maestra.text.toString().trim { it <= ' ' }
            val S_C_nuevo_password = Et_C_nuevo_password_maestra.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(S_nuevo_password)) {
                Toast.makeText(activity, "Ingrese contraseña", Toast.LENGTH_SHORT).show()
            } else if (S_nuevo_password.length < 6) {
                Toast.makeText(activity, "Contraseña muy corta", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(S_C_nuevo_password)) {
                Toast.makeText(activity, "Confirme contraseña", Toast.LENGTH_SHORT).show()
            } else if (S_nuevo_password != S_C_nuevo_password) {
                Toast.makeText(activity, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            } else {
                val editor = sharedPreferences!!.edit()
                editor.putString(KEY_PASSWORD, S_nuevo_password)
                editor.putString(KEY_C_PASSWORD, S_C_nuevo_password)
                editor.apply()

                startActivity(Intent(activity, Logeo_usuario::class.java))
                activity!!.finish()
                Toast.makeText(activity, "La contraseña se a cambiado", Toast.LENGTH_SHORT).show()
                cambiar_maestra!!.dismiss()
            }
        }

        Password_maestra.setText(password_maestra_recuperada)

        //Propiedades de editText
        Password_maestra.isEnabled = false
        Password_maestra.setBackgroundColor(Color.TRANSPARENT)
        Password_maestra.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        cambiar_maestra!!.show()
        cambiar_maestra!!.setCancelable(true)
    }

    companion object {
        private const val SHARED_PREF = "mi_pref"
        private const val KEY_PASSWORD = "password"
        private const val KEY_C_PASSWORD = "c_password"
    }
}