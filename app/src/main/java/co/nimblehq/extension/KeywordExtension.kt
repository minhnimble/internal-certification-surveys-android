package co.nimblehq.extension

import co.nimblehq.lib.EmptyCallback

/**
 * Provide syntactic sugar extensions that help the code flow cleaner and more readable
 */

inline fun unless(condition: Boolean, block: EmptyCallback) {
    if(!condition) block()
}
