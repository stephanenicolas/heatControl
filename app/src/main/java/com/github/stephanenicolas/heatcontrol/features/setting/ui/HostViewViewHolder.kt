package com.github.stephanenicolas.heatcontrol.features.setting.ui

import android.content.Context
import android.support.v7.widget.RecyclerView.ViewHolder
import android.widget.Button

class HostViewViewHolder(var context: Context) : ViewHolder(HostView(context)) {

    var hostView: HostView

    init {
        hostView = itemView as HostView
    }
}