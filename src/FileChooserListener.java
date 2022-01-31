import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileChooserListener implements ActionListener {

    private JTextField textField;
    private JButton button;
    private CryptAnalyzer cryptAnalyzer;
    private int typeOfFileName;

    public FileChooserListener(JTextField textField, JButton button, CryptAnalyzer cryptAnalyzer, int typeOfFileName) {
        this.textField      = textField;
        this.button         = button;
        this.cryptAnalyzer  = cryptAnalyzer;
        this.typeOfFileName = typeOfFileName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showOpenDialog(button);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            if (typeOfFileName==1)
                cryptAnalyzer.setFileNameForExecute(textField.getText());
            else if(typeOfFileName==2)
                cryptAnalyzer.setFileNameForResult(textField.getText());
            else if(typeOfFileName==3)
                cryptAnalyzer.setFileNameForStatistic(textField.getText());

        }
    }
}
