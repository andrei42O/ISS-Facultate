package observer;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BasicThreadSafeObservableExample implements IObservable<Event> {

    private final Set<IObserver<Event>> mObservers = Collections.newSetFromMap(
            new ConcurrentHashMap<IObserver<Event>, Boolean>(0));

    /**
     * This method adds a new Observer - it will be notified when Observable changes
     */
    @Override
    public void add(IObserver<Event> observer) {
        if (observer == null) return;
        mObservers.add(observer); // this is safe due to thread-safe Set
    }

    /**
     * This method removes an Observer - it will no longer be notified when Observable changes
     */
    @Override
    public void remove(IObserver<Event> observer) {
        if (observer != null) {
            mObservers.remove(observer); // this is safe due to thread-safe Set
        }
    }

    /**
     * This method notifies currently registered observers about Observable's change
     */
    @Override
    public void notifyObservers(Event e) {
        for (IObserver<Event> observer : mObservers) { // this is safe due to thread-safe Set
            observer.update(e);
        }
    }
}
