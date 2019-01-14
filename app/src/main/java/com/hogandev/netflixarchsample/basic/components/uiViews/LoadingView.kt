package com.hogandev.netflixarchsample.basic.components.uiViews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hogandev.netflixarchsample.R
import com.hogandev.netflixarchsample.base.UIView
import com.hogandev.netflixarchsample.basic.events.UserInteractionEvent

class LoadingView(container: ViewGroup) : UIView<UserInteractionEvent>(container) {
    private val view: View =
        LayoutInflater.from(container.context).inflate(R.layout.loading, container, true)
            .findViewById(R.id.loadingSpinner)

    override val containerId: Int = view.id

    override fun show() {
        view.visibility = View.VISIBLE
    }

    override fun hide() {
        view.visibility = View.GONE
    }
}
