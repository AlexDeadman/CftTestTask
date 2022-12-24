package ru.alexdeadman.cfttesttask.data.binlist.retrofit.binmetadata

import com.google.gson.annotations.SerializedName

data class BinMetadata(
    @SerializedName("number") var number: Number? = null,
    @SerializedName("scheme") var scheme: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("brand") var brand: String? = null,
    @SerializedName("prepaid") var prepaid: Boolean? = null,
    @SerializedName("country") var country: Country? = null,
    @SerializedName("bank") var bank: Bank? = null
)