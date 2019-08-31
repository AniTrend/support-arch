package io.wax911.sample.domain.entities.contract

interface TraktEntity {

    val id: Int
    val title: String?
    val year: Int?

    // extended info
    val overview: String?
    val runtime: Int?
    val country: String?
    val trailer: String?
    val homepage: String?
    val rating: Double?
    val votes: Int?
    val updatedAt: String?
    val language: String?
    val genres: List<String>?
    val certification: String?
}