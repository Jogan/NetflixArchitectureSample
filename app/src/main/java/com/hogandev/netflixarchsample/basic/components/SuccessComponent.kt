package com.hogandev.netflixarchsample.basic.components

import android.annotation.SuppressLint
import android.support.annotation.VisibleForTesting
import android.view.ViewGroup
import com.hogandev.netflixarchsample.base.EventBusFactory
import com.hogandev.netflixarchsample.base.UIComponent
import com.hogandev.netflixarchsample.basic.components.uiViews.SuccessView
import com.hogandev.netflixarchsample.basic.events.ScreenStateEvent
import com.hogandev.netflixarchsample.util.CoroutineContextProvider

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

@SuppressLint("CheckResult")
open class SuccessComponent(container: ViewGroup,
                            bus: EventBusFactory,
                            contextPool: CoroutineContextProvider = CoroutineContextProvider())
    : UIComponent<Unit> {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container, bus)

    open fun initView(container: ViewGroup, bus: EventBusFactory): SuccessView {
        return SuccessView(container, bus)
    }

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): ReceiveChannel<Unit> {
        return BroadcastChannel<Unit>(1).openSubscription() // FIXME create empty channel
    }


    init {
        val subscription = bus.getSafeManagedChannel(ScreenStateEvent::class.java)
        CoroutineScope(contextPool.Main)
                .launch {
                    subscription.consumeEach {
                        when (it) {
                            ScreenStateEvent.Loading -> {
                                uiView.hide()
                            }
                            ScreenStateEvent.Loaded -> {
                                uiView.show()
                            }
                            ScreenStateEvent.Error -> {
                                uiView.hide()
                            }
                        }
                    }
                }
    }
}