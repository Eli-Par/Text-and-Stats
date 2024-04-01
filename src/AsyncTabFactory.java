import java.io.File;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

public abstract class AsyncTabFactory extends SwingWorker<TabPanel, Object>{

    private File file;
    private String title;

    private FormattingToolBar toolBar;

    public AsyncTabFactory(FormattingToolBar toolBar, File file, String title) {
        this.file = file;
        this.title = title;

        this.toolBar = toolBar;
    }

    @Override
    protected final TabPanel doInBackground() throws Exception {
        TabPanel panel = new TabPanel(file, title);
        panel.getEditor().getTextPane().addCaretListener(toolBar);
        return panel;
    }

    @Override
    protected final void done() {
        try {
            TabPanel panel = get();
            panel.textChanged();
            tabCreated(panel);
        }
        catch(InterruptedException exception) {
            errorOccured();
        }
        catch(ExecutionException exception) {
            errorOccured();
        }
    }

    protected abstract void tabCreated(TabPanel tabPanel);

    protected void errorOccured() {

    }
    
}
