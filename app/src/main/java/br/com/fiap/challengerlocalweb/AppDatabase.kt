package br.com.fiap.challengerlocalweb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.fiap.challengerlocalweb.model.ReceivedEmail
import br.com.fiap.challengerlocalweb.model.SentEmail
import androidx.room.TypeConverters
import br.com.fiap.challengerlocalweb.dao.ReceivedEmailDao
import br.com.fiap.challengerlocalweb.dao.SentEmailDao
import br.com.fiap.challengerlocalweb.utils.Converters

@Database(entities = [
    ReceivedEmail::class,
    SentEmail::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun receivedEmailDao(): ReceivedEmailDao
    abstract fun sentEmailDao(): SentEmailDao

    companion object {
        lateinit var instanceDB: AppDatabase

        fun getDataBase(context: Context): AppDatabase {
            if(!Companion::instanceDB.isInitialized) {
                instanceDB = Room
                    .databaseBuilder(
                        context = context,
                        klass = AppDatabase::class.java,
                        name = "localweb_database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instanceDB
        }
    }
}