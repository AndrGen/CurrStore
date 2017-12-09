package local.CurrStore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CuurencyTest")
public class CTest {
    Main main;

    private final String fileNamePart = "src\\test\\";

    @Nested
    @DisplayName("FileReaderConfigCur")
    class FileReaderTestConfigCur {
        private local.CurrStore.FileReader fileReader;

        @BeforeEach
        void createNewFileReaderConfigCur() {
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
    @DisplayName("FileReaderInitCur")
    class FileReaderTestInitCur {
        private local.CurrStore.FileReader fileReader;

        @BeforeEach
        void createNewFileReaderConfigCur() {
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
}
