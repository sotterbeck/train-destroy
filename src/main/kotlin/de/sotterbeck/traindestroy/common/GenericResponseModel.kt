package de.sotterbeck.traindestroy.common

sealed interface GenericResponseModel {
    val messageKey: String

    data class Success(override val messageKey: String = "genericError") : GenericResponseModel

    data class Failure(override val messageKey: String = "genericSuccess") : GenericResponseModel
}