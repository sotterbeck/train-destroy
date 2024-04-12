package de.sotterbeck.traindestroy.schedule

import de.sotterbeck.traindestroy.common.GenericResponseModel

class DisableScheduleInteractorImpl(
    val schedule: DestroyScheduleManager
) : DisableScheduleInteractor {

    override suspend fun invoke(): GenericResponseModel {
        if (!schedule.isScheduleActive) {
            return GenericResponseModel.Failure("schedule.alreadyInactive")
        }
        schedule.disable()
        return GenericResponseModel.Success("schedule.hasBeenDisabled")
    }

}