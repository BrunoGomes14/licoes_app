package com.example.licoesigor.servidor;

import com.example.licoesigor.model.ErrorModel;
import com.example.licoesigor.model.HistoricoCel;
import com.example.licoesigor.model.modelHorariosAula;
import com.example.licoesigor.model.modelLicao;
import com.example.licoesigor.model.modelUsuario;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EnviaDados
{
    RotasAPI service;
    GsonBuilder gson = new GsonBuilder();
    JsonElement jEnvio = null;
    ErrorModel error = new ErrorModel();

    public void enviaUsuario(modelUsuario usuario, responseCallback callback)
    {
        jEnvio = gson.create().toJsonTree(usuario);

        service = retrofitConfig.acao();
        service.adicionaUsuario(jEnvio, "").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try
                {
                    if (response.isSuccessful())
                    {
                        callback.responseSucess(true);
                    }
                    else
                    {
                        JSONObject jErro = new JSONObject(response.errorBody().string());

                        error.setCodigoErro(jErro.getInt("codigoErro"));
                        error.setMensagemErro(jErro.getString("mensagemErro"));

                        callback.responseError(error);
                    }
                }
                catch (Exception err)
                {
                    error.erroPadrao();
                    callback.responseError(error);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                error.erroConexao();
                callback.responseError(error);
            }
        });
    }

    public void adicionaTarefa(modelLicao tarefa, responseCallback callback)
    {
        jEnvio = gson.create().toJsonTree(tarefa);

        service = retrofitConfig.acao();
        service.adicionaTarefa(jEnvio, "").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try
                {
                    if (response.isSuccessful())
                    {
                        callback.responseSucess(true);
                    }
                    else
                    {
                        JSONObject jErro = new JSONObject(response.errorBody().string());

                        error.setCodigoErro(jErro.getInt("codigoErro"));
                        error.setMensagemErro(jErro.getString("mensagemErro"));

                        callback.responseError(error);
                    }
                }
                catch (Exception err)
                {
                    error.erroPadrao();
                    callback.responseError(error);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                error.erroConexao();
                callback.responseError(error);
            }
        });
    }

    public void alteraTarefa(modelLicao tarefa, responseCallback callback)
    {
        jEnvio = gson.create().toJsonTree(tarefa);

        service = retrofitConfig.acao();
        service.alteraTarefa(jEnvio, "").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try
                {
                    if (response.isSuccessful())
                    {
                        callback.responseSucess(true);
                    }
                    else
                    {
                        JSONObject jErro = new JSONObject(response.errorBody().string());

                        error.setCodigoErro(jErro.getInt("codigoErro"));
                        error.setMensagemErro(jErro.getString("mensagemErro"));

                        callback.responseError(error);
                    }
                }
                catch (Exception err)
                {
                    error.erroPadrao();
                    callback.responseError(error);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                error.erroConexao();
                callback.responseError(error);
            }
        });
    }

    public void entregaTarefa(modelLicao licao, responseCallback callback)
    {
        jEnvio = gson.create().toJsonTree(licao);

        service = retrofitConfig.acao();
        service.entregaTarefa(jEnvio, "").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try
                {
                    if (response.isSuccessful())
                    {
                        callback.responseSucess(true);
                    }
                    else
                    {
                        JSONObject jErro = new JSONObject(response.errorBody().string());

                        error.setCodigoErro(jErro.getInt("codigoErro"));
                        error.setMensagemErro(jErro.getString("mensagemErro"));

                        callback.responseError(error);
                    }
                }
                catch (Exception err)
                {
                    error.erroPadrao();
                    callback.responseError(error);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                error.erroConexao();
                callback.responseError(error);
            }
        });
    }

    public void buscaTarefas(responseCallback callback)
    {
        service = retrofitConfig.acao();
        service.consultaTarefas("").enqueue(new Callback<ArrayList<modelLicao>>() {
            @Override
            public void onResponse(Call<ArrayList<modelLicao>> call, Response<ArrayList<modelLicao>> response)
            {
                try
                {
                    if (response.isSuccessful())
                    {
                        callback.responseSucess(response.body());
                    }
                    else
                    {
                        JSONObject jErro = new JSONObject(response.errorBody().string());

                        error.setCodigoErro(jErro.getInt("codigoErro"));
                        error.setMensagemErro(jErro.getString("mensagemErro"));

                        callback.responseError(error);
                    }
                }
                catch (Exception err)
                {
                    error.erroPadrao();
                    callback.responseError(error);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<modelLicao>> call, Throwable t) {
                error.erroConexao();
                callback.responseError(error);
            }
        });
    }

    public void getInicial(responseCallback callback)
    {
        service = retrofitConfig.acao();
        service.getTeste().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                callback.responseSucess(true);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                callback.responseError(true);
            }
        });
    }

    public void adicionaHistorico(HistoricoCel historicoCel) throws Exception
    {
        jEnvio = gson.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create().toJsonTree(historicoCel);

        service = retrofitConfig.acao();
        service.adionaHistorico(jEnvio).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                String s = t.getMessage();
            }
        });
    }

    public void notificaPushRecebido()
    {
        service = retrofitConfig.acao();
        service.notificaPushRecebido().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    public void listaHorarios(responseCallback callback)
    {
        service = retrofitConfig.acao();
        service.listaHorarios().enqueue(new Callback<ArrayList<modelHorariosAula>>() {
            @Override
            public void onResponse(Call<ArrayList<modelHorariosAula>> call, Response<ArrayList<modelHorariosAula>> response) {
                try
                {
                    if (response.isSuccessful())
                    {
                        callback.responseSucess(response.body());
                    }
                    else
                    {
                        JSONObject jErro = new JSONObject(response.errorBody().string());

                        error.setCodigoErro(jErro.getInt("codigoErro"));
                        error.setMensagemErro(jErro.getString("mensagemErro"));

                        callback.responseError(error);
                    }
                }
                catch (Exception err)
                {
                    error.erroPadrao();
                    callback.responseError(error);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<modelHorariosAula>> call, Throwable t) {
                error.erroConexao();
                callback.responseError(error);
            }
        });
    }
}
