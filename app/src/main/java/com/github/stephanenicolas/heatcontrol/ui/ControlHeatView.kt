package com.github.stephanenicolas.heatcontrol.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.github.stephanenicolas.heatcontrol.R
import com.github.stephanenicolas.heatcontrol.usecases.ControlHeatState
import javax.inject.Inject

class ControlHeatView : RelativeLayout {

    @BindView(R.id.message)
    lateinit var messageView: TextView
    @BindView(R.id.button_refresh)
    lateinit var buttonRefresh: Button
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
        buttonRefresh.setOnClickListener { controlHeatPresenter.refresh() }
    }

    override fun onDetachedFromWindow() {
        controlHeatPresenter.detach()
        super.onDetachedFromWindow()
    }

    fun setControlHeatState(controlHeatState: ControlHeatState) {
        messageView.text = "${controlHeatState.targetTemp} C"
    }
}