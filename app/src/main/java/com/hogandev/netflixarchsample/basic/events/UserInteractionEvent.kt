package com.hogandev.netflixarchsample.basic.events

import com.hogandev.netflixarchsample.base.ComponentEvent

/**
 * List of all events Views can emit
 */
sealed class UserInteractionEvent : ComponentEvent() {
    object IntentTapRetry : UserInteractionEvent()
}