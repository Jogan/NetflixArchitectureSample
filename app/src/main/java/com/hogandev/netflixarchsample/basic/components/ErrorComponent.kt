package com.hogandev.netflixarchsample.basic.components

import android.annotation.SuppressLint
import android.support.annotation.VisibleForTesting
import android.view.ViewGroup
import com.hogandev.netflixarchsample.base.EventBusFactory
import com.hogandev.netflixarchsample.base.UIComponent
import com.hogandev.netflixarchsample.basic.components.uiViews.ErrorView
import com.hogandev.netflixarchsample.basic.events.ScreenStateEvent
import com.hogandev.netflixarchsample.basic.events.UserInteractionEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

@SuppressLint("CheckResult")
open class ErrorComponent(container: ViewGroup, private val bus: EventBusFactory) : UIComponent<UserInteractionEvent> {
    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): ReceiveChannel<UserInteractionEvent> {
        return bus.getSafeManagedChannel(UserInteractionEvent::class.java)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container, bus)

    open fun initView(container: ViewGroup, bus: EventBusFactory): ErrorView {
        return ErrorView(container, bus)
    }

    init {
        val subscription = bus.getSafeManagedChannel(ScreenStateEvent::class.java)
        CoroutineScope(Dispatchers.Main)
            .launch {
                subscription.consumeEach {
                    when (it) {
                        ScreenStateEvent.Loading -> {
                            uiView.hide()
                        }
                        ScreenStateEvent.Loaded -> {
                            uiView.hide()
                        }
                        ScreenStateEvent.Error -> {
                            uiView.show()
                        }
                    }
                }
            }
    }
}
