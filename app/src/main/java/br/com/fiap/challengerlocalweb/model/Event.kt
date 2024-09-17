package br.com.fiap.challengerlocalweb.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.LocalDateTime

@Entity(tableName = "events")
data class Event(
    @PrimaryKey val id: Long,
    val title: String,
    val description: String,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val location: String?,
    @TypeConverters(StringListConverter::class)
    val participants: List<String>,
    val isAllDay: Boolean = false,
    val isRecurring: Boolean = false,
    val recurrenceRule: String? = null
)

class StringListConverter {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return value.split(",")
    }
}
