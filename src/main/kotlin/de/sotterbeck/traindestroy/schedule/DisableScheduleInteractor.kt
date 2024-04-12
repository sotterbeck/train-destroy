package de.sotterbeck.traindestroy.schedule

import de.sotterbeck.traindestroy.common.GenericResponseModel

interface DisableScheduleInteractor {

    suspend operator fun invoke(): GenericResponseModel
}