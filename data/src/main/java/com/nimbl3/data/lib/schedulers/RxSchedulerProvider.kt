package com.nimbl3.data.lib.schedulers

interface RxSchedulerProvider {
    fun io(): RxScheduler

    fun computation(): RxScheduler

    fun main(): RxScheduler
}
