package com.pascal.mytextrecogniter.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseTranslate(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
) : Parcelable

@Parcelize
data class Data(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("origin")
	val origin: String? = null,

	@field:SerializedName("targets")
	val targets: List<String?>? = null
) : Parcelable
