package MS_Quokka.Utils;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ExclusiveWriteManager {

    //static variables
    private static ExclusiveWriteManager instance;

    //variables
    //Key: lockable
    //Value: owner
    private ConcurrentMap<String, String> lockMap =  new ConcurrentHashMap<String, String>();

    private ExclusiveWriteManager(){}

    public static ExclusiveWriteManager getInstance(){

        // double-checked locking in Singleton pattern
        if(instance == null){
            synchronized (ExclusiveWriteManager.class) {
                if(instance == null){
                    instance = new ExclusiveWriteManager();
                }
            }
        }
        return instance;
    }


    public synchronized void acquireLock(String lockable, String owner) {
        while(lockMap.containsKey(lockable)) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lockMap.put(lockable, owner);
    }

    public synchronized void releaseLock(String lockable, String owner) throws IllegalArgumentException {

        if (!lockMap.containsKey(lockable)) {
            throw new IllegalArgumentException("lockable object could not be found in the lockMap");
        }
        if(!lockMap.remove(lockable, owner)){
            throw new IllegalArgumentException("lockable object does not belong to provided owner");
        }
        notifyAll();
    }

    public synchronized  void releaseAllLock(String owner) {

        try {
            for (Map.Entry<String, String> entry: lockMap.entrySet()) {
                if(entry.getValue().equals(owner)) releaseLock(entry.getKey(), entry.getValue());
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
