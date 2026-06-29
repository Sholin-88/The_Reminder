package com.sholin.the_reminder.alarmManager

import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

class AlarmHelperTest {

    @Before
    fun setup() {
        mockkStatic(LocalDate::class)
        mockkStatic(LocalTime::class)
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `calculateNextOccurrence for later today returns today's timestamp`() {
        // Given today is Monday 2023-10-02
        val today = LocalDate.of(2023, 10, 2) // Monday
        every { LocalDate.now() } returns today
        every { LocalTime.now() } returns LocalTime.of(10, 0)

        // When requesting Monday at 15:00
        val result = AlarmHelper.calculateNextOccurrence(1, LocalTime.of(15, 0))

        // Then result should be today's timestamp at 15:00
        val expected = today.atTime(15, 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        assert(result == expected)
    }

    @Test
    fun `calculateNextOccurrence for earlier today returns next week's timestamp`() {
        // Given today is Monday 2023-10-02
        val today = LocalDate.of(2023, 10, 2) // Monday
        every { LocalDate.now() } returns today
        every { LocalTime.now() } returns LocalTime.of(10, 0)

        // When requesting Monday at 08:00 (already passed)
        val result = AlarmHelper.calculateNextOccurrence(1, LocalTime.of(8, 0))

        // Then result should be next Monday (2023-10-09) at 08:00
        val nextMonday = today.plusWeeks(1)
        val expected = nextMonday.atTime(8, 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        assert(result == expected)
    }

    @Test
    fun `calculateNextOccurrence for different day returns correct timestamp`() {
        // Given today is Monday 2023-10-02
        val today = LocalDate.of(2023, 10, 2)
        every { LocalDate.now() } returns today
        every { LocalTime.now() } returns LocalTime.of(10, 0)

        // When requesting Wednesday (dayId = 3) at 12:00
        val result = AlarmHelper.calculateNextOccurrence(3, LocalTime.of(12, 0))

        // Then result should be Wednesday 2023-10-04 at 12:00
        val wednesday = LocalDate.of(2023, 10, 4)
        val expected = wednesday.atTime(12, 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        assert(result == expected)
    }
}
