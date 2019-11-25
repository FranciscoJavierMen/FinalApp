package com.example.finalapp.utils

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

object RxBus {
    private val publisher = PublishSubject.create<Any>()

    private fun publish(event: Any) {
        publisher.onNext(event)
    }

    private fun <T> listen(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)
}