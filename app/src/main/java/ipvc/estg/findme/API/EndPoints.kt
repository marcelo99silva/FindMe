package ipvc.estg.findme.API

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    @FormUrlEncoded
    @POST("/MySlim/api/addreport")
    fun adicionarPonto(@Field("raca") raca: String?,
                       @Field("tamanho") tamanho: String?,
                       @Field("sexo") sexo: String?,
                       @Field("localizacao") localizacao: String?,
                       @Field("data") data: String?,
                       @Field("descricao") descricao: String?,
                       @Field("chip") chip: String?,
                       @Field("foto") foto: String?,
                       @Field("idUser") idUser: String?,
                       @Field("TipoReport") TipoReport: Int?
    ): Call<OutputReports>

    // Obter report pelo id
    @GET("/MySlim/api/getreport/{idReport}")
    fun obterReportId(
        @Path("idReport") tipo: String
    ): Call<List<Reports>>

    // Obter reports de um tipo especifico
    @GET("/MySlim/api/getreports/{tipo}")
    fun getReports(
        @Path("tipo") tipo: Int,
    ): Call<List<Reports>>
}