package br.com.fiap.challengerlocalweb.dao

import androidx.room.*
import br.com.fiap.challengerlocalweb.model.SentEmail
import br.com.fiap.challengerlocalweb.model.SentEmailCcCrossRef
import br.com.fiap.challengerlocalweb.model.SentEmailReceiverCrossRef
import br.com.fiap.challengerlocalweb.relations.SentEmailWithUsers

@Dao
interface SentEmailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSentEmail(sentEmail: SentEmail)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSentEmailReceiversCrossRef(crossRefs: List<SentEmailReceiverCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSentEmailCcCrossRef(crossRefs: List<SentEmailCcCrossRef>)

    @Transaction
    @Query("SELECT * FROM sent_emails")
    suspend fun getAllSentEmails(): List<SentEmailWithUsers>

    @Transaction
    @Query("SELECT * FROM sent_emails WHERE sentEmailId = :emailId")
    suspend fun getSentEmailById(emailId: String): SentEmailWithUsers?

    @Update
    suspend fun updateSentEmail(sentEmail: SentEmail)

    @Delete
    suspend fun deleteSentEmail(sentEmail: SentEmail)

    @Query("DELETE FROM SentEmailReceiverCrossRef WHERE sentEmailId = :emailId")
    suspend fun deleteSentEmailReceiversCrossRefs(emailId: String)

    @Query("DELETE FROM SentEmailCcCrossRef WHERE sentEmailId = :emailId")
    suspend fun deleteSentEmailCcCrossRefs(emailId: String)
}
