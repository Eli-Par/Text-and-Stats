package parsing_temp.testing_temp;

import java.awt.event.*;

import parsing_temp.*;


public class StartParseAction implements ActionListener {

    private Parser parser;
    private TestParseObserver worker; //Class extends ParseObserver

    public StartParseAction(Parser parser) {
        this.parser = parser;
        worker = new TestParseObserver(parser);
        parser.addParseObserver(worker);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        parser.parse("This would be the text that is currently hard coded inside the TestParser");

    }
    
}
