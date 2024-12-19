package kr.ac.uc.order_mgr;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.util.Date;

public class TimeService extends Service {
    Date startTime;
    long passedSec;

    LocalBinder binder = new LocalBinder();

    public TimeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTime = new Date();
        new Thread(() -> {
            while (true) {
                Date now = new Date();
                passedSec = (now.getTime() - startTime.getTime()) / 1000;
                SystemClock.sleep(1_000);
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    public long getPassedSec() {
        return passedSec;
    }

    class LocalBinder extends Binder {
        public TimeService getService() {
            return TimeService.this;
        }
    }
}