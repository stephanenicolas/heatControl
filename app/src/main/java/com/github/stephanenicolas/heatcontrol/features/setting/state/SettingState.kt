package com.github.stephanenicolas.heatcontrol.features.control.state

const val MOCK = "Mock"
class SettingState(var key:String? = null, var hostList:ArrayList<Host> = ArrayList())
class Host(var name:String, var address:String, var isSelected:Boolean = false)
