package artictrail.hanshotfirst.ms.asrc.artictrail.notifications;


public class PeriodicMutex {
    public boolean periodicStatus;
    private static PeriodicMutex instance;

    private PeriodicMutex(){
        periodicStatus = false;
    }

    public static synchronized PeriodicMutex getInstance() {
        if(instance == null)
            instance = new PeriodicMutex();
        return instance;
    }

    public boolean isPeriodicActive() {
        return periodicStatus;
    }

    public void setPeriodicActive() {
        periodicStatus = true;
    }

    public void setPeriodicInactive() {
        periodicStatus = false;
    }
}
