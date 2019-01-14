package com.hogandev.netflixarchsample.basic.components.uiViews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.hogandev.netflixarchsample.R
import com.hogandev.netflixarchsample.base.EventBusFactory
import com.hogandev.netflixarchsample.base.UIView
import com.hogandev.netflixarchsample.basic.events.UserInteractionEvent

class ErrorView(container: ViewGroup, eventBusFactory: EventBusFactory) :
    UIView<UserInteractionEvent>(container) {
    private val view: View =
        LayoutInflater.from(container.context).inflate(R.layout.error, container, true)
            .findViewById(R.id.error_container)

    override val containerId: Int = 0

    init {
        view.findViewById<Button>(R.id.button)
            .setOnClickListener {
                eventBusFactory.emit(
                    UserInteractionEvent::class.java,
                    UserInteractionEvent.IntentTapRetry
                )
            }
    }

    override fun show() {
        view.visibility = View.VISIBLE
    }

    override fun hide() {
        view.visibility = View.GONE
    }
}