package com.rigodev.tareasapp.Adaptador

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.rigodev.tareasapp.BaseDeDatos.BDHelper
import com.rigodev.tareasapp.Detalle.Detalle_registro
import com.rigodev.tareasapp.Fragmentos.F_Horas_Trabajadas
import com.rigodev.tareasapp.Modelo.HorasExtras
import com.rigodev.tareasapp.R

class Adaptador_Horas_Extras(
    val horasExt: MutableList<HorasExtras>,
    private val onItemClicked:(HorasExtras)-> Unit,
    private val bdHelper: BDHelper
):RecyclerView.Adapter<Adaptador_Horas_Extras.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_horas_extras, parent, false)
        return ViewHolder(view)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val horaExtra = horasExt[position]

        holder.tv_itemFecha.text = horaExtra.fechaActual
        holder.tv_itemEntrada.text = horaExtra.horaEntrada
        holder.tv_itemSalida.text = horaExtra.horaSalida
        holder.tv_itemHorasExtras.text = horaExtra.horasExtras

        holder.itemView.setOnClickListener{
            onItemClicked(horaExtra)
        }

    }

    override fun getItemCount(): Int {return horasExt.size}

    fun removeItem(item: HorasExtras) {
        val position = horasExt.indexOf(item)
        if (position != -1) {
            horasExt.removeAt(position)
            bdHelper.eliminarHoraExtra(item.ids)  // Eliminar de la base de datos
            notifyItemRemoved(position)
        }
    }


    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val tv_itemEntrada: TextView = itemView.findViewById(R.id.tv_itemEntrada)
        val tv_itemSalida: TextView = itemView.findViewById(R.id.tv_itemSalida)
        val tv_itemFecha: TextView = itemView.findViewById(R.id.tv_itemFecha)
        val tv_itemHorasExtras: TextView = itemView.findViewById(R.id.tv_itemHorasExtras)
    }

}