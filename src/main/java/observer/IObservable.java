package observer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public interface IObservable<T>{
    public HashSet<IObserver<Event>> observers = new HashSet<>();
    default void add(IObserver<Event> obs){
        observers.add(obs);
    }
    default void remove(IObserver<Event> obs){
        observers.remove(obs);
    }
    default void notifyObservers(Event notification){
        this.observers.forEach(o -> {
            o.update(notification);
        });
    }
}


