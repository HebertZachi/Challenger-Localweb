package br.com.fiap.challengerlocalweb.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "carbon_copy",
//    foreignKeys = [ForeignKey(
//        entity = Email::class,
//        parentColumns = ["emailId"],
//        childColumns = ["emailId"],
//        onDelete = ForeignKey.CASCADE
//    )]
)
data class CC(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val ccId: Long = 0,
    val email: String,
)