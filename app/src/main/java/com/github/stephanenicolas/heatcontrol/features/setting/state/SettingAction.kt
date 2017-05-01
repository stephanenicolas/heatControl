package com.github.stephanenicolas.heatcontrol.features.control.state

open class SettingAction
class SetKeyAction(var key:String): SettingAction()
class SelectHostAction(var hostIndex:Int): SettingAction()
class RenameHostAction(var hostIndex:Int, var name:String): SettingAction()
class SetHostAction(var hostIndex:Int, var address:String): SettingAction()
class DeleteHostAction(var hostIndex:Int): SettingAction()
class AddHostAction(): SettingAction()
class ResetAction(var settingState: SettingState?): SettingAction()
class SaveAction(): SettingAction()