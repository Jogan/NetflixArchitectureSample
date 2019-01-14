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

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.support.annotation.VisibleForTesting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * It implements a Factory pattern generating Rx Subjects based on Event Types.
 * It maintain a map of Rx Subjects, one per type per instance of EventBusFactory.
 *
 * @param owner is a LifecycleOwner used to auto dispose based on destroy observable
 */
class EventBusFactory private constructor(val owner: LifecycleOwner) {

    companion object {

        private val buses = mutableMapOf<LifecycleOwner, EventBusFactory>()

        /**
         * Return the [EventBusFactory] associated to the [LifecycleOwner]. It there is no bus it will create one.
         * If the [LifecycleOwner] used is a fragment it use [Fragment#getViewLifecycleOwner()]
         */
        @JvmStatic
        fun get(lifecycleOwner: LifecycleOwner): EventBusFactory {
            return with(lifecycleOwner) {
                var bus = buses[lifecycleOwner]
                if (bus == null) {
                    bus = EventBusFactory(lifecycleOwner)
                    buses[lifecycleOwner] = bus
                    // LifecycleOwner
                    lifecycleOwner.lifecycle.addObserver(bus.observer)
                }
                bus
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val map = HashMap<Class<*>, BroadcastChannel<*>>()

    internal val observer = object : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            map.forEach { entry -> entry.value.cancel() }
            buses.remove(owner)
        }
    }

    fun <T> create(clazz: Class<T>): BroadcastChannel<T> {
        val subject = BroadcastChannel<T>(1)
        map[clazz] = subject
        return subject
    }

    /**
     * emit will create (if needed) or use the existing Rx Subject to send events.
     *
     * @param clazz is the Event Class
     * @param event is the instance of the Event to be sent
     */
    fun <T : ComponentEvent> emit(clazz: Class<T>, event: T, context: CoroutineContext = Dispatchers.Default) {
        val channel = if (map[clazz] != null) map[clazz] else create(clazz)
        CoroutineScope(context).launch {
            (channel as BroadcastChannel<T>).send(event)
        }
    }

    /**x
     * getSafeManagedChannel returns an Rx Observable which is
     *  *Safe* against reentrant events as it is serialized and
     *  *Managed* since it disposes itself based on the lifecycle
     *
     *  @param clazz is the class of the event type used by this observable
     */
    fun <T : ComponentEvent> getSafeManagedChannel(clazz: Class<T>): ReceiveChannel<T> {
        val channel = if (map[clazz] != null) map[clazz]  else create(clazz)
        return (channel as BroadcastChannel<T>).openSubscription()
    }
}

/**
 * Extension on [LifecycleOwner] used to emit an event.
 */
inline fun <reified T : ComponentEvent> LifecycleOwner.emit(event: T) =
    with(EventBusFactory.get(this)) {
        getSafeManagedChannel(T::class.java)
        emit(T::class.java, event)
    }


