package de.sotterbeck.traindestroy.info

import assertk.assertThat
import assertk.assertions.isEqualTo
import de.sotterbeck.traindestroy.common.LocalizationSource
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockKExtension::class)
class GetDestroyStatusInteractorImplTest {

    @MockK
    private lateinit var localizationSource: LocalizationSource

    private val defaultLocale = Locale.US

    @BeforeEach
    fun setUp() {
        every { localizationSource.getString(eq("status.enabled"), any()) } returns "enabled"
        every { localizationSource.getString(eq("status.disabled"), any()) } returns "disabled"
        every { localizationSource.getString(eq("performance.poor"), any()) } returns "poor"
        every { localizationSource.getString(eq("performance.good"), any()) } returns "good"
    }


    private val anyStatus = true

    @Nested
    inner class DisabledState {

        @Test
        fun `should format status`() {
            val request = GetDestroyStatusInteractor.RequestModel(
                lastDestroy = LocalDateTime.of(2024, 1, 1, 1, 1),
                tps = 20.0,
                tpsThreshold = 17.0,
                isDisabled = true,
                locale = defaultLocale
            )

            val getInfo = GetDestroyStatusInteractorImpl(localizationSource)
            val formattedLastDestroy = runBlocking { getInfo(request).status }

            assertThat(formattedLastDestroy).isEqualTo("disabled")
        }

    }

    @Nested
    inner class EnabledState {

        @Test
        fun `should format next destroy`() {
            val request = GetDestroyStatusInteractor.RequestModel(
                lastDestroy = LocalDateTime.of(2024, 1, 1, 1, 1),
                nextDestroy = LocalDateTime.of(2024, 1, 1, 1, 15),
                remainingToNextDestroy = Duration.ofMinutes(14),
                tps = 20.0,
                tpsThreshold = 17.0,
                isDisabled = false,
                locale = Locale.GERMANY
            )

            val getInfo = GetDestroyStatusInteractorImpl(localizationSource)
            val response = runBlocking { getInfo(request) }
            val nextDestroy = when (response) {
                is GetDestroyStatusInteractor.ResponseModel.Enabled -> response.nextDestroy
                else -> error("Unexpected response type: ${response::class.simpleName}")
            }

            assertThat(nextDestroy).isEqualTo("01.01.24, 01:15")
        }

    }

    @Test
    fun `should format last destroy`() {
        val request = GetDestroyStatusInteractor.RequestModel(
            lastDestroy = LocalDateTime.of(2024, 1, 1, 1, 1),
            tps = 20.0,
            tpsThreshold = 17.0,
            isDisabled = anyStatus,
            locale = Locale.GERMANY
        )

        val getInfo = GetDestroyStatusInteractorImpl(localizationSource)
        val formattedLastDestroy = runBlocking { getInfo(request).lastDestroy }

        assertThat(formattedLastDestroy).isEqualTo("01.01.24, 01:01")
    }

    @Test
    fun `should respond with good performance level when tps is over threshold`() {
        val request = GetDestroyStatusInteractor.RequestModel(
            lastDestroy = LocalDateTime.of(2024, 1, 1, 1, 1),
            tps = 20.0,
            tpsThreshold = 17.0,
            isDisabled = anyStatus,
            locale = defaultLocale
        )

        val getInfo = GetDestroyStatusInteractorImpl(localizationSource)
        val formattedLastDestroy = runBlocking { getInfo(request).performance }

        assertThat(formattedLastDestroy).isEqualTo("good")
    }

    @Test
    fun `should respond with poor performance level when tps is under threshold`() {
        val request = GetDestroyStatusInteractor.RequestModel(
            lastDestroy = LocalDateTime.of(2024, 1, 1, 1, 1),
            tps = 16.9,
            tpsThreshold = 17.0,
            isDisabled = anyStatus,
            locale = defaultLocale
        )

        val getInfo = GetDestroyStatusInteractorImpl(localizationSource)
        val formattedLastDestroy = runBlocking { getInfo(request).performance }

        assertThat(formattedLastDestroy).isEqualTo("poor")
    }
}