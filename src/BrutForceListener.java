import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BrutForceListener implements ActionListener {

    private int solution;
    private CryptAnalyzer cryptAnalyzer;

    public BrutForceListener(int solution, CryptAnalyzer cryptAnalyzer) {
        this.solution       = solution;
        this.cryptAnalyzer  = cryptAnalyzer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
