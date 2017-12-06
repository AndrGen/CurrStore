package local.CurrStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReaderConfigCur implements FileReader {
    public List<String> read(String name, String delimeter)  throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(name));
        List<String> curCodes = new ArrayList<>();
        while (scanner.hasNext()) {
             curCodes.add(scanner.nextLine());
        }
       scanner.close();
       return curCodes;
    }
}
