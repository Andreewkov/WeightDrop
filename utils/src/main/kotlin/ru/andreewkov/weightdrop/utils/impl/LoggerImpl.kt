package ru.andreewkov.weightdrop.utils.impl

import android.util.Log
import ru.andreewkov.weightdrop.utils.api.Logger

internal class LoggerImpl(private val tag: String) : Logger {

    override fun e(message: String, throwable: Throwable) {
        Log.e("WeightDrop/Log $tag", "$message: $throwable")
    }
}
