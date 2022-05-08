package com.example.licoesigor.firebase;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;

public class FirebaseIdManeger extends FirebaseMessagingService
{
    @Override
    public void onNewToken(@NonNull String token)
    {
        super.onNewToken(token);
    }

}
