package co.nimblehq.data.lib.schedulers

class TestRxSchedulerProviderImpl : RxSchedulerProvider {
    override fun io(): RxScheduler = RxScheduler.TestIoThread

    override fun computation(): RxScheduler = RxScheduler.TestComputationThread

    override fun main(): RxScheduler = RxScheduler.TestMainThread
}
