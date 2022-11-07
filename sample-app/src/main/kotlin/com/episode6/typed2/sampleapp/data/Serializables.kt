package com.episode6.typed2.sampleapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@kotlinx.serialization.Serializable data class KtxSerializable(val content: String)

data class JavaIoSerializable(val content: String): java.io.Serializable

@Parcelize data class AndroidParcelable(val content: String): Parcelable

data class RegularAssDataClass(val content: String)
