package com.github.stephanenicolas.heatcontrol.features.setting.ui

import android.content.Context
import android.support.v7.widget.RecyclerView.ViewHolder
import android.widget.Button
import com.github.stephanenicolas.heatcontrol.R

class AddNewHostViewHolder(context: Context) : ViewHolder(Button(context)) {

    var button: Button

    init {
        button = itemView as Button
        button.setText(R.string.add_host)
    }
}