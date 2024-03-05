package Parsing;

import java.util.List;

/**
 * Provides methods to stop and start the internal swing worker, only allowing 1 of the processes to run at a time.
 * 
 * Background task is a method that is run on a separate thread, similar to a swing worker.
 * Process task and done task are for interacting with swing components. 
 * Process task is run shortly after calling publish with a object of type K. Done task is run after the background task is finished
 * and will have a parameter for whatever the background task returns.
 * The parseStarted method is called when a new parse begins
 * 
 * @author Eli Pardalis
 * @version 1.0.0
 */

public abstract class ParseObserver<T, K> {

    private ParseObserverWorker<T, K> worker = null;

    //Run on a background thread
    protected abstract T backgroundTask();

    //Run on the swing event disbatch thread
    protected void processTask(List<K> data) { }

    //Run on the swing event disbatch thread
    protected void doneTask(T backgroundResult) {}

    //Run on the thread that called the parser
    public void parseStarted() {}

    //Send a piece of data to the processTask method
    public final void publish(K value) {
        worker.publishValue(value);
    }

    //Check if the task is cancelled
    public final boolean isCancelled() {
        return worker.isCancelled();
    }

    //Start the internal swing worker
    public final void startThread() {
        worker = new ParseObserverWorker<>(this);
        worker.execute();
    }

    //Stop the internal swing worker
    public final void stopThread() {
        if(worker != null) {
            worker.cancel(true);
            worker = null;
        }

    }
    
}
