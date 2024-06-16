package br.com.fiap.challengerlocalweb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.fiap.challengerlocalweb.model.ReceivedEmail
import br.com.fiap.challengerlocalweb.model.SentEmail
import androidx.room.TypeConverters
import br.com.fiap.challengerlocalweb.dao.CCDao
import br.com.fiap.challengerlocalweb.dao.ReceivedEmailDao
import br.com.fiap.challengerlocalweb.dao.RecipientDao
import br.com.fiap.challengerlocalweb.dao.SentEmailDao
import br.com.fiap.challengerlocalweb.model.CC
import br.com.fiap.challengerlocalweb.model.ReceivedEmailAndCCCrossRef
import br.com.fiap.challengerlocalweb.model.ReceivedEmailAndRecipientCrossRef
import br.com.fiap.challengerlocalweb.model.Recipient
import br.com.fiap.challengerlocalweb.model.SentEmailAndCCCrossRef
import br.com.fiap.challengerlocalweb.model.SentEmailAndRecipientCrossRef
import br.com.fiap.challengerlocalweb.utils.Converters

@Database(entities = [
    CC::class,
    ReceivedEmail::class,
    ReceivedEmailAndCCCrossRef::class,
    ReceivedEmailAndRecipientCrossRef::class,
    Recipient::class,
    SentEmail::class,
    SentEmailAndCCCrossRef::class,
    SentEmailAndRecipientCrossRef::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun receivedEmailDao(): ReceivedEmailDao
    abstract fun sentEmailDao(): SentEmailDao
    abstract fun recipientDao(): RecipientDao
    abstract fun ccDao(): CCDao

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