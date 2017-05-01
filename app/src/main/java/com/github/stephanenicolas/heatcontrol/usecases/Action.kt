package com.github.stephanenicolas.heatcontrol.usecases

open class Action
class SetTargetTempAction(var temp:Float): Action()
class RefreshAmbientTemperatureAction(var temp:Float): Action()
