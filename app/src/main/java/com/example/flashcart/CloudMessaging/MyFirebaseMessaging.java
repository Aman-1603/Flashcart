package com.example.flashcart.CloudMessaging;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.flashcart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    private static final String NOTIFICATION_CHANNEL_ID = "MY_NOTIFICATION_CHANNEL_ID";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remotemessage) {
        super.onMessageReceived(remotemessage);

        //all notification will be displayed here


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        //get data from notification

        String notificationType = remotemessage.getData().get("notificationType");

        if (notificationType.equals("NewOrder")){

            String buyerUid = remotemessage.getData().get("buyertid");
            String sellerUid = remotemessage.getData().get("sellerUid");
            String orderId = remotemessage.getData().get("orderId");
            String notificationTitle = remotemessage.getData().get("notificationTitle");
            String notificationDescription = remotemessage.getData().get("notificationDescription");

            if (firebaseUser !=null && firebaseAuth.getUid().equals(sellerUid)){

                //user is signed in and its a same user

            }

        }

        if (notificationType.equals("OrderStatusChanged")){

            String buyerUid = remotemessage.getData().get("buyerUid");
            String sellerUid = remotemessage.getData().get("sellerUid");
            String orderTd = remotemessage.getData().get("orderId");
            String notificationTitle = remotemessage.getData().get("notificationTitle");
            String notificationDescription = remotemessage.getData().get("notificationDescription");


            if (firebaseUser !=null && firebaseAuth.getUid().equals(buyerUid)){

                //user is signed in and its a same user on which notification is sent

                shownotification(orderTd,sellerUid,buyerUid,notificationTitle,notificationDescription,notificationType);

            }

        }

        }
        private void shownotification(String orderId,String sellerUid,String buyerUid, String notificationTitle,String notificationDescription,String notificationType){


            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

            //id for notification, random

            int notificationID = new Random().nextInt(3000);


            //here we will check if the version is oreo or not
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

                setupNotificationChannel(notificationManager);

            }

            //handle notification click, start order Activity;

            if (notificationType.equals("NewOrder")){

                //open orderdetailseller activity


            } else if (notificationType.equals("OrderStatusChanged")) {
                
            }

            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,new Intent(),PendingIntent.FLAG_ONE_SHOT);


            //Large icon

            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.square_icon);

            //sound of notification

            Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(),NOTIFICATION_CHANNEL_ID);

            notificationBuilder.setSmallIcon(R.drawable.square_icon)
                            .setLargeIcon(largeIcon).setContentTitle(notificationTitle).setContentTitle(notificationDescription).setSound(notificationSoundUri).setAutoCancel(true).setContentIntent(pendingIntent);

            notificationManager.notify(notificationID, notificationBuilder.build());

        }

    private void setupNotificationChannel(NotificationManager notificationManager) {




        CharSequence channelName = "Some Sample Text";
        String ChannelDescription = "Channel Description Here";

        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,channelName,NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription(ChannelDescription);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.enableVibration(true);

        if (notificationManager !=null){



        }


    }


}
