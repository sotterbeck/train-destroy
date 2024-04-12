package de.sotterbeck.traindestroy.common

import java.util.*

interface LanguageAwareRequest {

    val locale: Locale
        get() = Locale.getDefault()
}