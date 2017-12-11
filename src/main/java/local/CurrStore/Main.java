package local.CurrStore;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static Logger getLogger() {
        return logger;
    }

    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                CurPay.setInitCurFile(args[0]);
            }

            new Main("");

            logger.info("Старт приложения");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Main(String fileNamePart) throws ParseException, FileNotFoundException {

        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(1);

        scheduledExecutorService.scheduleAtFixedRate(
                () ->
                {
                    CurPay.getCurrencyMap().keySet().stream()
                            .filter(
                                    (curName) -> CurPay.getCurrencyMap().get(curName).getBill().compareTo(new BigDecimal(0)) != 0
                            )
                            .forEach(
                                    (curName) ->
                                    {
                                        System.out.println(curName + " " + CurPay.getCurrencyMap().get(curName).getBill().toString());
                                    }
                            );

                }, 0, 1, TimeUnit.MINUTES
        );

        CurPay curPay = new CurPay();
        curPay.initCurFromFile(fileNamePart);
        //ввод из консоли
        Scanner keyboard = new Scanner(System.in);
        while (true) {
            String inputString = keyboard.nextLine().toUpperCase();

            switch (inputString) {
                case "Q":
                    System.exit(0);
                    break;
                case "HISTORY":
                    CurPay.getCurrencyMap().keySet()
                            .forEach(
                                    (curName) ->
                                    {
                                        System.out.println(curName + " -> \n " + CurPay.getCurrencyMap().get(curName).toString());
                                    }
                            );
                    break;
                default:
                    if (!curPay.isCurCodeInAccess(inputString))
                        throw new ParseException("Неверная валюта", 0);

                    String[] splitStr = inputString.split(" ");
                    curPay.addToCurHashMap(splitStr[0], splitStr[1]);
                    break;
            }
        }

    }

}
