package local.CurrStore;


import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        try {
            new Main();

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Main() throws ParseException, FileNotFoundException {

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
        curPay.initCurFromFile();
        //ввод из консоли
        Scanner keyboard = new Scanner(System.in);
        while (true) {
            String inputString = keyboard.nextLine();

            if (inputString.equalsIgnoreCase("q"))
                System.exit(0);

            if (!curPay.isCurCodeInAccess(inputString))
                throw new ParseException("Неверная валюта", 0);

            String[] splitStr = inputString.split(" ");
            curPay.addToCurHashMap(splitStr[0], splitStr[1]);
        }

    }

}
