package co.nimblehq.testutil

import co.nimblehq.data.lib.schedulers.RxScheduler
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider

object MockRxSchedulersProvider : RxSchedulerProvider {
    override fun io() = RxScheduler.MainThread

    override fun computation() = RxScheduler.MainThread

    override fun main() = RxScheduler.MainThread
}
