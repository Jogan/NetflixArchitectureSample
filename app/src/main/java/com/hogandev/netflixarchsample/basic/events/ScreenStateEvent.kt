package com.hogandev.netflixarchsample.basic.events

import com.hogandev.netflixarchsample.base.ComponentEvent

/**
 * List of all events this Screen can emit
 */
sealed class ScreenStateEvent : ComponentEvent() {
    object Loading : ScreenStateEvent()
    object Loaded : ScreenStateEvent()
    object Error : ScreenStateEvent()
}