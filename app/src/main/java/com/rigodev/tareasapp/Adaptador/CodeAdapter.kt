package com.rigodev.tareasapp.Adaptador

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rigodev.tareasapp.R

class CodeAdapter(@JvmField val codes: MutableList<String>) :
    RecyclerView.Adapter<CodeAdapter.ViewHolder?>() {
    var dialog: Dialog? = null
    var FAB_ExportarRegistro: FloatingActionButton? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_scanner, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val code = codes[position]
        holder.tv_itemScanner.text = code
    }


    override fun getItemCount(): Int {
        return codes.size
    }

    fun addCode(code: String) {
        codes.add(code)
        notifyItemInserted(codes.size - 1)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_itemScanner: TextView = itemView.findViewById(R.id.tv_itemScanner)
    }
}

