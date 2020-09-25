package com.coolxtc.common.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.jeremyliao.liveeventbus.LiveEventBus
import com.jeremyliao.liveeventbus.core.Observable

inline fun <reified EVENT> eventObservable(): Observable<EVENT> {
    return LiveEventBus.get(EVENT::class.java.simpleName, EVENT::class.java)
}

inline fun <reified EVENT> postEvent(event: EVENT) {
    LiveEventBus.get(EVENT::class.java.simpleName).post(event)
}

inline fun <reified EVENT> postEventDelay(tag: String, event: EVENT, delay: Long) {
    LiveEventBus.get(tag).postDelay(event, delay)
}

inline fun <reified EVENT> AppCompatActivity.observeEvent(
        noinline observer: (EVENT) -> Unit
) {
    val o = Observer<EVENT> {
        observer(it)
    }
    eventObservable<EVENT>().observe(this, o)
}


inline fun <reified EVENT> AppCompatActivity.observeEventSticky(
        noinline observer: (EVENT) -> Unit
) {
    val o = Observer<EVENT> {
        observer(it)
    }
    eventObservable<EVENT>().observeSticky(this, o)
}

inline fun <reified EVENT> Fragment.observeEvent(
        noinline observer: (EVENT) -> Unit
) {
    val o = Observer<EVENT> {
        observer(it)
    }
    eventObservable<EVENT>().observe(this, o)
}

inline fun <reified EVENT> Fragment.observeEventSticky(
        noinline observer: (EVENT) -> Unit
) {
    val o = Observer<EVENT> {
        observer(it)
    }
    eventObservable<EVENT>().observeSticky(this, o)
}