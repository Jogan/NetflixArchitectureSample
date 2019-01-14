package com.hogandev.netflixarchsample.base

import kotlinx.coroutines.channels.ReceiveChannel


interface UIComponent<T> {
    fun getContainerId(): Int
    fun getUserInteractionEvents(): ReceiveChannel<T>
}