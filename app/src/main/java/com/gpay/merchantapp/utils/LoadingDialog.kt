package com.gpay.merchantapp.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Circle
import com.gpay.merchantapp.databinding.LoadingDialogNewBinding

class LoadingDialog(context: Context) : Dialog(context){

    private lateinit var binding: LoadingDialogNewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        binding = LoadingDialogNewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.progressBarNew.isIndeterminate = true
        setCancelable(false)
        setCanceledOnTouchOutside(false)

        val cubeGrid: Sprite = Circle()
        binding.progressBarNew.setIndeterminateDrawable(cubeGrid)

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}