package com.example.licoesigor.ultil;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.inputmethodservice.InputMethodService;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.licoesigor.R;
import com.example.licoesigor.model.HistoricoCel;
import com.example.licoesigor.servidor.EnviaDados;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.spec.ECField;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class ServicoHistorico extends Service {

    boolean bContinua = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
        } else {
            startForeground(1, new Notification());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try
        {
            if (intent.getAction().equals("LIGA")) {
                comecaProcesso();
            }
            else if (intent.getAction().equals("DESLIGA")) {
                //your end servce code
                stopForeground(true);
                stopSelfResult(startId);
            }
        }
        catch (Exception err)
        {
            Toast.makeText(this, "Erro serviço", Toast.LENGTH_LONG).show();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Notificação 'to de olho'";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        //Bitmap btmIcon = BitmapFactory.decodeResource(getResources(), );

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();

        startForeground(2, notification);
    }


    public void comecaProcesso() throws Exception
    {
        ActivityManager acManager = null;
        HistoricoCel hstCel = null;
        BatteryManager btManager = (BatteryManager)getSystemService(BATTERY_SERVICE);

        while (bContinua)
        {
            // espera um minuto
            //Thread.sleep(60000);

            // recupera informacoes do cel
            acManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);

            // recupera a primeira task que será a da activity principal
            ActivityManager.RunningTaskInfo foregroundTaskInfo = acManager.getRunningTasks(1).get(0);

            InputMethodService packageName = new InputMethodService();
            InputConnection currentInputConnection = packageName.getCurrentInputConnection();

            String sa = getCurrentApp();

            // pega o pack da tela ativa e após com isso pega o nome do app
            String foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();
            PackageManager pm = this.getPackageManager();
            PackageInfo foregroundAppPackageInfo = pm.getPackageInfo(foregroundTaskPackageName, 0);

            hstCel = new HistoricoCel();
            hstCel.sAppPack = foregroundAppPackageInfo.applicationInfo.loadLabel(pm).toString();
            hstCel.sBateria = btManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) + "%";
            hstCel.dtAtual = Calendar.getInstance().getTime();

            EnviaDados enviaDados = new EnviaDados();
            enviaDados.adicionaHistorico(hstCel);

            Thread.sleep(15000);
        }
    }

    private String getCurrentApp() throws Exception {
        String serv = "";
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo = am.getRunningAppProcesses();

        for (int i = 0; i < runningAppProcessInfo.size(); i++) {
            serv = runningAppProcessInfo.get(i).processName + "/";
        }

        return serv;
    }
}
