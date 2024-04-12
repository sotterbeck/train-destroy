package de.sotterbeck.traindestroy.common

import java.util.*

class ResourceBundleLocalizationSource(private val baseName: String) : LocalizationSource {

    override fun getString(key: String, locale: Locale): String {
        val resourceBundle = ResourceBundle.getBundle(baseName, locale)
        return resourceBundle.getString(key)
    }
}