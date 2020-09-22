package com.easy.demo.ui.kotlin

import android.view.View
import androidx.databinding.ObservableField
import com.easy.demo.ui.mvvm.binding.BindingCommand
import com.easy.demo.ui.mvvm.binding.BindingConsumer
import com.easy.framework.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


class TestKotlinViewModel : BaseViewModel(), CoroutineScope by CoroutineScope(Dispatchers.IO) {

    suspend fun loadDataAsync(): String {
        return withContext(Dispatchers.Default) {
            delay(3000)
            " execute from thread ${Thread.currentThread().name}"
        }
    }

    var userName = ObservableField("")

    var onTextChangedCommand = BindingCommand(
        BindingConsumer<String> { view: View, result: String? ->
            userName.set(result)
        }
    )
}