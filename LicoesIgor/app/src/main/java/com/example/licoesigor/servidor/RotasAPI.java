package com.example.licoesigor.servidor;

import com.example.licoesigor.model.modelHorariosAula;
import com.example.licoesigor.model.modelLicao;
import com.google.gson.JsonElement;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface RotasAPI
{
    //public static String URL_BASE  = "http://177.104.205.131:5051/";
    public static String URL_BASE  = "https://avisoescola.herokuapp.com/";
    //public static String URL_BASE  = "http://192.168.0.109:5051/";

    @POST("Escola/RegistraUsuario")
    Call<String> adicionaUsuario(@Body JsonElement usuario, @Header("auth_token") String token);

    @POST("Escola/RegistraTarefa")
    Call<String> adicionaTarefa(@Body JsonElement tarefa, @Header("auth_token") String token);

    @GET("Escola/ConsultaTarefas")
    Call<ArrayList<modelLicao>> consultaTarefas(@Header("auth_token") String token);

    @PUT("Escola/EntregaTarefa")
    Call<String> entregaTarefa(@Body JsonElement tarefa, @Header("auth_token") String token);

    @PUT("Escola/alteraTarefa")
    Call<String> alteraTarefa(@Body JsonElement tarefa, @Header("auth_token") String token);

    @GET("Escola/Teste")
    Call<String> getTeste();

    @POST("Escola/AdicionaHistorico")
    Call<String> adionaHistorico(@Body JsonElement historico);

    @PUT("Escola/ConfirmaRecebimentoNotf/")
    Call<String> notificaPushRecebido();

    @GET("Escola/AulasSemana")
    Call<ArrayList<modelHorariosAula>> listaHorarios();
}
