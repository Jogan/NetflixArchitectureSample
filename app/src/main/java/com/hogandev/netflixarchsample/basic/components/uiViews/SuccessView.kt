package com.hogandev.netflixarchsample.basic.components.uiViews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hogandev.netflixarchsample.R
import com.hogandev.netflixarchsample.base.EventBusFactory
import com.hogandev.netflixarchsample.base.UIView
import com.hogandev.netflixarchsample.basic.events.UserInteractionEvent

class SuccessView(container: ViewGroup, eventBusFactory: EventBusFactory) :
    UIView<UserInteractionEvent>(container) {
    private val view: View =
        LayoutInflater.from(container.context).inflate(R.layout.success, container, true)
            .findViewById(R.id.success_tv)

    override val containerId: Int = view.id

    override fun show() {
        view.visibility = View.VISIBLE
    }

    override fun hide() {
        view.visibility = View.GONE
    }
}
