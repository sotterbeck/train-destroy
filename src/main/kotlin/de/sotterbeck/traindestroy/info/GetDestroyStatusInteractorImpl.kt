package de.sotterbeck.traindestroy.info

import de.sotterbeck.traindestroy.common.LocalizationSource
import de.sotterbeck.traindestroy.common.humanReadableString
import de.sotterbeck.traindestroy.info.GetDestroyStatusInteractor.ResponseModel
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

internal class GetDestroyStatusInteractorImpl(
    private val localizationSource: LocalizationSource
) : GetDestroyStatusInteractor {

    private val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)

    override suspend fun invoke(request: GetDestroyStatusInteractor.RequestModel): ResponseModel {
        val lastDestroy = request.lastDestroy?.format(formatter.withLocale(request.locale)) ?: "N/A"

        return when {
            request.isDisabled -> ResponseModel.Disabled(
                lastDestroy = lastDestroy,
                status = localizationSource.getString("status.disabled", request.locale),
                performance = getPerformanceString(request)
            )

            request.nextDestroy != null && request.remainingToNextDestroy != null -> ResponseModel.Enabled(
                lastDestroy = lastDestroy,
                status = localizationSource.getString("status.enabled", request.locale),
                performance = getPerformanceString(request),
                nextDestroy = request.nextDestroy.format(formatter.withLocale(request.locale)),
                remainingToNextDestroy = request.remainingToNextDestroy.humanReadableString
            )

            else -> ResponseModel.Error
        }
    }

    private fun getPerformanceString(request: GetDestroyStatusInteractor.RequestModel) =
        if (request.tps >= request.tpsThreshold)
            localizationSource.getString(
                "performance.good",
                request.locale
            ) else localizationSource.getString("performance.poor", request.locale)
}