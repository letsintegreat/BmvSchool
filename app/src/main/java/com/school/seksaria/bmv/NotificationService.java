package com.school.seksaria.bmv;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NotificationService extends Service {

    private FirebaseUser mFirebaseUser;
    private User mUser;
    private DatabaseReference mUserDatabaseReference, mDiscussionDatabaseReference, mHolidayDatabaseReference, mHomeworkDatabaseReference;

    @Override
    public void onCreate() {
        super.onCreate();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mUserDatabaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(mFirebaseUser.getUid());
        mUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);

                mDiscussionDatabaseReference = FirebaseDatabase.getInstance()
                        .getReference()
                        .child("discussion")
                        .child(""+mUser.getClassNumber());
                mDiscussionDatabaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        UserMessage message = dataSnapshot.getValue(UserMessage.class);

                        if (mUser.getFullName() != message.getUserName()) {
                            Intent intent = new Intent(NotificationService.this, MainActivity.class);
                            PendingIntent pIntent = PendingIntent.getActivity(NotificationService.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

                            Notification notification = new Notification.Builder(NotificationService.this)
                                    .setContentTitle("New Message from " + message.getUserName())
                                    .setContentText(message.getMessageContext())
                                    .setContentIntent(pIntent)
                                    .setSmallIcon(R.drawable.ic_chat_black_24dp)
                                    .setLargeIcon(BitmapFactory.decodeResource(NotificationService.this.getResources(),
                                            R.drawable.ic_chat_black_24dp))
                                    .setSound(Uri.parse("android.resource://"
                                            + NotificationService.this.getPackageName() + "/" + R.raw.sound))
                                    .build();
                            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            notification.flags |= Notification.FLAG_AUTO_CANCEL;
                            notificationManager.notify(0, notification);
                        }
                    }

                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onCancelled(DatabaseError databaseError) {}
                });

                mHolidayDatabaseReference = FirebaseDatabase.getInstance()
                        .getReference()
                        .child("holiday");
                mHolidayDatabaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Holiday holiday = dataSnapshot.getValue(Holiday.class);
                        Intent intent = new Intent(NotificationService.this, MainActivity.class);
                        PendingIntent pIntent = PendingIntent.getActivity(NotificationService.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

                        Notification notification = new Notification.Builder(NotificationService.this)
                                .setContentTitle(holiday.getTag())
                                .setContentText("Holiday on " + holiday.getHolidayDate())
                                .setSmallIcon(R.drawable.ic_directions_car_black_24dp)
                                .setLargeIcon(BitmapFactory.decodeResource(NotificationService.this.getResources(),
                                        R.drawable.ic_directions_car_black_24dp))
                                .setSound(Uri.parse("android.resource://"
                                        + NotificationService.this.getPackageName() + "/" + R.raw.sound))
                                .setContentIntent(pIntent)
                                .build();

                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        notification.flags |= Notification.FLAG_AUTO_CANCEL;
                        notificationManager.notify(0, notification);
                    }
                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onCancelled(DatabaseError databaseError) {}
                });

                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());

                String formattedDate = dateFormat.format(date);
                mHomeworkDatabaseReference = FirebaseDatabase.getInstance()
                        .getReference()
                        .child("homework")
                        .child(""+mUser.getClassNumber())
                        .child(formattedDate);
                mHomeworkDatabaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Holiday holiday = dataSnapshot.getValue(Holiday.class);
                        Intent intent = new Intent(NotificationService.this, MainActivity.class);
                        PendingIntent pIntent = PendingIntent.getActivity(NotificationService.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

                        Notification notification = new Notification.Builder(NotificationService.this)
                                .setContentTitle(holiday.getTag() + " Homework")
                                .setContentText(holiday.getHolidayDate())
                                .setSmallIcon(R.drawable.ic_edit_black_24dp)
                                .setLargeIcon(BitmapFactory.decodeResource(NotificationService.this.getResources(),
                                        R.drawable.ic_edit_black_24dp))
                                .setSound(Uri.parse("android.resource://"
                                        + NotificationService.this.getPackageName() + "/" + R.raw.sound))
                                .setContentIntent(pIntent)
                                .build();

                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        notification.flags |= Notification.FLAG_AUTO_CANCEL;
                        notificationManager.notify(0, notification);
                    }
                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onCancelled(DatabaseError databaseError) {}
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
