package com.example.licoesigor.model;

import com.example.licoesigor.model.modelImagem;

import java.io.Serializable;
import java.util.ArrayList;

public class modelLicao implements Serializable {
    public int idTarefa ;
    public String nmTarefa;
    public String dsTarefa ;
    public String dtEntrega;
    public ArrayList<modelImagem> imgs;
    public int qtdDiaVencer;
    public Boolean bEntregou;
}
