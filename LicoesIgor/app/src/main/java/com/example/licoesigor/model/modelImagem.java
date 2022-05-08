package com.example.licoesigor.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Base64;

public class modelImagem implements Serializable
{
    public int IdImagem;

    @Expose
    public byte[] ImgBt;

    public String img;

    public int idTarefa;

    public int isDuvida;

    public String dsDuvida;
}
