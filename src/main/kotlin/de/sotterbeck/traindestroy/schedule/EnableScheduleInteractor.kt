package de.sotterbeck.traindestroy.schedule

import de.sotterbeck.traindestroy.common.GenericResponseModel

interface EnableScheduleInteractor {

    suspend operator fun invoke(): GenericResponseModel

}