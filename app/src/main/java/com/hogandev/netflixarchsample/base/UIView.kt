/*
 * Copyright (C) 2018 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by Juliano Moraes, Rohan Dhruva, Emmanuel Boudrant.
 */
package com.hogandev.netflixarchsample.base

import android.support.annotation.IdRes
import android.view.ViewGroup

abstract class UIView<T>(val container: ViewGroup) {
    /**
     * Get the XML id for the IUIView
     */
    @get:IdRes
    abstract val containerId: Int

    /**
     * Show the IUIView
     */
    abstract fun show()

    /**
     * Hide the IUIView
     */
    abstract fun hide()
}