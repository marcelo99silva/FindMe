package ipvc.estg.findme.API

import java.sql.Blob

data class OutputReports(
    val idReport: Int,
    val raca: Double,
    val tamanho: Double,
    val sexo: Int,
    val localizacao: String,
    val data: String,
    val descricao: Double,
    val chip: Int,
    val foto: String,
    val idUser: String,
    val TipoReport: String,
)

