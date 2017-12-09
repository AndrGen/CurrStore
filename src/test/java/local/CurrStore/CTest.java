package local.CurrStore;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CuurencyTest")
class CTest {

    private final String fileNamePart = "src\\test\\";

    @Nested
    @DisplayName("FileReaderTestConfigCur")
    class FileReaderTestConfigCur {
        private FileReader fileReader;

        @BeforeEach
        void createNewFileReader() {
            fileReader = new FileReaderConfigCur();
        }

        @org.junit.jupiter.api.Test
        @DisplayName("throws FileNotFoundException when Incorrect File Name")
        void throwsExceptionWhenIncorrectFileName() {
            assertThrows(FileNotFoundException.class, () -> fileReader.read("", ""));
        }

        @org.junit.jupiter.api.Test
        @DisplayName("fileReader is not empty")
        void fileIsNotEmpty() {
            assertFalse(fileReader == null);
        }

        @org.junit.jupiter.api.Test
        @DisplayName("currency list is not empty")
        void currencyIsNotEmpty() throws FileNotFoundException {
            String absoluteFilePath = System.getProperty("user.dir")
                    + File.separator + fileNamePart + "resources" + File.separator + "configCurs.properties";
            List<String> curCodes = fileReader.read(absoluteFilePath, "");
            assertAll("currencyIsNotEmpty",
                    () -> assertFalse(curCodes.isEmpty()),
                    () -> assertTrue(curCodes.size() == 161),
                    () -> assertEquals("ZWL", curCodes.get(160), "Last Currency"));

        }
    }

    @Nested
    @DisplayName("FileReaderTestInitCur")
    class FileReaderTestInitCur {
        private FileReader fileReader;

        @BeforeEach
        void createNewFileReader() {
            fileReader = new FileReaderInitCur();
        }

        @org.junit.jupiter.api.Test
        @DisplayName("throws FileNotFoundException when Incorrect File Name")
        void throwsExceptionWhenIncorrectFileName() {
            assertThrows(FileNotFoundException.class, () -> fileReader.read("", ""));
        }

        @org.junit.jupiter.api.Test
        @DisplayName("fileReader is not empty")
        void fileIsNotEmpty() {
            assertFalse(fileReader == null);
        }
    }

    @Nested
    @DisplayName("CurPayTest")
    class CurPayTest {
        private CurPay curPay;

        @BeforeEach
        void createNewCurPay() {
            curPay = new CurPay();
        }

        @org.junit.jupiter.api.Test
        @DisplayName("throws FileNotFoundException when Incorrect File Name")
        void throwsExceptionWhenIncorrectFileName() {
            assertThrows(FileNotFoundException.class, () -> curPay.initCurFromFile(""));
        }

        @org.junit.jupiter.api.Test
        @DisplayName("curPay is not empty")
        void fileIsNotEmpty() {
            assertFalse(curPay == null);
        }

        @org.junit.jupiter.api.Test
        @DisplayName("currency list and currency map is not empty")
        void currencyIsNotEmpty() throws FileNotFoundException, ParseException {
            curPay.initCurFromFile(fileNamePart);
            ConcurrentHashMap<String, Currency> currencyMap = CurPay.getCurrencyMap();
            assertAll("currencyMapIsNotEmpty",
                    () -> assertFalse(currencyMap.isEmpty()),
                    () -> assertTrue(currencyMap.size() == 5),
                    () -> assertTrue(currencyMap.containsKey("USD")),
                    () -> assertEquals(new BigDecimal(0), currencyMap.get("AUD").getBill()));

        }

        @org.junit.jupiter.api.Test
        @DisplayName("isCurCodeInAccess normal input")
        void isCurCodeInAccessTestNormal() throws FileNotFoundException, ParseException  {
            curPay.initCurFromFile(fileNamePart);
            assertAll("isCurCodeInAccessTestNormal",
                    () -> assertTrue(curPay.isCurCodeInAccess("USD ")),
                    () -> assertTrue(curPay.isCurCodeInAccess("rub 1")),
                    () -> assertTrue(curPay.isCurCodeInAccess("rub 1.2")),
                    () -> assertTrue(curPay.isCurCodeInAccess("Czk -1.222222145")));
        }
    }
}
