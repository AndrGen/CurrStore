package local.CurrStore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.NoSuchElementException;
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
                    () -> assertTrue(currencyMap.containsKey("USD")),
                    () -> assertEquals(new BigDecimal(0), currencyMap.get("AUD").getBill()));

        }

        @org.junit.jupiter.api.Test
        @DisplayName("isCurCodeInAccess normal input")
        void isCurCodeInAccessTestNormal() throws FileNotFoundException, ParseException {
            curPay.initCurFromFile(fileNamePart);
            assertAll("isCurCodeInAccessTestNormal",
                    () -> assertTrue(curPay.isCurCodeInAccess("USD ")),
                    () -> assertTrue(curPay.isCurCodeInAccess("rub 1")),
                    () -> assertTrue(curPay.isCurCodeInAccess("rub 1.2")),
                    () -> assertTrue(curPay.isCurCodeInAccess("Czk -1.222222145")));
        }

        @org.junit.jupiter.api.Test
        @DisplayName("isCurCodeInAccess wrong input")
        void isCurCodeInAccessTestWrong() throws FileNotFoundException, ParseException {
            curPay.initCurFromFile(fileNamePart);
            assertAll("isCurCodeInAccessTestWrong",
                    () -> assertFalse(curPay.isCurCodeInAccess("ddd ")),
                    () -> assertFalse(curPay.isCurCodeInAccess("ruc 1")),
                    () -> assertFalse(curPay.isCurCodeInAccess("1 1.2")));
        }

        @org.junit.jupiter.api.Test
        @DisplayName("addToCurHashMapTest input")
        void addToCurHashMapTest() throws FileNotFoundException, ParseException {
            curPay.initCurFromFile(fileNamePart);
            curPay.addToCurHashMap("aud", "ttt");
            curPay.addToCurHashMap("EUR", "100");
            ConcurrentHashMap<String, Currency> currencyMap = CurPay.getCurrencyMap();
            assertAll("addToCurHashMapTest",
                    () -> assertTrue(currencyMap.containsKey("AUD")),
                    () -> assertEquals(new BigDecimal(0), currencyMap.get("AUD").getBill()),
                    () -> assertEquals(new BigDecimal(100), currencyMap.get("EUR").getBill()));
        }
    }

    @Nested
    @DisplayName("MainTest")
    class MainTest {
        @org.junit.jupiter.api.Test
        @DisplayName("throws FileNotFoundException when Incorrect File Name")
        void throwsExceptionWhenIncorrectFileName() {
            assertThrows(FileNotFoundException.class, () -> new Main(""));
        }

        @org.junit.jupiter.api.Test
        @DisplayName("throws ParseException when Incorrect currency name")
        void throwsExceptionWhenIncorrectCurName() {
            System.setIn(new ByteArrayInputStream("My string".getBytes()));
            assertThrows(ParseException.class, () -> new Main(fileNamePart));
        }


        @org.junit.jupiter.api.Test
        @DisplayName("main input")
        void mainInput()  throws FileNotFoundException, ParseException {
            String str = "RUB 18\nusd 1\nCNY -2.5\nhistory\n";

            System.setIn(new ByteArrayInputStream(str.getBytes()));
            try {
                new Main(fileNamePart);
            }
            catch (NoSuchElementException nsee)
            {
            }
            ConcurrentHashMap<String, Currency> currencyMap = CurPay.getCurrencyMap();
            assertAll("mainInput",
                    () -> assertTrue(currencyMap.containsKey("AUD")),
                    () -> assertEquals(new BigDecimal(-2.5), currencyMap.get("CNY").getBill()),
                    () -> assertEquals(new BigDecimal(9), currencyMap.get("RUB").getBill()));
        }
    }
}
