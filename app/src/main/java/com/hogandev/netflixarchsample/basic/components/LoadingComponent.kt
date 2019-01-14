package com.hogandev.netflixarchsample.basic.components

import android.annotation.SuppressLint
import android.support.annotation.VisibleForTesting
import android.view.ViewGroup
import com.hogandev.netflixarchsample.base.EventBusFactory
import com.hogandev.netflixarchsample.base.UIComponent
import com.hogandev.netflixarchsample.basic.components.uiViews.LoadingView
import com.hogandev.netflixarchsample.basic.events.ScreenStateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

@SuppressLint("CheckResult")
open class LoadingComponent(container: ViewGroup, bus: EventBusFactory) : UIComponent<Unit> {
    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): ReceiveChannel<Unit> {
        return BroadcastChannel<Unit>(1).openSubscription() // FIXME
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container)

    open fun initView(container: ViewGroup): LoadingView {
        return LoadingView(container)
    }

    init {
        val subscription = bus.getSafeManagedChannel(ScreenStateEvent::class.java)
        CoroutineScope(Dispatchers.Main)
            .launch {
                subscription.consumeEach {
                    when (it) {
                        ScreenStateEvent.Loading -> {
                            uiView.show()
                        }
                        ScreenStateEvent.Loaded -> {
                            uiView.hide()
                        }
                        ScreenStateEvent.Error -> {
                            uiView.hide()
                        }
                    }
                }
            }
    }
}
