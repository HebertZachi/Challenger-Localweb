package br.com.fiap.challengerlocalweb.utils

import br.com.fiap.challengerlocalweb.model.ReceivedEmail
import br.com.fiap.challengerlocalweb.viewmodel.EmailReturnReceivedDto
import java.time.LocalDateTime

fun mapToReceivedEmail(dto: EmailReturnReceivedDto): ReceivedEmail {
    return ReceivedEmail(
        receivedEmailId = dto.id,
        subject = dto.subject,
        senderEmail = dto.from,
        body = dto.body,
        createdAt = LocalDateTime.now(),
        isRead = dto.isRead,
        isSent = dto.isSent,
        flaged = dto.flaged,
        type = dto.type,
        priority = dto.priority
    )
}
