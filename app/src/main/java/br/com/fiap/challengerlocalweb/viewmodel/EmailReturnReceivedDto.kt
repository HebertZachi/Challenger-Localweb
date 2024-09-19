package br.com.fiap.challengerlocalweb.viewmodel

import com.google.gson.annotations.SerializedName

data class EmailReturnReceivedDto(
    @SerializedName("id") val id: String,
    @SerializedName("from") val from: String,
    @SerializedName("to") val to: List<String>,
    @SerializedName("cc") val cc: List<String>,
    @SerializedName("subject") val subject: String,
    @SerializedName("body") val body: String,
    @SerializedName("date") val date: String,
    @SerializedName("type") val type: String,
    @SerializedName("priority") val priority: String,
    @SerializedName("isRead") val isRead: Boolean,
    @SerializedName("isSent") val isSent: Boolean,
    @SerializedName("flaged") val flaged: Boolean
)

data class PageResponse<T>(
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("size") val size: Int,
    @SerializedName("content") val content: List<T>,
    @SerializedName("number") val number: Int,
    @SerializedName("sort") val sort: Sort,
    @SerializedName("first") val first: Boolean,
    @SerializedName("last") val last: Boolean,
    @SerializedName("numberOfElements") val numberOfElements: Int,
    @SerializedName("pageable") val pageable: Pageable,
    @SerializedName("empty") val empty: Boolean
)

data class Sort(
    @SerializedName("empty") val empty: Boolean,
    @SerializedName("sorted") val sorted: Boolean,
    @SerializedName("unsorted") val unsorted: Boolean
)

data class Pageable(
    @SerializedName("pageNumber") val pageNumber: Int,
    @SerializedName("pageSize") val pageSize: Int,
    @SerializedName("sort") val sort: Sort,
    @SerializedName("offset") val offset: Int,
    @SerializedName("paged") val paged: Boolean,
    @SerializedName("unpaged") val unpaged: Boolean
)

