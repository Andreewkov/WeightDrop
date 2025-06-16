package ru.andreewkov.weightdrop.ui.screen

interface AppActionHandler {

    fun handleAction(action: AppAction)
}

val appActionHandlerStub = object : AppActionHandler {
    override fun handleAction(action: AppAction) = Unit
}
