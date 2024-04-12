package de.sotterbeck.traindestroy.schedule

import de.sotterbeck.traindestroy.common.GenericResponseModel

class EnableScheduleInteractorImpl(
    private val schedule: DestroyScheduleManager
) : EnableScheduleInteractor {

    override suspend fun invoke(): GenericResponseModel {
        if (schedule.isScheduleActive) {
            return GenericResponseModel.Failure("schedule.alreadyActive")
        }
        schedule.enable()
        return GenericResponseModel.Success("schedule.hasBeenEnabled")
    }

}