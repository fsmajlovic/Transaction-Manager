package ba.unsa.etf.rma.transactionmanager.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Observable;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.d("NetworkChangeReceiver","Connection status changed");
        getObservable().connectionChanged();
    }

    public static class NetworkObservable extends Observable {
        private static NetworkObservable instance = null;

        private NetworkObservable() {

        }

        public void connectionChanged(){
            setChanged();
            notifyObservers();
        }

        public static NetworkObservable getInstance(){
            if(instance == null){
                instance = new NetworkObservable();
            }
            return instance;
        }
    }

    public static NetworkObservable getObservable() {
        return NetworkObservable.getInstance();
    }
}