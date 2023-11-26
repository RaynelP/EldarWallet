package com.raynel.eldarwallet.model.implementations

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

class QrRepo {

    fun getQR(name: String, lastName: String): ByteArray? {
        val client = OkHttpClient()

        val bgColor = "#ffffff"
        val fgColor = "#000000"
        val height = "128"
        val width = "128"
        val content = "$name + $lastName" // Contenido dinámico

        val requestBody = FormBody.Builder()
            .add("bg-color", bgColor)
            .add("fg-color", fgColor)
            .add("height", height)
            .add("width", width)
            .add("content", content)
            .build()

        val request = Request.Builder()
            .url("https://neutrinoapi-qr-code.p.rapidapi.com/qr-code")
            .post(requestBody)
            .addHeader("content-type", "application/x-www-form-urlencoded")
            .addHeader("X-RapidAPI-Key", "50846a8a2emsh6d5d615cd66bdd5p18c4bejsn2007fedea211")
            .addHeader("X-RapidAPI-Host", "neutrinoapi-qr-code.p.rapidapi.com")
            .build()

        val response = client.newCall(request).execute()

        var image: ByteArray? = null
        if (response.isSuccessful) {
            val responseBody = response.body
            if (responseBody != null) {
                // Leer los bytes de la imagen
                image = responseBody.bytes()
            }
        }

        return image
    }

}