package br.com.fiap.challengerlocalweb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.fiap.challengerlocalweb.dao.ReceivedEmailDao
import br.com.fiap.challengerlocalweb.dao.SentEmailDao
import br.com.fiap.challengerlocalweb.dao.UserDao
import br.com.fiap.challengerlocalweb.model.ReceivedEmail
import br.com.fiap.challengerlocalweb.model.ReceivedEmailCcCrossRef
import br.com.fiap.challengerlocalweb.model.ReceivedEmailReceiverCrossRef
import br.com.fiap.challengerlocalweb.model.SentEmail
import br.com.fiap.challengerlocalweb.model.SentEmailCcCrossRef
import br.com.fiap.challengerlocalweb.model.SentEmailReceiverCrossRef
import br.com.fiap.challengerlocalweb.model.User
import br.com.fiap.challengerlocalweb.utils.Converters

@Database(entities = [
    ReceivedEmail::class,
    SentEmail::class,
    User::class,
    ReceivedEmailCcCrossRef::class,
    ReceivedEmailReceiverCrossRef::class,
    SentEmailCcCrossRef::class,
    SentEmailReceiverCrossRef::class
], version = 4)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun receivedEmailDao(): ReceivedEmailDao
    abstract fun sentEmailDao(): SentEmailDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var instanceDB: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instanceDB ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "localweb_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                instanceDB = instance
                instance
            }
        }
    }
}
