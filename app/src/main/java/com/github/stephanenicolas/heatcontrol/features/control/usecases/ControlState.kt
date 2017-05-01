package com.github.stephanenicolas.heatcontrol.features.control.usecases

class ControlState(var ambiantTemp: Float = 0f, var targetTemp: Float = 0f, var throwable: Throwable? = null)
