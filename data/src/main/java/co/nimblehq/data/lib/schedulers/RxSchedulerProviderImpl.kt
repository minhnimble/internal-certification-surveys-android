package co.nimblehq.data.lib.schedulers

class RxSchedulerProviderImpl : RxSchedulerProvider {
    override fun io(): RxScheduler = RxScheduler.IoThread

    override fun computation(): RxScheduler = RxScheduler.ComputationThread

    override fun main(): RxScheduler = RxScheduler.MainThread
}
