package com.example.licoesigor.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.licoesigor.R;
import com.example.licoesigor.servidor.EnviaDados;
import com.example.licoesigor.telas.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class LicoesFirebaseMessagingService extends FirebaseMessagingService
{
    private static final String CHANNEL_ID = "1";
    private static final String CHANNEL_ID_AULA = "2";
    BroadcastReceiver mReceiver;


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String sTitulo = "";
        String sMensagem = "";
        String sActivity = "";

        sTitulo = remoteMessage.getData().get("title");
        sMensagem = remoteMessage.getData().get("body");
        sActivity = remoteMessage.getData().get("activity");

        exibirNotificacao(sTitulo, sMensagem, sActivity);
    }

    private void exibirNotificacao(String strTituloNotificacao, String strMensagem, String sActivity)
    {
        AudioManager mobilemode = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getApplicationContext().getPackageName() + "/" + R.raw.sirene);
        boolean bAvisoAula = strTituloNotificacao.contains("Tá tendo aula hein!");

        exibiuNotificacao(this, bAvisoAula);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !bAvisoAula)
        {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = getString(R.string.channel_aula);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_AULA, name, importance);
            channel.setDescription(description);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            channel.setSound(soundUri, audioAttributes);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("pushActivity", sActivity);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        long[] pattern = {0, 100, 1000};

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, bAvisoAula ? CHANNEL_ID_AULA : CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_book)
                .setContentTitle(strTituloNotificacao)
                .setContentText(strMensagem)
                .setColor(getColor(R.color.colorPrimary))
                .setColorized(true)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(strMensagem))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setVibrate(pattern)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        try
        {
            mobilemode.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            mobilemode.setStreamVolume(AudioManager.STREAM_RING,mobilemode.getStreamMaxVolume(AudioManager.STREAM_RING) - 2 ,0);
        }
        catch (Exception err)
        {
            String s = err.getMessage();
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(retornaIdNotificacao(this), builder.build());
    }

    public void exibiuNotificacao(Context context, boolean bAvisa)
    {
        String key = "notification";
        String keyIdNotf = "notifyID";

        SharedPreferences dados = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        int id = dados.getInt(keyIdNotf, 0);

        SharedPreferences.Editor edit = dados.edit();
        edit.putInt(keyIdNotf, id + 1);
        edit.commit();

        if (bAvisa)
        {
            EnviaDados enviaDados = new EnviaDados();
            enviaDados.notificaPushRecebido();
        }
    }

    public int retornaIdNotificacao(Context context)
    {
        String key = "notification";
        String keyIdNotf = "notifyID";

        SharedPreferences dados = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return dados.getInt(keyIdNotf, 0);
    }
}
