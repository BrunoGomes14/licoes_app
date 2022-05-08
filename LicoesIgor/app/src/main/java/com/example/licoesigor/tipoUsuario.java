package com.example.licoesigor;

import android.content.Context;
import android.content.SharedPreferences;

public class tipoUsuario
{
    public static final int TIPO_ALUNO = 1;
    public static final int TIPO_ADM = 2;

    public static void insereTipoUsuario(Context context, int tpUsuario)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences("tpUsuario", context.MODE_PRIVATE).edit();
        editor.putInt("user", tpUsuario);
        editor.commit();
    }

    public static boolean possuiUsuario(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences("tpUsuario", context.MODE_PRIVATE);
        int ret  = pref.getInt("user", 0);
        return ret != 0;
    }

    public static boolean isAluno(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences("tpUsuario", context.MODE_PRIVATE);
        int ret  = pref.getInt("user", 0);
        return ret == TIPO_ALUNO;
    }
}
