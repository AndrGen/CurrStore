package local.CurrStore;

import java.io.FileNotFoundException;
import java.util.List;

public interface FileReader {
    List<String> read(String name, String delimeter)  throws FileNotFoundException;
}
