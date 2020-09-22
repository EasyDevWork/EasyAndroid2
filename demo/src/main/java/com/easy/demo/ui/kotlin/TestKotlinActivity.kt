package com.easy.demo.ui.kotlin

import android.util.Log
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.easy.demo.BR
import com.easy.demo.R
import com.easy.demo.databinding.TestKotlinBinding
import com.easy.framework.base.BaseMvvmActivity
import kotlinx.coroutines.*

@Route(path = "/demo/TestKotlinActivity", name = "test kotlin")
class TestKotlinActivity : BaseMvvmActivity<TestKotlinViewModel, TestKotlinBinding>(),
    CoroutineScope by CoroutineScope(Dispatchers.Main) {
    @Autowired
    @JvmField
    var age: Int = 0

    override fun getLayoutId(): Int = R.layout.test_kotlin

    override fun initVariableId(): Int = BR.testKotlinViewModel

    override fun initView() {
        ARouter.getInstance().inject(this)
        Log.d("TestKotlinActivity", "age = " + age);
    }

    fun clickMe(view: View) {
        println("start from thread ${Thread.currentThread().name}")
        launch {
            viewBind.progressBar3.visibility = View.VISIBLE
            val result = viewModel.loadDataAsync()
            println(result)
            viewBind.tvTest.text = result
            viewBind.progressBar3.visibility = View.GONE
        }
        println("end from thread ${Thread.currentThread().name}")
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}