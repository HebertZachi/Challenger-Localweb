package br.com.fiap.challengerlocalweb.dao

import androidx.room.*
import br.com.fiap.challengerlocalweb.model.ReceivedEmail
import br.com.fiap.challengerlocalweb.model.ReceivedEmailCcCrossRef
import br.com.fiap.challengerlocalweb.model.ReceivedEmailReceiverCrossRef
import br.com.fiap.challengerlocalweb.relations.ReceivedEmailWithUsers

@Dao
interface ReceivedEmailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmail(receivedEmail: ReceivedEmail)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmailReceiversCrossRef(crossRefs: List<ReceivedEmailReceiverCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmailCcCrossRef(crossRefs: List<ReceivedEmailCcCrossRef>)

    @Transaction
    @Query("SELECT * FROM received_emails")
    suspend fun getAllEmails(): List<ReceivedEmailWithUsers>

    @Transaction
    @Query("SELECT * FROM received_emails WHERE receivedEmailId = :emailId")
    suspend fun getEmailById(emailId: String): ReceivedEmailWithUsers?

    @Update
    suspend fun updateEmail(receivedEmail: ReceivedEmail)

    @Delete
    suspend fun deleteEmail(receivedEmail: ReceivedEmail)

    @Query("DELETE FROM ReceivedEmailReceiverCrossRef WHERE receivedEmailId = :emailId")
    suspend fun deleteEmailReceiversCrossRefs(emailId: String)

    @Query("DELETE FROM ReceivedEmailCcCrossRef WHERE receivedEmailId = :emailId")
    suspend fun deleteEmailCcCrossRefs(emailId: String)
}
