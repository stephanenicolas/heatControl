package com.github.stephanenicolas.heatcontrol

import android.app.Application
import toothpick.Toothpick
import toothpick.configuration.Configuration

class HeatControlApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Toothpick.setConfiguration(Configuration.forDevelopment().enableReflection())
    }
}