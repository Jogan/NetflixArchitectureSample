package com.hogandev.netflixarchsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.hogandev.netflixarchsample.base.EventBusFactory
import com.hogandev.netflixarchsample.basic.components.ErrorComponent
import com.hogandev.netflixarchsample.basic.components.LoadingComponent
import com.hogandev.netflixarchsample.basic.components.SuccessComponent
import com.hogandev.netflixarchsample.basic.events.ScreenStateEvent
import com.hogandev.netflixarchsample.basic.events.UserInteractionEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initComponents(findViewById(R.id.root))

        startSimulation()
    }

    private fun initComponents(rootViewContainer: ViewGroup) {

        LoadingComponent(rootViewContainer, EventBusFactory.get(this))

        // If the UI Component emits Interaction Events it can be observed like this
        val errorComponent = ErrorComponent(rootViewContainer, EventBusFactory.get(this))
        val errorSubscription = errorComponent.getUserInteractionEvents()
        CoroutineScope(Dispatchers.Main)
            .launch {
                errorSubscription.consumeEach {
                    when (it) {
                        UserInteractionEvent.IntentTapRetry -> {
                            startSimulation()
                        }
                    }
                }
            }

        SuccessComponent(rootViewContainer, EventBusFactory.get(this))
    }

    /**
     * Start a simulation emitting events every 2 seconds
     */
    private fun startSimulation() {
        val lifecycleOwner = this
        CoroutineScope(Dispatchers.IO)
            .launch {
                EventBusFactory.get(lifecycleOwner).emit(ScreenStateEvent::class.java, ScreenStateEvent.Loading)
                delay(2000)
                EventBusFactory.get(lifecycleOwner).emit(ScreenStateEvent::class.java, ScreenStateEvent.Loaded)
                delay(2000)
                EventBusFactory.get(lifecycleOwner).emit(ScreenStateEvent::class.java, ScreenStateEvent.Error)

            }
    }
}