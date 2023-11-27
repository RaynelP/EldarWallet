package com.raynel.eldarwallet.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.raynel.eldarwallet.model.implementations.QrRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class QRViewModel(
    val name: String,
    val lastName: String
): ViewModel() {

    private val qrRepo = QrRepo()

    private val _image: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    val image = _image.filterNotNull()

    init {
        getQR(name, lastName)
    }
    private fun getQR(name: String, lastName: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val img = qrRepo.getQR(name, lastName)
                _image.value = convertImageByteArrayToBitmap(img)
            }catch (e: Exception){

            }
        }
    }

    private fun convertImageByteArrayToBitmap(imageData: ByteArray?): Bitmap? {
        imageData?.let {
            return BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
        }
        return null
    }

    class QrViewModelFactory(
        val name: String,
        val lastName: String
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            if (modelClass.isAssignableFrom(QRViewModel::class.java)) {
                return QRViewModel(name, lastName) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}