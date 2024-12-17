package com.rigodev.tareasapp.BaseDeDatos

object Constants {
    const val BD_NAME: String = "PASSWORD_BD"

    const val BD_VERSION: Int = 10

    const val TABLE_NAME: String = "PASSWORD_TABLE"
    const val TABLE_EAN: String = "EAN_TABLE"
    const val TABLE_ASISTENCIA: String = "REGISTRO_ASISTENCIA"

    const val C_IDS: String = "IDS"
    const val C_NOTAVTA: String = "NOTAVENTA"
    const val C_CLIENTE: String = "CLIENTE"
    const val C_CODIGO: String = "CODIGO"
    const val C_CANTIDAD: String = "CANTIDAD"
    const val C_PARCIAL: String = "PARCIAL"
    const val C_RESTANTE: String = "RESTANTE"
    const val C_ETAPA: String = "ETAPA"
    const val C_NOTA: String = "NOTA"
    const val C_TIEMPO_REGISTRO: String = "TIEMPO_REGISTRO"
    const val C_TIEMPO_ACTUALIZACION: String = "TIEMPO_ACTUALIZACION"

    const val C_IDEAN: String = "IDEAN"
    const val C_NUM_EAN: String = "NUM_EAN"

    const val ID_HORAS: String = "IDHORAS"
    const val NOMBRE_EMPLEADO: String = "NOMBRE_EMPLEADO"
    const val COLUMN_ENTRADA: String = "HORA_ENTRADA"
    const val COLUMN_SALIDA: String = "HORA_SALIDA"
    const val COLUMN_HORASEXTRAS: String = "HORAS_EXTRAS"
    const val COLUMN_FECHA: String = "FECHA"

    // Definición de la creación de la nueva tabla
    const val CREATE_NEW_TABLE: String = ("CREATE TABLE " + TABLE_EAN
            + " ("
            + C_IDEAN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + C_NUM_EAN + " INTEGER"
            + ")")

    const val CREATE_TABLE: String = ("CREATE TABLE " + TABLE_NAME
            + "("
            + C_IDS + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + C_NOTAVTA + " TEXT,"
            + C_CLIENTE + " TEXT,"
            + C_CODIGO + " TEXT,"
            + C_CANTIDAD + " TEXT,"
            + C_PARCIAL + " TEXT,"
            + C_RESTANTE + " TEXT,"
            + C_ETAPA + " TEXT,"
            + C_NOTA + " TEXT,"
            + C_TIEMPO_REGISTRO + " TEXT,"
            + C_TIEMPO_ACTUALIZACION + " TEXT"
            + ")")

    const val TABLE_CREATE: String = ("CREATE TABLE " + TABLE_ASISTENCIA
            + " ("
            + ID_HORAS + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ENTRADA + " TEXT, "
            + COLUMN_SALIDA + " TEXT, "
            + COLUMN_HORASEXTRAS + " REAL, "
            + COLUMN_FECHA + " TEXT"
            + ")")
}
