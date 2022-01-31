import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionListener;

public class SwingApp extends JFrame {
    private CryptAnalyzer cryptAnalyzer;

    public SwingApp(CryptAnalyzer cryptAnalyzer) {

        this.cryptAnalyzer = cryptAnalyzer;

        JTabbedPane tabbedPane                  = new JTabbedPane();
        //Закладки
        //TODO: Реализовать настройки:
        //     1. реадктирование в пользовательском режиме типовой криптоалфавит
        //     2. создание и добавление файловой базы SQLi для хранения статистики
        JPanel mainPanel                        = new JPanel();
        JPanel settingPanel                     = new JPanel();

        //Панель для элементов выбора файлов для обработки
        JPanel panelForExecuteFile              = new JPanel();
        JLabel labelFileForExecute              = new JLabel("File for execute:");
        JTextField fileNameForExecute           = new JTextField(25);
        JButton fileChooseButtonForExecute      = new JButton("Choose file");
        ActionListener chooseExecuteFileAction  = new FileChooserListener(fileNameForExecute, fileChooseButtonForExecute, cryptAnalyzer, 1);
        fileChooseButtonForExecute.addActionListener(chooseExecuteFileAction);
        panelForExecuteFile.add(labelFileForExecute);
        panelForExecuteFile.add(fileNameForExecute);
        panelForExecuteFile.add(fileChooseButtonForExecute);

        mainPanel.add(panelForExecuteFile);
        //Панель для элементов выбора файла результата
        JPanel panelForResultFile               = new JPanel();
        JLabel labelFileForResult               = new JLabel("File for result:");
        JTextField fileNameForResult            = new JTextField(25);
        JButton fileChooseButtonForResult       = new JButton("Choose file");
        ActionListener chooseResultFileAction   = new FileChooserListener(fileNameForResult, fileChooseButtonForResult, cryptAnalyzer, 2);
        fileChooseButtonForResult.addActionListener(chooseResultFileAction);
        panelForResultFile.add(labelFileForResult);
        panelForResultFile.add(fileNameForResult);
        panelForResultFile.add(fileChooseButtonForResult);

        mainPanel.add(panelForResultFile);

        //Панель для элементов выбора файла статистики
        JPanel panelForStatisticFile = new JPanel();
        JLabel labelFileForStatistic = new JLabel("File for statistic:");
        JTextField fileNameForStatistic = new JTextField(25);
        JButton fileChooseButtonForStatistic = new JButton("Choose file");
        ActionListener chooseStatisticFileAction = new FileChooserListener(fileNameForStatistic, fileChooseButtonForStatistic, cryptAnalyzer, 3);
        fileChooseButtonForStatistic.addActionListener(chooseStatisticFileAction);
        panelForStatisticFile.add(labelFileForStatistic);
        panelForStatisticFile.add(fileNameForStatistic);
        panelForStatisticFile.add(fileChooseButtonForStatistic);

        panelForStatisticFile.setVisible(false);
        mainPanel.add(panelForStatisticFile);

        //Панель для элементов установки ключа. Создается раньше, чтобы передать панель в слушатель изменения радиокнопок
        //По умолчанию видимость - false
        JPanel keyPanel     = new JPanel();
        JTextField keyField = new JTextField(10);
        JLabel keyLabel     = new JLabel("Set key: ");
        keyPanel.add(keyLabel);
        keyPanel.add(keyField);
        keyPanel.setVisible(false);

        //Панель для элементов радио-кнопок
        JPanel panelForRadioButton                  = new JPanel();
        ActionListener radioButtonActionListener    = new RadioButtonActionListener(cryptAnalyzer, keyPanel, panelForStatisticFile);
        JRadioButton encrypt                        = new JRadioButton("Encrypt");
        encrypt.setMnemonic(1);
        encrypt.addActionListener(radioButtonActionListener);
        JRadioButton decrypt                        = new JRadioButton("Decrypt");
        decrypt.setMnemonic(2);
        decrypt.addActionListener(radioButtonActionListener);
        JRadioButton cryptanalyzeBrutForce          = new JRadioButton("Crypanalyze(Brut Force)");
        cryptanalyzeBrutForce.setMnemonic(3);
        cryptanalyzeBrutForce.addActionListener(radioButtonActionListener);
        JRadioButton cryptanalyzeStatistic          = new JRadioButton("Cryptanalyz(Statistic)");
        cryptanalyzeStatistic.setMnemonic(4);
        cryptanalyzeStatistic.addActionListener(radioButtonActionListener);
        ButtonGroup radioButtonGroup                = new ButtonGroup();
        radioButtonGroup.add(encrypt);
        radioButtonGroup.add(decrypt);
        radioButtonGroup.add(cryptanalyzeBrutForce);
        radioButtonGroup.add(cryptanalyzeStatistic);
        panelForRadioButton.add(encrypt);
        panelForRadioButton.add(decrypt);
        panelForRadioButton.add(cryptanalyzeBrutForce);
        panelForRadioButton.add(cryptanalyzeStatistic);

        mainPanel.add(panelForRadioButton);
        mainPanel.add(keyPanel);

        //Панель кнопки выполнения
        JPanel executeButtonPanel           = new JPanel();
        JButton executeButton               = new JButton("Execute");
        JLabel erorrLabel                   = new JLabel();
        ActionListener executeLListener     = new ExecuteListener(cryptAnalyzer, erorrLabel, keyField);
        executeButton.addActionListener(executeLListener);
        executeButtonPanel.add(executeButton);

        mainPanel.add(executeButtonPanel);

        //Панель поля для вывода ошибок
        JPanel erorrLabelPanel = new JPanel();
        erorrLabelPanel.add(erorrLabel);

        mainPanel.add(erorrLabelPanel);

        //Панель вывода результата и принятия решения при использовании BrutForce
        //По умолчанию видимость - false. Панель по умолчанию передается в криптоанализатор
        JPanel panelForBrutForse                        = new JPanel();
        JTextArea textAreaForBrutForse                  = new JTextArea();
        JButton buttonOk                                = new JButton("Ok");
        ActionListener brutForceActionListinerOk        = new BrutForceListener(1, cryptAnalyzer);
        buttonOk.addActionListener(brutForceActionListinerOk);
        JButton buttonCanсel                            = new JButton("Cancel");
        ActionListener brutForceActionListinerCancel    = new BrutForceListener(2, cryptAnalyzer);
        buttonCanсel.addActionListener(brutForceActionListinerCancel);
        panelForBrutForse.add(textAreaForBrutForse);
        panelForBrutForse.add(buttonOk);
        panelForBrutForse.add(buttonCanсel);
        panelForBrutForse.setVisible(false);
        cryptAnalyzer.setPanelForBrutForse(panelForBrutForse);
        cryptAnalyzer.setAreaForBrutForce(textAreaForBrutForse);

        mainPanel.add(panelForBrutForse);

        //Добавление закладок на форму
        tabbedPane.add("Main",      mainPanel);
        tabbedPane.add("Settings",  settingPanel);
        add(tabbedPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,500);
        setVisible(true);

    }

}
