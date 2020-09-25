package com.nimbl3.data.errors

sealed class NavigationError(
    cause: Throwable?
) : AppError(cause) {

    class UnsupportedNavigationError(
        currentGraph: String?,
        currentDestination: String?
    ) : NavigationError(
        RuntimeException("Unsupported navigation on $currentGraph at $currentDestination")
    )
}
