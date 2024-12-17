package com.rigodev.tareasapp.Adaptador

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rigodev.tareasapp.Adaptador.Adaptador_Tareas.HolderPassword
import com.rigodev.tareasapp.BaseDeDatos.BDHelper
import com.rigodev.tareasapp.Detalle.Detalle_registro
import com.rigodev.tareasapp.MainActivity
import com.rigodev.tareasapp.Modelo.Tareas
import com.rigodev.tareasapp.OpcionesTareas.Agregar_Actualizar_Registro
import com.rigodev.tareasapp.R

class Adaptador_Tareas(private val context: Context, private val tareasList: ArrayList<Tareas>) :
    RecyclerView.Adapter<HolderPassword>() {
    var dialog: Dialog = Dialog(context)
    var bdHelper: BDHelper = BDHelper(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderPassword {
        /*inflamos el item*/
        val view = LayoutInflater.from(context).inflate(R.layout.item_password, parent, false)
        return HolderPassword(view)
    }

    override fun onBindViewHolder(
        holder: HolderPassword,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val modelo_tareas = tareasList[position]
        val ids = modelo_tareas.ids
        val notavta = modelo_tareas.notavta
        val cliente = modelo_tareas.cliente
        val codigo = modelo_tareas.codigo
        val cantidad = modelo_tareas.cantidad
        val parcial = modelo_tareas.parcial
        val restante = modelo_tareas.restante
        val etapa = modelo_tareas.etapa
        val nota = modelo_tareas.nota
        val tiempo_registro = modelo_tareas.t_registro
        val tiempo_actualizacion = modelo_tareas.t_actualizacion

        holder.Item_notavta.text = notavta
        holder.Item_cliente.text = cliente
        holder.Item_codigo.text = codigo
        holder.Item_nota.text = nota
        holder.Item_restante.text = restante

        // Seleccionar la imagen según item_codigo
        if (codigo.lowercase().contains("thor")) {
            holder.img_item_password.setImageResource(R.drawable.thor)
        } else if (codigo.lowercase().contains("pagoda")) {
            holder.img_item_password.setImageResource(R.drawable.pagoda)
        } else if (codigo.lowercase().contains("viento")) {
            holder.img_item_password.setImageResource(R.drawable.viento)
        } else if (codigo.lowercase().contains("haze")) {
            holder.img_item_password.setImageResource(R.drawable.haze)
        } else if (codigo.lowercase().contains("monzon")) {
            holder.img_item_password.setImageResource(R.drawable.monzon)
        } else {
            holder.img_item_password.setImageResource(R.drawable.mg)
        }

        holder.itemView.setOnClickListener { /*usuario presione en el item*/
            val intent = Intent(context, Detalle_registro::class.java)
            /* Enviamos el dato id a la actividad Detalle registro*/
            intent.putExtra("Nota_Vta", notavta)
            context.startActivity(intent)
        }

        holder.Ib_mas_opciones.setOnClickListener {
            Opciones_Editar_Elimnar(
                ids,
                "" + position,
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
        }
    }

    override fun getItemCount(): Int {
        /*regresa tamaño de lista*/
        return tareasList.size
    }

    class HolderPassword(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var Item_notavta: TextView = itemView.findViewById(R.id.Item_notavta)
        var Item_cliente: TextView = itemView.findViewById(R.id.Item_cliente)
        var Item_codigo: TextView = itemView.findViewById(R.id.Item_codigo)
        var Item_cantidad: TextView = itemView.findViewById(R.id.Item_cantidad)
        var Item_parcial: TextView = itemView.findViewById(R.id.Item_parcial)
        var Item_restante: TextView = itemView.findViewById(R.id.Item_restante)
        var Item_etapa: TextView = itemView.findViewById(R.id.Item_etapa)
        var Item_nota: TextView = itemView.findViewById(R.id.Item_nota)
        var Item_fecha: TextView = itemView.findViewById(R.id.Item_fecha)
        var Ib_mas_opciones: ImageButton = itemView.findViewById(R.id.Ib_mas_opciones)
        var img_item_password: ImageView = itemView.findViewById(R.id.img_item_password)
    }

    private fun Opciones_Editar_Elimnar(
        ids: Int,
        posicion: String,
        notavta: String,
        cliente: String,
        codigo: String,
        cantidad: String,
        parcial: String,
        restante: String,
        etapa: String,
        nota: String,
        tiempo_registro: String,
        tiempo_actualizacion: String
    ) {
        dialog.setContentView(R.layout.cuadro_dialogo_editar_eliminar)

        val Btn_Editar_Registro = dialog.findViewById<Button>(R.id.Btn_Editar_Registro)
        val Btn_Eliminar_Registro = dialog.findViewById<Button>(R.id.Btn_Eliminar_Registro)

        Btn_Editar_Registro.setOnClickListener {
            val intent = Intent(context, Agregar_Actualizar_Registro::class.java)
            intent.putExtra("POSICION", posicion)
            intent.putExtra("IDS", ids)
            intent.putExtra("NOTA_VENTA", notavta)
            intent.putExtra("CLIENTE", cliente)
            intent.putExtra("CODIGO", codigo)
            intent.putExtra("CANTIDAD", cantidad)
            intent.putExtra("PARCIAL", parcial)
            intent.putExtra("RESTANTE", restante)
            intent.putExtra("ETAPA", etapa)
            intent.putExtra("NOTA", nota)
            intent.putExtra("T_REGISTRO", tiempo_registro)
            intent.putExtra("T_ACTUALIZACION", tiempo_actualizacion)
            intent.putExtra("MODO_EDICION", true)
            context.startActivity(intent)
            dialog.dismiss()
        }

        Btn_Eliminar_Registro.setOnClickListener {
            bdHelper.EliminarRegistro(ids.toString())
            val intent = Intent(context, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            Toast.makeText(context, "Registro Eliminado", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
        dialog.setCancelable(true)
    }
}
