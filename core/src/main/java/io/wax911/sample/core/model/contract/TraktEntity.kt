package io.wax911.sample.core.model.contract

import java.time.OffsetDateTime

interface TraktEntity {

    val title: String
    val year: Int

    // extended info
    val overview: String?
    val runtime: Int?
    val country: String?
    val trailer: String?
    val homepage: String?
    val rating: Double?
    val votes: Int?
    val updated_at: OffsetDateTime?
    val language: String?
    val genres: List<String>?
    val certification: String?
}