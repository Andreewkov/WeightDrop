package ru.andreewkov.weightdrop.utils.api

interface LoggerProvider {

    fun get(tag: String): Logger
}
