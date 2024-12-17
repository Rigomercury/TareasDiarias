package com.rigodev.tareasapp.Modelo

class Tareas(
    var ids: Int,
    var notavta: String,
    var cliente: String,
    var codigo: String,
    var cantidad: String,
    var parcial: String,
    var restante: String,
    var etapa: String,
    var nota: String,
    var t_registro: String,
    var t_actualizacion: String
) {

    val restanteCalculado:String
        get(){
            // Convertir los valores de String a int
            val cantidadInt = cantidad.toInt()
            val parcialInt = parcial.toInt()

            // Realizar la resta
            val resultado = cantidadInt - parcialInt

            // Convertir el resultado a String y devolverlo
            return resultado.toString()

        }

    fun setId(ids: Int) {
        this.ids = ids
    }
}
