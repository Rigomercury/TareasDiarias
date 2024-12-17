package com.rigodev.tareasapp.Adaptador

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rigodev.tareasapp.Adaptador.RegistroAdapter.RegistroViewHolder
import com.rigodev.tareasapp.Detalle.Detalle_registro
import com.rigodev.tareasapp.Modelo.Tareas
import com.rigodev.tareasapp.R

class RegistroAdapter(private val tareaList: List<Tareas>, private val context: Context) :
    RecyclerView.Adapter<RegistroViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegistroViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_registro, parent, false)
        return RegistroViewHolder(view)
    }

    override fun onBindViewHolder(holder: RegistroViewHolder, position: Int) {
        val tarea = tareaList[position]
        holder.D_Notavta.text = tarea.notavta
        holder.D_Cliente.text = tarea.cliente
        holder.D_Codigo.text = tarea.codigo
        holder.D_Cantidad.text = tarea.cantidad
        holder.D_Parcial.text = tarea.parcial
        holder.D_Restante.text = tarea.restante
        holder.D_Cantidad.text = tarea.cantidad
        holder.D_Etapa.text = tarea.etapa
        holder.D_Nota.text = tarea.nota
        holder.D_Inicio.text = tarea.t_registro

        val etapa = tarea.etapa
        if (etapa.lowercase() != "terminado") {
            holder.D_Termino.text = ""
        } else {
            holder.D_Termino.text = tarea.t_actualizacion
        }

        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val tarea = tareaList[position]
                (context as Detalle_registro).mostrarDialogoCompartir(tarea)
            }
        }
    }

    override fun getItemCount(): Int {
        return tareaList.size
    }

    class RegistroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var D_Notavta: TextView = itemView.findViewById(R.id.D_Notavta)
        var D_Cliente: TextView = itemView.findViewById(R.id.D_Cliente)
        var D_Codigo: TextView = itemView.findViewById(R.id.D_Codigo)
        var D_Cantidad: TextView = itemView.findViewById(R.id.D_Cantidad)
        var D_Parcial: TextView = itemView.findViewById(R.id.D_Parcial)
        var D_Restante: TextView = itemView.findViewById(R.id.D_Restante)
        var D_Etapa: TextView = itemView.findViewById(R.id.D_Etapa)
        var D_Nota: TextView = itemView.findViewById(R.id.D_Nota)
        var D_Inicio: TextView = itemView.findViewById(R.id.D_Inicio)
        var D_Termino: TextView = itemView.findViewById(R.id.D_Termino)
    }
}

