package com.example.licoesigor.model;

import com.google.gson.annotations.SerializedName;

public class ErrorModel
{
    @SerializedName("codigoErro")
    private int codigoErro;

    @SerializedName("mensagemErro")
    private String mensagemErro;

    public int getCodigoErro() {
        return codigoErro;
    }

    public void setCodigoErro(int codigoErro) {
        this.codigoErro = codigoErro;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

    public void erroPadrao()
    {
        codigoErro = 500;
        mensagemErro = "Erro inesperado\nTente novamente";
    }

    public void erroConexao()
    {
        codigoErro = 400;
        mensagemErro = "Não foi possível estabelecer\nconexão com servidor";
    }
}
