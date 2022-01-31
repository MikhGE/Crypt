import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExecuteListener implements ActionListener {

    private CryptAnalyzer cryptAnalyzer;
    private JLabel erorrLabel;
    private JTextField keyField;

    public ExecuteListener(CryptAnalyzer cryptAnalyzer, JLabel erorrLabel, JTextField keyField) {
        this.cryptAnalyzer  = cryptAnalyzer;
        this.erorrLabel     = erorrLabel;
        this.keyField       = keyField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        erorrLabel.setText("");
        cryptAnalyzer.clearErorrs();
        String keyString = keyField.getText();
        if(keyString.isEmpty()&&(cryptAnalyzer.getMode()==1||cryptAnalyzer.getMode()==2))
        {
            erorrLabel.setText("Key is empty");
            return;
        }
        int keyInt = 0;
        if(cryptAnalyzer.getMode()==1||cryptAnalyzer.getMode()==2){
            try{
                keyInt = Integer.parseInt(keyString);
            }
            catch (NumberFormatException exception){
                erorrLabel.setText("Key is not digit");
                return;
            }
        }
        cryptAnalyzer.setKey(keyInt);
        if(!cryptAnalyzer.check())
            erorrLabel.setText(cryptAnalyzer.getErorrs());
        else cryptAnalyzer.execute();
    }
}
