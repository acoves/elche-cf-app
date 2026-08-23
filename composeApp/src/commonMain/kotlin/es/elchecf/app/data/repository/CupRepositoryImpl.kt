package es.elchecf.app.data.repository

import es.elchecf.app.data.CupDataSource
import es.elchecf.app.domain.model.CupTie
import es.elchecf.app.domain.repository.CupRepository

class CupRepositoryImpl(
    private val dataSource: CupDataSource,
) : CupRepository {
    override suspend fun getBracket(): List<CupTie> = dataSource.fetchBracket()
}
