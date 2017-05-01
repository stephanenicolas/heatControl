package com.github.stephanenicolas.heatcontrol.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.github.stephanenicolas.heatcontrol.R

class ControlHeatView: FrameLayout {

    @BindView(R.id.message)
    lateinit var messageView: TextView

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.control_heat_view, this)
        ButterKnife.bind(this)
    }
}