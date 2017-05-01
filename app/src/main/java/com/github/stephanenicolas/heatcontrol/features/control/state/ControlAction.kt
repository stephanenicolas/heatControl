package com.github.stephanenicolas.heatcontrol.features.control.state

open class ControlAction
class SetTargetTempControlAction(var targetTemp:Float): ControlAction()
class RefreshTemperaturesControlAction(var ambientTemp:Float, var targetTemp:Float): ControlAction()
class ErrorControlAction(var throwable:Throwable): ControlAction()