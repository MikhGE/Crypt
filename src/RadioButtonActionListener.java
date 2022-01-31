
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RadioButtonActionListener implements ActionListener {
    private CryptAnalyzer cryptAnalyzer;
    private JPanel keyPanel;
    private JPanel panelForStatisticFile;

    public RadioButtonActionListener(CryptAnalyzer cryptAnalyzer, JPanel keyPanel, JPanel panelForStatisticFile) {

        this.cryptAnalyzer = cryptAnalyzer;
        this.keyPanel = keyPanel;
        this.panelForStatisticFile = panelForStatisticFile;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        cryptAnalyzer.setMode(((JRadioButton) e.getSource()).getMnemonic());
        if(cryptAnalyzer.getMode()==1||cryptAnalyzer.getMode()==2)
            keyPanel.setVisible(true);
        else
            keyPanel.setVisible(false);
        if(cryptAnalyzer.getMode()==4)
            panelForStatisticFile.setVisible(true);
        else
            panelForStatisticFile.setVisible(false);
    }
}
