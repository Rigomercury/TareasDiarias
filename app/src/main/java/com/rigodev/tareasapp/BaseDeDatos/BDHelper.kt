package com.rigodev.tareasapp.BaseDeDatos

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.rigodev.tareasapp.Modelo.HorasExtras
import com.rigodev.tareasapp.Modelo.Tareas
import java.text.SimpleDateFormat
import java.util.Locale

class BDHelper(context: Context?) :
    SQLiteOpenHelper(context, Constants.BD_NAME, null, Constants.BD_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(Constants.CREATE_TABLE)
        db.execSQL(Constants.CREATE_NEW_TABLE)
        db.execSQL(Constants.TABLE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 10) {
            db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_ASISTENCIA)
            db.execSQL(Constants.TABLE_CREATE)
        }
    }

    fun insertarRegistro(
        notavta: String?,
        cliente: String?,
        codigo: String?,
        cantidad: String?,
        parcial: String?,
        etapa: String?,
        nota: String?,
        T_regitro: String?,
        T_acualizacion: String?
    ): Long {
        val db = this.writableDatabase

        val values = ContentValues()

        // Convertir cantidad y parcial a números y calcular restante
        val cantidadInt = cantidad?.toInt() ?: 0
        val parcialInt = parcial?.toInt() ?: 0
        val restante = (cantidadInt - parcialInt)

        /*Inswertamos los datos*/
        values.put(Constants.C_NOTAVTA, notavta)
        values.put(Constants.C_CLIENTE, cliente)
        values.put(Constants.C_CODIGO, codigo)
        values.put(Constants.C_CANTIDAD, cantidad)
        values.put(Constants.C_PARCIAL, parcial)
        values.put(Constants.C_RESTANTE, restante)
        values.put(Constants.C_ETAPA, etapa)
        values.put(Constants.C_NOTA, nota)
        values.put(Constants.C_TIEMPO_REGISTRO, T_regitro)
        values.put(Constants.C_TIEMPO_ACTUALIZACION, T_acualizacion)

        /*insertamos la fila*/
        val id = db.insert(Constants.TABLE_NAME, null, values)

        /*Cerrar la base de datos*/
        db.close()

        return id
    }

    fun actualizarRegistro(
        ids: Int, notavta: String?, cliente: String?, codigo: String?, cantidad: String?,
        parcial: String?,  etapa: String?, nota: String?,
        T_regitro: String?, T_acualizacion: String?
    ) {
        val db = this.writableDatabase

        val values = ContentValues()

        // Convertir cantidad y parcial a números y calcular restante
        val cantidadInt = cantidad?.toInt() ?: 0
        val parcialInt = parcial?.toInt() ?: 0
        val restante = (cantidadInt - parcialInt)

        /*Actualizar los datos*/
        values.put(Constants.C_NOTAVTA, notavta)
        values.put(Constants.C_CLIENTE, cliente)
        values.put(Constants.C_CODIGO, codigo)
        values.put(Constants.C_CANTIDAD, cantidad)
        values.put(Constants.C_PARCIAL, parcial)
        values.put(Constants.C_RESTANTE, restante)
        values.put(Constants.C_ETAPA, etapa)
        values.put(Constants.C_NOTA, nota)
        values.put(Constants.C_TIEMPO_REGISTRO, T_regitro)
        values.put(Constants.C_TIEMPO_ACTUALIZACION, T_acualizacion)

        /*insertamos la fila*/
        db.update(Constants.TABLE_NAME, values, Constants.C_IDS + " =? ", arrayOf(ids.toString()))

        /*Cerrar la base de datos*/
        db.close()
    }

    fun ObtenerTodosLosRegistros(numero: Int, orderby: String): ArrayList<Tareas> {
        val tareasArrayList = ArrayList<Tareas>()
        val selectQuery = if (numero == 2) {
            "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.C_ETAPA + " != 'TERMINADO' ORDER BY " + orderby
        } else {
            "SELECT * FROM " + Constants.TABLE_NAME + " ORDER BY " + orderby
        }

        val db = this.writableDatabase
        @SuppressLint("Recycle") val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") val modelo_tareas = Tareas(
                    cursor.getInt(cursor.getColumnIndex(Constants.C_IDS)),
                    cursor.getString(cursor.getColumnIndex(Constants.C_NOTAVTA)),
                    cursor.getString(cursor.getColumnIndex(Constants.C_CLIENTE)),
                    cursor.getString(cursor.getColumnIndex(Constants.C_CODIGO)),
                    cursor.getString(cursor.getColumnIndex(Constants.C_CANTIDAD)),
                    cursor.getString(cursor.getColumnIndex(Constants.C_PARCIAL)),
                    cursor.getString(cursor.getColumnIndex(Constants.C_RESTANTE)),
                    cursor.getString(cursor.getColumnIndex(Constants.C_ETAPA)),
                    cursor.getString(cursor.getColumnIndex(Constants.C_NOTA)),
                    cursor.getString(cursor.getColumnIndex(Constants.C_TIEMPO_REGISTRO)),
                    cursor.getString(cursor.getColumnIndex(Constants.C_TIEMPO_ACTUALIZACION))
                )
                tareasArrayList.add(modelo_tareas)
            } while (cursor.moveToNext())
        }
        db.close()

        return tareasArrayList
    }

    fun BuscarRegistros(consulta: String): ArrayList<Tareas> {
        val tareasArrayList = ArrayList<Tareas>()
        val selectQuery =
            "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.C_NOTAVTA + " LIKE '%" + consulta + "%'"
        val db: SQLiteDatabase
        try {
            db = this.writableDatabase
        } catch (e: SQLException) {
            Log.e("BDHelper", "Error al obtener base de datos writable: ", e)
            return tareasArrayList
        }
        @SuppressLint("Recycle") val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") val modelo_tareas = Tareas(
                    cursor.getInt(cursor.getColumnIndex(Constants.C_IDS)),
                    cursor.getString(cursor.getColumnIndex(Constants.C_NOTAVTA)),
                    cursor.getString(cursor.getColumnIndex(Constants.C_CLIENTE)),
                    cursor.getString(cursor.getColumnIndex(Constants.C_CODIGO)),
                    cursor.getString(cursor.getColumnIndex(Constants.C_CANTIDAD)),
                    cursor.getString(cursor.getColumnIndex(Constants.C_PARCIAL)),
                    cursor.getString(cursor.getColumnIndex(Constants.C_RESTANTE)),
                    cursor.getString(cursor.getColumnIndex(Constants.C_ETAPA)),
                    cursor.getString(cursor.getColumnIndex(Constants.C_NOTA)),
                    cursor.getString(cursor.getColumnIndex(Constants.C_TIEMPO_REGISTRO)),
                    cursor.getString(cursor.getColumnIndex(Constants.C_TIEMPO_ACTUALIZACION))
                )
                tareasArrayList.add(modelo_tareas)
            } while (cursor.moveToNext())
        }
        db.close()

        return tareasArrayList
    }


    fun ObtenerNumeroRegistros(): Int {
        val countquery = "SELECT * FROM " + Constants.TABLE_NAME
        val db = this.writableDatabase
        val cursor = db.rawQuery(countquery, null)

        val contador = cursor.count

        cursor.close()

        return contador
    }

    fun EliminarRegistro(ids: String) {
        val db = writableDatabase
        db.delete(Constants.TABLE_NAME, Constants.C_IDS + " = ?", arrayOf(ids))
        db.close()
    }

    fun EliminarTodosRegistros() {
        val db = writableDatabase
        db.execSQL("DELETE FROM " + Constants.TABLE_NAME)
        db.close()
    }

    fun insertarCodigoEscaneado(code: String?, timestamp: String?): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(Constants.C_NUM_EAN, code)
        val id = db.insert(Constants.TABLE_EAN, null, values)
        db.close()
        return id
    }

    fun eliminarRegistros() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM " + Constants.TABLE_EAN)
        db.close()
    }


    @SuppressLint("Range")
    fun obtenerTodosLosCodigosEscaneados(): ArrayList<String> {
        val codes = ArrayList<String>()
        val selectQuery = "SELECT * FROM " + Constants.TABLE_EAN
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                codes.add(cursor.getString(cursor.getColumnIndex(Constants.C_NUM_EAN)))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return codes
    }

    fun insertarHoras(
        ids: Int,
        horaEntrada: String?,
        horaSalida: String?,
        fechaActual: String?,
        horasExtras:String?
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(Constants.COLUMN_ENTRADA, horaEntrada)
            put(Constants.COLUMN_SALIDA, horaSalida)
            put(Constants.COLUMN_HORASEXTRAS, horasExtras)
            put(Constants.COLUMN_FECHA, fechaActual)
        }
        val cursor = db.query(
            Constants.TABLE_ASISTENCIA,
            arrayOf(Constants.ID_HORAS),
            "${Constants.ID_HORAS} = ?",
            arrayOf(ids.toString()),
            null,
            null,
            null
        )

        val id: Long
        if (cursor.count == 0) {
            // Insertar un nuevo registro
            id = db.insert(Constants.TABLE_ASISTENCIA, null, values)
        } else {
            // Actualizar el registro existente
            db.update(Constants.TABLE_ASISTENCIA, values, "${Constants.ID_HORAS} = ?", arrayOf(ids.toString()))
            id = ids.toLong()
        }
        cursor.close()
        db.close()
        return id
    }

    @SuppressLint("Range")
    fun obtenerHoras(orderby: String): ArrayList<HorasExtras> {
        val horas = ArrayList<HorasExtras>()
        val selectQuery = """
            SELECT * FROM ${Constants.TABLE_ASISTENCIA}
            ORDER BY strftime('dd/MM/yy', ${Constants.COLUMN_FECHA}) ASC
        """.trimIndent()
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val horasExtras = HorasExtras(
                    cursor.getInt(cursor.getColumnIndex(Constants.ID_HORAS)),
                    cursor.getString(cursor.getColumnIndex(Constants.COLUMN_ENTRADA)),
                    cursor.getString(cursor.getColumnIndex(Constants.COLUMN_SALIDA)),
                    cursor.getString(cursor.getColumnIndex(Constants.COLUMN_FECHA)),
                    cursor.getString(cursor.getColumnIndex(Constants.COLUMN_HORASEXTRAS))
                )
                horas.add(horasExtras)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return horas
    }
    fun eliminarRegistrosAsistencia() {
        val db = writableDatabase
        db.execSQL("DELETE FROM " + Constants.TABLE_ASISTENCIA)
        db.close()
    }

    fun eliminarHoraExtra(ids: Int) {
        val db = writableDatabase
        db.delete(Constants.TABLE_ASISTENCIA, "${Constants.ID_HORAS} = ?", arrayOf(ids.toString()))
        db.close()
    }

}
