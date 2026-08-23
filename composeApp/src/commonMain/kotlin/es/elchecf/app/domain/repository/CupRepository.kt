package es.elchecf.app.domain.repository

import es.elchecf.app.domain.model.CupTie

interface CupRepository {
    suspend fun getBracket(): List<CupTie>
}
