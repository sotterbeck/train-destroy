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
class EnableScheduleInteractorImplTest {

    @RelaxedMockK
    lateinit var schedule: DestroyScheduleManager

    @Test
    fun `should not enable schedule when its already active`() {
        every { schedule.isScheduleActive } returns true

        val enableSchedule = EnableScheduleInteractorImpl(schedule)
        val response = runBlocking { enableSchedule() }

        verify(exactly = 0) { schedule.enable() }
        assertAll {
            assertThat(response).isInstanceOf<GenericResponseModel.Failure>()
            assertThat(response.messageKey).isEqualTo("schedule.alreadyActive")
        }
    }

    @Test
    fun `should enable schedule when it was disabled`() {
        every { schedule.isScheduleActive } returns false

        val enableSchedule = EnableScheduleInteractorImpl(schedule)
        val response = runBlocking { enableSchedule() }

        verify() { schedule.enable() }
        assertAll {
            assertThat(response).isInstanceOf<GenericResponseModel.Success>()
            assertThat(response.messageKey).isEqualTo("schedule.hasBeenEnabled")
        }
    }
}