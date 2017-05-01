package com.github.stephanenicolas.heatcontrol.usecases

open class Action
class SetTargetTempAction(var temp:Float): Action()
class GetTargetTempAction: Action()
class GetAmbientAction: Action()
