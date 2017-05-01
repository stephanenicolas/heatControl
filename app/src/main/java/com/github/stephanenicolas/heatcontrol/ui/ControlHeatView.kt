package com.github.stephanenicolas.heatcontrol.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.github.stephanenicolas.heatcontrol.R
import javax.inject.Inject

class ControlHeatView : FrameLayout {

    @BindView(R.id.message)
    lateinit var messageView: TextView
    @Inject
    lateinit var controlHeatPresenter: ControlHeatPresenter

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.control_heat_view, this)
        ButterKnife.bind(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        controlHeatPresenter.attach(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        controlHeatPresenter.detach()
    }

    fun setTargetTemp(targetTemp: Float) {
        messageView!!.text = "$targetTemp C"
    }

    fun setAmbientTemp(ambientTemp: Float) {
    }

    fun setRefreshing(isRefreshing: Boolean) {
    }
}