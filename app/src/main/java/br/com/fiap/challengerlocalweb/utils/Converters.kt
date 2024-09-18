package br.com.fiap.challengerlocalweb.utils

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class Converters {

    companion object {
        private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

        @TypeConverter
        @JvmStatic
        fun fromTimestamp(value: String?): LocalDateTime? {
            return value?.let {
                return formatter.parse(value, LocalDateTime::from)
            }
        }

        @TypeConverter
        @JvmStatic
        fun dateToTimestamp(date: LocalDateTime?): String? {
            return date?.format(formatter)
        }

        @TypeConverter
        @JvmStatic
        fun fromStringList(value: String?): List<String>? {
            return value?.split(",")?.map { it.trim() }
        }

        @TypeConverter
        @JvmStatic
        fun stringListToString(list: List<String>?): String? {
            return list?.joinToString(",")
        }
    }

    // Remover as funções duplicadas aqui
    // private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    // @TypeConverter
    // fun fromTimestamp(value: String?): LocalDateTime? {
    //     return value?.let { LocalDateTime.parse(it, formatter) }
    // }

    // @TypeConverter
    // fun dateToTimestamp(date: LocalDateTime?): String? {
    //     return date?.format(formatter)
    // }

    @TypeConverter
    fun fromUuid(uuid: UUID?): String? {
        return uuid?.toString()
    }

    @TypeConverter
    fun toUuid(uuid: String?): UUID? {
        return uuid?.let { UUID.fromString(it) }
    }
}
