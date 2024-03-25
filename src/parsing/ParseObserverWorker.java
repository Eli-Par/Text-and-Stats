package parsing;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

class ParseObserverWorker<T, K> extends SwingWorker<T, K> {

    private ParseObserver<T, K> observer;

    private long startTime;

    ParseObserverWorker(ParseObserver<T, K> observer, long startTime) {
        this.observer = observer;
        this.startTime = startTime;
    }

    @Override
    protected T doInBackground() {
        if(!isCancelled()) {
            return observer.backgroundTask();
        }

        return null;
    }

    @Override
    protected void process(List<K> data) {
        if(!isCancelled()) {
            observer.processTask(data);
        }
    }

    @Override
    protected void done() {
        if(!isCancelled()) {
            try {
                observer.tryDoneTask(get(), startTime);
            }
            catch(InterruptedException interruptedException) {

            }
            catch(ExecutionException executionException) {

            }
        }

        //observer.stopThread();
    }

    protected void publishValue(K value) {
        publish(value);
    }
    
}
