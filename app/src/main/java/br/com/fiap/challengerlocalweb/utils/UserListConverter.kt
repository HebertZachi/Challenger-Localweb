package br.com.fiap.challengerlocalweb.utils

import androidx.room.TypeConverter
import br.com.fiap.challengerlocalweb.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class UserListConverter {

    @TypeConverter
    fun fromUserList(users: List<User>): String {
        val gson = Gson()
        return gson.toJson(users)
    }

    @TypeConverter
    fun toUserList(data: String): List<User> {
        val listType = object : TypeToken<List<User>>() {}.type
        return Gson().fromJson(data, listType)
    }
}
