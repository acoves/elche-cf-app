package es.elchecf.app.data

import es.elchecf.app.domain.model.CupTie

interface CupDataSource {
    suspend fun fetchBracket(): List<CupTie>
}
