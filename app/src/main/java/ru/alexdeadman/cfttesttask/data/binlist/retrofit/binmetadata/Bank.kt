package ru.alexdeadman.cfttesttask.data.binlist.retrofit.binmetadata

import com.google.gson.annotations.SerializedName

data class Bank(
    @SerializedName("name") var name: String? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("city") var city: String? = null
)