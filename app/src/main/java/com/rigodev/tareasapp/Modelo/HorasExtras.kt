package com.rigodev.tareasapp.Modelo

import android.os.Parcel
import androidx.versionedparcelable.ParcelField
import androidx.versionedparcelable.VersionedParcelize
import android.os.Parcelable

data class HorasExtras(
    val ids: Int,
    val horaEntrada: String,
    val horaSalida: String,
    val fechaActual: String,
    val horasExtras: String
)