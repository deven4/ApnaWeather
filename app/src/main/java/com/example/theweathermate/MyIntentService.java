package com.example.theweathermate;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.theweathermate.Utils.HttpHelper;

import java.io.IOException;

public class MyIntentService extends JobIntentService {

    public static final String SERVICE_PAYLOAD = "SERVICE_PAYLOAD";
    public static final String ACTION = "SERVICE_MESSAGE";
    private static final String TAG = "TAG";

    public static void enqueueWork(Context context, Intent work){
        enqueueWork(context,MyIntentService.class,100,work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        String data;
        try {
            data = HttpHelper.downloadUrl(intent.getData().toString());
            sendMessageToUIThread(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToUIThread(String data) {

        Intent intent = new Intent(ACTION);
        intent.putExtra(SERVICE_PAYLOAD, data);
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.sendBroadcast(intent);
    }
}
