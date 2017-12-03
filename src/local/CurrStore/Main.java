package local.CurrStore;

import local.CurrStore.config.ConfigKeys;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.regex.Pattern;

public class Main {

    private final static String configFile = "config.xml";
    private final static String initCurFile = "initCur.txt";
    private List<Object> currs;

    private ConcurrentHashMap<String, Currency> currencyMap = new ConcurrentHashMap<>();


    public static void main(String[] args) {
        try {
            new Main();

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Main() throws ConfigurationException, ParseException {
        XMLConfiguration config = new XMLConfiguration(configFile);
        config.setListDelimiter(',');
        //разрешенные валюты
        currs = config.getList(ConfigKeys.CURR);

        if (currs == null || currs.size() == 0)
            throw new ParseException("Не найден конфиг с валютами", 0);

        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(1);

        scheduledExecutorService.scheduleAtFixedRate(
                () ->
                {
                    currencyMap.keySet().stream()
                            .filter(
                                    (curName) -> currencyMap.get(curName).getBill().compareTo(new BigDecimal(0)) != 0
                            )
                            .forEach(
                                    (curName) ->
                                    {
                                        System.out.println(curName + " " + currencyMap.get(curName).getBill().toString());
                                    }
                            );

                }, 0, 1, TimeUnit.MINUTES
        );

        //ввод с файла
        try {
            initCurFromFile();
        } catch (FileNotFoundException fne) {
            System.out.println("Файл не найден");
        }

        //ввод из консоли
        Scanner keyboard = new Scanner(System.in);
        while (true) {
            String inputString = keyboard.nextLine();

            if (inputString.equalsIgnoreCase("q"))
                System.exit(0);

            if (!isCurCodeInAccess(inputString))
                throw new ParseException("Неверная валюта", 0);

            String[] splitStr = inputString.split(" ");
            addToCurHashMap(splitStr[0], splitStr[1]);
        }
    }

    private void initCurFromFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(initCurFile));

        while (scanner.hasNext()) {
            String fileString = scanner.nextLine();

            if (isCurCodeInAccess(fileString)) {
                String[] splitStr = fileString.split(" ");
                addToCurHashMap(splitStr[0], splitStr[1]);
            }
        }
        scanner.close();
    }

    private boolean isCurCodeInAccess(String cCode) {
        return (currs.stream().anyMatch(cur -> (cCode.split(" "))[0].equalsIgnoreCase(cur.toString())));
    }


    private void addToCurHashMap(String curName, String curValue) {
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
