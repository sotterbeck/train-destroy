package de.sotterbeck.traindestroy.schedule

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import de.sotterbeck.traindestroy.common.GenericResponseModel
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class DisableScheduleInteractorImplTest {

    @RelaxedMockK
    lateinit var schedule: DestroyScheduleManager

    @Test
    fun `should not disabled schedule when its disabled`() {
        every { schedule.isScheduleActive } returns false

        val disableSchedule = DisableScheduleInteractorImpl(schedule)
        val response = runBlocking { disableSchedule() }

        verify(exactly = 0) { schedule.disable() }
        assertAll {
            assertThat(response).isInstanceOf<GenericResponseModel.Failure>()
            assertThat(response.messageKey).isEqualTo("schedule.alreadyInactive")
        }
    }

    @Test
    fun `should disabled schedule when it was active`() {
        every { schedule.isScheduleActive } returns true

        val disableSchedule = DisableScheduleInteractorImpl(schedule)
        val response = runBlocking { disableSchedule() }

        verify() { schedule.disable() }
        assertAll {
            assertThat(response).isInstanceOf<GenericResponseModel.Success>()
            assertThat(response.messageKey).isEqualTo("schedule.hasBeenDisabled")
        }
    }
}