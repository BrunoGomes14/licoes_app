package com.example.licoesigor.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Calendar;

public class modelHorariosAula
{
    @SerializedName("dia")
    public String sDia = "";

    @SerializedName("materias")
    public ArrayList<modelMateria> arrMaterias = null;

    @SerializedName("diaHorario")
    public int iDiaSemana = 0;

    public boolean isDia()
    {
        Calendar calendar = Calendar.getInstance();
        int diaSemanaAtual = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        return diaSemanaAtual == iDiaSemana;
    }
}
