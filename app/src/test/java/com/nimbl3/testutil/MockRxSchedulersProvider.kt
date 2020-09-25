package com.nimbl3.testutil

import com.nimbl3.data.lib.schedulers.RxScheduler
import com.nimbl3.data.lib.schedulers.RxSchedulerProvider

object MockRxSchedulersProvider : RxSchedulerProvider {
    override fun io() = RxScheduler.IoThread

    override fun computation() = RxScheduler.ComputationThread

    override fun main() = RxScheduler.MainThread
}