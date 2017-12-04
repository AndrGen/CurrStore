package local.CurrStore;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class CurPay {
    private static List<String> currs;
    private final static String configFile = "configCurs.txt";
    private final static String initCurFile = "initCur.txt";
    private FileReader fileReader;
    private static ConcurrentHashMap<String, Currency> currencyMap = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, Currency> getCurrencyMap() {
        return currencyMap;
    }


    public void initCurFromFile() throws FileNotFoundException, ParseException
    {
        fileReader = new FileReaderConfigCur();
        //разрешенные валюты
        currs = fileReader.read(configFile, "");

        if (currs == null || currs.size() == 0)
            throw new ParseException("Не найден конфиг с валютами", 0);
        //ввод с файла
        try {
            fileReader = new FileReaderInitCur();
            fileReader.read(initCurFile, " ");
        } catch (FileNotFoundException fne) {
            System.out.println("Файл не найден");
        }
    }


    public boolean isCurCodeInAccess(String cCode) {
        return (currs.stream().anyMatch(cur -> (cCode.split(" "))[0].equalsIgnoreCase(cur)));
    }


    public void addToCurHashMap(String curName, String curValue) {
        String regex = "[-]?[0-9]*\\.?,?[0-9]+";
        Pattern pattern = Pattern.compile(regex);

        if (!pattern.matcher(curValue).matches())
            return;

        if (!currencyMap.containsKey(curName.toUpperCase()))
            currencyMap.put(curName.toUpperCase(), new Currency(curValue, new BigDecimal(curValue)));
        else {
            Currency currency = currencyMap.get(curName.toUpperCase());
            currency.addPaysHistory(curValue);
            currency.addBill(new BigDecimal(curValue));

            currencyMap.replace(curName, currency);
        }
    }
}
