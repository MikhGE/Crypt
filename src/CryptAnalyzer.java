import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CryptAnalyzer {
    private String cryptAlphabet = "";
    private String fileNameForExecute   = "";
    private String fileNameForResult    = "";

    public String getFileNameForStatistic() {
        return fileNameForStatistic;
    }

    public void setFileNameForStatistic(String fileNameForStatistic) {
        this.fileNameForStatistic = fileNameForStatistic;
    }

    private String fileNameForStatistic = "";
    private int mode                    = 0;
    private int key                     = 0;
    private StringBuilder erorrs        = new StringBuilder("");
    private JTextArea areaForBrutForce;
    private JPanel panelForBrutForse;

    public CryptAnalyzer() {
        String cyrillicAlphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
        String symbols			= ".,\":-!? ";
        cryptAlphabet	        = cyrillicAlphabet + symbols;
    }

    public String getCryptAlphabet() {
        return new String(cryptAlphabet);
    }

    public void setCryptAlphabet(String cryptAlphabet) {
        this.cryptAlphabet = cryptAlphabet;
    }

    public String getFileNameForExecute() {
        return fileNameForExecute;
    }

    public void setFileNameForExecute(String fileNameForExecute) {
        this.fileNameForExecute = fileNameForExecute;
    }

    public String getFileNameForResult() {
        return fileNameForResult;
    }

    public void setFileNameForResult(String fileNameForResult) {
        this.fileNameForResult = fileNameForResult;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public Boolean check(){
        if(fileNameForExecute.isEmpty())
            erorrs.append("File for execute not selected \n");
        if (fileNameForResult.isEmpty())
            erorrs.append("File for result not selected \n");
        if (mode==0)
            erorrs.append("Mode not selected \n");
        if((mode==1||mode==2)&&key==0)
            erorrs.append("Key is empty \n");

        return erorrs.toString().isEmpty();
    }

    public String getErorrs() {
        return erorrs.toString();
    }

    public void clearErorrs(){
        erorrs = new StringBuilder("");
    }

    public void execute(){
        Path path = Paths.get(getFileNameForExecute());
        if(Files.exists(path)){
            switch (getMode()){
                case 1:
                    EncryptDecrypt(1);
                    break;
                case 2:
                    EncryptDecrypt(-1);
                    break;
                case 3:
                    DecryptBrutForce();
                    break;
                case 4:
                    DecryptStatistic();
                    break;
                default:
                    erorrs.append("Mode not selected \n");
                    return;
            }
        }

    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setAreaForBrutForce(JTextArea areaForBrutForce) {
        this.areaForBrutForce = areaForBrutForce;
    }

    private void EncryptDecrypt(int modKey){
        String encryptAlphabet      = getCryptAlphabet();
        int encryptAlphabetLength   = encryptAlphabet.length();
        Path fileForExecute = Paths.get(getFileNameForExecute());
        Path fileForResult  = Paths.get(getFileNameForResult());
        int subKey = (getMode() == 1)? getKey(): encryptAlphabetLength - getKey();
        try(BufferedReader bufferedReader = Files.newBufferedReader(fileForExecute);
            BufferedWriter bufferedWriter = Files.newBufferedWriter(fileForResult);){
            while (bufferedReader.ready()){
                String nextString   = bufferedReader.readLine();
                String resultString = "";
                for (Character c : nextString.toCharArray()) {
                    String charToString = Character.toString(c);
                    int index    = encryptAlphabet.indexOf(charToString.toUpperCase());
                    if(index>=0) {
                        int newIndex = (index + subKey) % encryptAlphabetLength;
                        resultString += encryptAlphabet.charAt(newIndex);
                    }
                    else
                        resultString += charToString;
                }
                bufferedWriter.write(resultString);
                bufferedWriter.flush();
            }
        }
        catch (IOException e){

        }
    }

    private void DecryptBrutForce(){
        String decryptAlphabet         = getCryptAlphabet();
        int encryptAlphabetLength       = decryptAlphabet.length();
        Path fileForExecute = Paths.get(getFileNameForExecute());
        Path fileForResult  = Paths.get(getFileNameForResult());
        try(BufferedReader bufferedReader = Files.newBufferedReader(fileForExecute);
            BufferedWriter bufferedWriter = Files.newBufferedWriter(fileForResult);){
            while (bufferedReader.ready()) {
                String nextString = bufferedReader.readLine();
                for (int i = 0; i < encryptAlphabetLength; i++) {
                    String resultString = "";
                    setKey(i);
                    for (Character c : nextString.toCharArray()) {
                        String charToString = Character.toString(c);
                        int index   = decryptAlphabet.indexOf(charToString.toUpperCase());
                        int subKey  = encryptAlphabetLength - getKey();
                        if(index>=0) {
                            int newIndex = (index + subKey) % encryptAlphabetLength;
                            resultString += decryptAlphabet.charAt(newIndex);
                        }
                        else
                            resultString += charToString;
                    }
                    if(CheckString(resultString)){
                        bufferedWriter.write(resultString);
                        bufferedWriter.flush();
                        break;
                    }
                }
            }

        }
        catch (IOException e){

        }
    }

    private void DecryptStatistic(){
        Path fileForExecute     = Paths.get(getFileNameForExecute());
        Path fileForResult      = Paths.get(getFileNameForResult());
        Path fileForStatistic   = Paths.get(getFileNameForStatistic());
        HashMap<String, Integer> statisticFromStatisticFile = new HashMap();
        HashMap<String, Integer> statisticFromExecuteFile   = new HashMap();
        //Предполагаю, что текст шифровался при помощи того же криптоалфавита
        //В противном случае возникает ситуация,когда состав ключей статистик различается
        //TODO: Необходимо доработать метод для случая различных криптоалфавитов
        for (Character c : getCryptAlphabet().toCharArray()) {
            String charToString = Character.toString(c);
            statisticFromStatisticFile.put(charToString.toUpperCase(),  0);
            statisticFromExecuteFile.put(charToString.toUpperCase(),    0);
        }
        //Собираем статистику из файла статистики
        int countCharFromStatisticFile = 0;
        if (Files.exists(fileForStatistic)){
            try(BufferedReader bufferedReader = Files.newBufferedReader(fileForStatistic)){
                while (bufferedReader.ready()){
                String newString = bufferedReader.readLine();
                    for (Character c : newString.toCharArray()) {
                        countCharFromStatisticFile++;
                        String charToString = Character.toString(c);
                        if(statisticFromStatisticFile.containsKey(charToString.toUpperCase())){
                            statisticFromStatisticFile.put(charToString.toUpperCase(), statisticFromStatisticFile.get(charToString.toUpperCase()) + 1);
                        }
                    }
                }
            }catch (IOException e){

            }
        }
        else
            return;

        // Собираем статистику из файла? который нужно расшифровать
        int countCharFromExecuteFile = 0;
        if(Files.exists(fileForExecute)){
            try(BufferedReader bufferedReader = Files.newBufferedReader(fileForExecute)){
                while (bufferedReader.ready()){
                    String newString = bufferedReader.readLine();
                    for (Character c : newString.toCharArray()) {
                        countCharFromExecuteFile++;
                        String charToString = Character.toString(c);
                        if(statisticFromExecuteFile.containsKey(charToString.toUpperCase())){
                            statisticFromExecuteFile.put(charToString.toUpperCase(), statisticFromExecuteFile.get(charToString.toUpperCase()) + 1);
                        }
                    }
                }
            }catch (IOException e){

            }
        }
        else
            return;

        // Сортируем полученные результаты
        List listStatistikFromFileStatistic = new ArrayList(statisticFromStatisticFile.entrySet());
        Collections.sort(listStatistikFromFileStatistic, Collections.reverseOrder(new StatusticComporator()));

        List listStatisticFromFileExecuteFile = new ArrayList(statisticFromExecuteFile.entrySet());
        Collections.sort(listStatisticFromFileExecuteFile, Collections.reverseOrder(new StatusticComporator()));

        try(BufferedReader bufferedReader = Files.newBufferedReader(fileForExecute);
            BufferedWriter bufferedWriter = Files.newBufferedWriter(fileForResult);){

            while (bufferedReader.ready()){
                String nextLine = bufferedReader.readLine();
                String nextResultString = nextLine;
                Set replacementSet = new HashSet();
                for (Object listItem: listStatisticFromFileExecuteFile) {
                    Map.Entry<String, Integer> mapEntryExecute      = (Map.Entry<String, Integer>) listItem;
                    Map.Entry<String, Integer> mapEntryStatistic    = (Map.Entry<String, Integer>) listStatistikFromFileStatistic.get(listStatisticFromFileExecuteFile.indexOf(listItem));
                    String regex        = mapEntryExecute.getKey();
                    String replacement  = mapEntryStatistic.getKey();
                    regex               = checkRegex(regex);
                    replacement         = checkRegex(replacement);
                    if(!replacementSet.contains(regex)) {
                        nextResultString = nextResultString.replaceAll(regex, replacement);
                        replacementSet.add(replacement);
                    }
                }
                bufferedWriter.write(nextResultString);
                bufferedWriter.flush();
            }
        }catch (IOException e){

        }



    }

    private String checkRegex(String regex){
        regex = (regex.equals("?"))?"\\?":regex;
        regex = (regex.equals("."))?"\\.":regex;
        return regex;
    }

    private boolean CheckString(String resultString){
        String consonants = "БВГДЖЗЙКЛМНПРСТФХЧЦШЩ";
        String vowels     = "АЕЁИОУЫЭЮЯ";
        String[] words = resultString.split(" ");
        //Нет слов
        if(words.length==0)
            return false;
        //Одно слово длинее 10 символов
        if(resultString.length()>=10&&words.length==1)
            return false;
        int countRightPunctuation   = 0;
        int allPunctuation          = 0;
        int countConsonants         = 0;
        int countVowels             = 0;
        int countIncorrectSequence  = 0;
        char[] arrayOfChar = resultString.toCharArray();
        for (int i = 0; i< arrayOfChar.length; i++) {
            Character c = arrayOfChar[i];
            String charToString = Character.toString(c);
            if(charToString.equals(".")||charToString.equals(",")||charToString.equals("!")||charToString.equals("?")||charToString.equals(":")){
                allPunctuation++;
                if(i<arrayOfChar.length&&(i+1)<arrayOfChar.length){
                    Character nc = arrayOfChar[i+1];
                    String newCharToString = Character.toString(nc);
                    if(newCharToString.equals(" ")){
                        countRightPunctuation++;
                    }
                }
            }
            if(consonants.indexOf(charToString.toUpperCase())>=0){
                if(countVowels>3)
                    countIncorrectSequence++;
                if(countVowels>0)
                    countVowels=0;
                countConsonants++;
            }
            else if(vowels.indexOf(charToString.toUpperCase())>=0){
                if(countConsonants>4)
                    countIncorrectSequence++;
                if(countConsonants>0)
                    countConsonants=0;
                countVowels++;
            }

        }
        if(countIncorrectSequence>1)
            return false;

        if(countRightPunctuation>1)
            return true;

        return false;
    }

    public void setPanelForBrutForse(JPanel panelForBrutForse) {

        this.panelForBrutForse = panelForBrutForse;
    }
}
