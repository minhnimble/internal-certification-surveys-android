package co.nimblehq.data.api.common.listener

interface AppRefreshTokenFailedListener {

    fun onRefreshTokenFailed(error: Throwable?)
}

object AppTokenExpiredNotifier {

    private val appRefreshTokenFailedListeners:
        MutableList<AppRefreshTokenFailedListener> = mutableListOf()

    fun bindAppTokenExpiredListener(listener: AppRefreshTokenFailedListener) {
        appRefreshTokenFailedListeners.add(listener)
    }

    fun unbindAppTokenExpiredListener(listener: AppRefreshTokenFailedListener) {
        appRefreshTokenFailedListeners.remove(listener)
    }

    @Synchronized
    internal fun requestTokenFailed(error: Throwable?) {
        val iterator = appRefreshTokenFailedListeners.iterator()
        while (iterator.hasNext()) {
            iterator.next().onRefreshTokenFailed(error)
        }
    }
}
