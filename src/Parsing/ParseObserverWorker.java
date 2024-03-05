package Parsing;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

class ParseObserverWorker<T, K> extends SwingWorker<T, K> {

    private ParseObserver<T, K> observer;

    ParseObserverWorker(ParseObserver<T, K> observer) {
        this.observer = observer;
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
                observer.doneTask(get());
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
