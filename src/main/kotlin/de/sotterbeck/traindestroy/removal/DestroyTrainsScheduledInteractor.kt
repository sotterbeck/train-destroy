package de.sotterbeck.traindestroy.removal

fun interface DestroyTrainsScheduledInteractor {

    operator fun invoke(destroyCallback: (ResponseModel) -> Unit)

    sealed interface ResponseModel {
        data class Success(val destroyedMinecarts: Int) : ResponseModel
        data object Idle : ResponseModel
    }

}