package com.github.stephanenicolas.heatcontrol

import android.app.Application
import toothpick.Scope
import toothpick.Toothpick
import toothpick.configuration.Configuration
import toothpick.smoothie.module.SmoothieApplicationModule

class HeatControlApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Toothpick.setConfiguration(Configuration.forDevelopment().enableReflection())
        val appScope: Scope = Toothpick.openScope(this)

        appScope.installModules(SmoothieApplicationModule(this))
    }
}