package com.sholin.the_reminder.data.mapper

import com.sholin.the_reminder.Reminder as ReminderEntity
import com.sholin.the_reminder.domain.model.Reminder

fun ReminderEntity.toDomain(): Reminder {
    return Reminder(
        id = id,
        header = header,
        description = description,
        date = date,
        alarm = alarm,
        repeatDays = repeatDays,
        repeatTime = repeatTime
    )
}

fun Reminder.toEntity(): ReminderEntity {
    return ReminderEntity(
        id = id,
        header = header,
        description = description,
        date = date,
        alarm = alarm,
        repeatDays = repeatDays,
        repeatTime = repeatTime
    )
}
