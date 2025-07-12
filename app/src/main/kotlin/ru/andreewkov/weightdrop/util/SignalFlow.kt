package ru.andreewkov.weightdrop.util

class SignalFlow : ShareHiddenFlow<Signal>() {

    fun signal() {
        update(Signal())
    }
}

class Signal