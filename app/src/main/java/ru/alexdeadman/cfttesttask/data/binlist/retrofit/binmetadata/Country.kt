package ru.alexdeadman.cfttesttask.data.binlist.retrofit.binmetadata

import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("numeric") var numeric: String? = null,
    @SerializedName("alpha2") var alpha2: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("emoji") var emoji: String? = null,
    @SerializedName("currency") var currency: String? = null,
    @SerializedName("latitude") var latitude: Double? = null,
    @SerializedName("longitude") var longitude: Double? = null
)