package ipvc.estg.findme.API




data class Reports(
    val idReport: Int,
    val raca: String,
    val tamanho: String,
    val sexo: String,
    val localizacao: String,
    val data: String,
    val descricao: String,
    val chip: String,
    val foto: String,
    val idUser: String,
    val TipoReport: Int,
)

