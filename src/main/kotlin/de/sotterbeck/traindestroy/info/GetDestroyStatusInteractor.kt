package de.sotterbeck.traindestroy.info

import de.sotterbeck.traindestroy.common.LanguageAwareRequest
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

fun interface GetDestroyStatusInteractor {

    suspend operator fun invoke(request: RequestModel): ResponseModel

    data class RequestModel(
        override val locale: Locale,
        val remainingToNextDestroy: Duration? = null,
        val nextDestroy: LocalDateTime? = null,
        val lastDestroy: LocalDateTime? = null,
        val tps: Double,
        val tpsThreshold: Double,
        val isDisabled: Boolean
    ) : LanguageAwareRequest

    sealed interface ResponseModel {

        val lastDestroy: String

        val performance: String

        val status: String

        data class Enabled(
            override val lastDestroy: String,
            override val performance: String,
            override val status: String,
            val nextDestroy: String,
            val remainingToNextDestroy: String,
        ) : ResponseModel

        data class Disabled(
            override val lastDestroy: String,
            override val performance: String,
            override val status: String,
        ) : ResponseModel

        data object Error : ResponseModel {

            override val lastDestroy: String get() = "N/A"
            override val performance: String get() = "N/A"
            override val status: String get() = "N/A"

        }
    }
}