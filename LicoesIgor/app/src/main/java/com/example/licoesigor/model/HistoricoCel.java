package com.example.licoesigor.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class HistoricoCel
{
    @SerializedName("DsAppNome")
    public String sAppPack = "";

    @SerializedName("DsHorario")
    public Date dtAtual = null;

    @SerializedName("DsBateria")
    public String sBateria = "";

    @SerializedName("")
    public String sRede = "";
}
