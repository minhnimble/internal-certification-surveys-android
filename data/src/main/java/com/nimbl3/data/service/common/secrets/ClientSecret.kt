package com.nimbl3.data.service.common.secrets

interface ClientSecret {
    companion object {
        const val Secret = ""
    }
    val value: String
}