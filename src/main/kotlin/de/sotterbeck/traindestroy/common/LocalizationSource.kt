package de.sotterbeck.traindestroy.common

import java.util.*

interface LocalizationSource {

    fun getString(key: String, locale: Locale): String

}
