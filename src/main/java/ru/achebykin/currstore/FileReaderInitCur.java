package ru.achebykin.currstore;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class FileReaderInitCur implements FileReader {
    public List<String> read(String name, String delimeter) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(name));
        CurPay curPay = new CurPay();
        while (scanner.hasNext()) {
            String fileString = scanner.nextLine();

            if (curPay.isCurCodeInAccess(fileString)) {
                String[] splitStr = fileString.split(" ");
                curPay.addToCurHashMap(splitStr[0], splitStr[1]);

                Main.getLogger().debug("init_file.add cur_code = {}, cur_value = {}", splitStr[0], splitStr[1]);
            }
        }
        scanner.close();
        return null;
    }
}
