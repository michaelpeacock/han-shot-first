package artictrail.hanshotfirst.ms.asrc.artictrail.bluetooth;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import eu.hgross.blaubot.android.BlaubotAndroid;
import eu.hgross.blaubot.android.BlaubotAndroidFactory;
import eu.hgross.blaubot.core.IBlaubotDevice;
import eu.hgross.blaubot.core.ILifecycleListener;
import eu.hgross.blaubot.messaging.BlaubotMessage;
import eu.hgross.blaubot.messaging.IBlaubotChannel;
import eu.hgross.blaubot.messaging.IBlaubotMessageListener;

/**
 * Created by Owner on 12/18/2015.
 */
public class BlueToothConnectionManager implements ILifecycleListener {

    private static BlueToothConnectionManager instance;
    private BlaubotAndroid blaubot ;
    private IBlaubotChannel channel;
    private boolean mConnected = false;
    private List<BTConnectionListenerIF> mConnetionListeners;
    private List<BTMessageListenerIF> mMessageListeners;

    private BlueToothConnectionManager() {
        this("ec127529-2e9c-4046-a5a5-144feb30465f");
    }

    private BlueToothConnectionManager(String uuid) {
        final UUID APP_UUID = UUID.fromString(uuid);

        blaubot = BlaubotAndroidFactory.createBluetoothBlaubot(APP_UUID);
        channel = blaubot.createChannel((short) 1);
        mConnetionListeners = new ArrayList<BTConnectionListenerIF>();
        mMessageListeners = new ArrayList<BTMessageListenerIF>();
    }

    @Override
    public void onConnected() {
        mConnected = true;
        for(BTConnectionListenerIF listener : mConnetionListeners) {
            listener.onConnected();
        }

        channel.subscribe(new IBlaubotMessageListener() {
            @Override
            public void onMessage(BlaubotMessage message) {
                for(BTMessageListenerIF listener : mMessageListeners) {
                    listener.onMessageRecieved(message.getPayload());
                }
            }
        });
    }

    @Override
    public void onDisconnected() {
        mConnected = false;
        for(BTConnectionListenerIF listener : mConnetionListeners) {
            listener.onDisconnected();
        }
    }

    @Override
    public void onDeviceJoined(IBlaubotDevice blaubotDevice) {
        for(BTConnectionListenerIF listener : mConnetionListeners) {
            listener.onDeviceJoined(blaubotDevice);
        }
    }

    @Override
    public void onDeviceLeft(IBlaubotDevice blaubotDevice) {
        for(BTConnectionListenerIF listener : mConnetionListeners) {
            listener.onDeviceLeft(blaubotDevice);
        }
    }

    @Override
    public void onPrinceDeviceChanged(IBlaubotDevice oldPrince, IBlaubotDevice newPrince) {
        for(BTConnectionListenerIF listener : mConnetionListeners) {
            listener.onPrinceDeviceChanged(oldPrince, newPrince);
        }
    }

    @Override
    public void onKingDeviceChanged(IBlaubotDevice oldKing, IBlaubotDevice newKing) {
        for(BTConnectionListenerIF listener : mConnetionListeners) {
            listener.onKingDeviceChanged(oldKing, newKing);
        }
    }

    public void onResume(Activity activity, Context context) {
        blaubot.startBlaubot();
        blaubot.registerReceivers(context);
        blaubot.setContext(context);
        blaubot.onResume(activity);
    }

    public void onPause(Activity activity, Context context) {
        blaubot.unregisterReceivers(context);
        blaubot.onPause(activity);
    }

    public void onStop(Activity activity, Context context) {
        blaubot.stopBlaubot();
    }

    public void publish(byte[] data) {
        if(mConnected) {
            channel.publish(data);
        }
    }

    public void startConnection() {
        if(!blaubot.isStarted())
            blaubot.startBlaubot();
    }

    public void registerOnConnectionListener(BTConnectionListenerIF listener) {
        mConnetionListeners.add(listener);
    }

    public void registerOnMessageListener(BTMessageListenerIF listener) {
        mMessageListeners.add(listener);
    }

    public static synchronized BlueToothConnectionManager getInstance() {
        if(instance == null)
            instance = new BlueToothConnectionManager();
        return instance;
    }


}
