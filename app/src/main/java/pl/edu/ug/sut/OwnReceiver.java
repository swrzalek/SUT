package pl.edu.ug.sut;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class OwnReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //do all of your jobs here

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, OwnReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);

        if (Build.VERSION.SDK_INT >= 23) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000 * 60 * 5), pi);
        }
        else if (Build.VERSION.SDK_INT >= 19) {
            am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000 * 60 * 5), pi);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000 * 60 * 5), pi);
        }
    }
}
