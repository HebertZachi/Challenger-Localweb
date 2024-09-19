package br.com.fiap.challengerlocalweb.api

import br.com.fiap.challengerlocalweb.viewmodel.EmailReturnReceivedDto
import br.com.fiap.challengerlocalweb.viewmodel.PageResponse
import retrofit2.Call
import retrofit2.http.*

interface EmailApiService {

    @GET("/api/mail/received")
    fun getAllReceivedEmails(
        @Query("id") userId: String?
    ): Call<PageResponse<EmailReturnReceivedDto>>

    @GET("/api/mail")
    fun getReceivedEmail(
        @Query("userId") userId: String,
        @Query("mailId") mailId: String
    ): Call<EmailReturnReceivedDto>

    @GET("/api/mail/find")
    fun getAllEmails(
        @Query("userId") userId: String
    ): Call<PageResponse<EmailReturnReceivedDto>>

//    @POST("/api/mail/send")
//    fun sendEmail(
//        @Query("id") userId: String?,
//        @Body email: EmailAddDto
//    ): Call<EmailReturnSendDto>

    @DELETE("/api/mail")
    fun deleteEmail(
        @Query("userId") userId: String,
        @Query("mailId") mailId: String
    ): Call<Void>
}
