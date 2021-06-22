package it.buniva.strage.utility.csvfile;

import it.buniva.strage.constant.CSVFileConstant;
import it.buniva.strage.constant.RegexConst;
import it.buniva.strage.exception.csvfile.*;
import it.buniva.strage.utility.EmailValidatorApache;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ApacheCommonsCsvUtil {

    public static final String[] HEADERS_EXPECTED_EN = {"no", "email", "name", "surname"};
    public static final String FILE_TYPE = "text/csv";
    public static final Integer NUMBER_HEADER_EXPECTED_IN_FILE = HEADERS_EXPECTED_EN.length;


    /**
     * Read the csv file and return a list of StudentCSV{no, email, name, surname, role}
     * Parameter of valid csv file accepted:
     * -> delimiter/separator of field=";"
     * -> separator of record="\n\r"
     *
     * @param multipartFileCSV
     * @return
     * @throws IOException
     */
    public static List<StudentCSV> readFromCSVFile(MultipartFile multipartFileCSV)
            throws IOException, NoItemFoundInFileException, FileNotPresentException,
            TypeFileNotCorrectException, InvalidNumberHeaderFieldException,
            CSVHeaderFieldNotFoundException, CSVInconsistentRecordException, CSVEmailFormatException,
            CSVNumberFormatException, CSVNameFormatException {

        // Checking if the file is present
        if (!StringUtils.isNotEmpty(multipartFileCSV.getOriginalFilename())) { // If it is empty
            throw new FileNotPresentException(
                    CSVFileConstant.FILE_NOT_PRESENT_MSG);
        }

        // checking the upload file's type is CSV or NOT
        if (!ApacheCommonsCsvUtil.hasCSVExtension(multipartFileCSV)) {
            throw new TypeFileNotCorrectException(
                    CSVFileConstant.CSV_FILE_TYPE_INCORRECT_MSG);
        }

        // Can throw exception: IOException
        InputStream inputStream = multipartFileCSV.getInputStream();

        // To handle files that start with a Byte Order Mark (BOM) like some Excel CSV files,
        // you need an extra step to deal with these optional bytes.
        // You can use the BOMInputStream class from Apache Commons IO
        // The problem is, that the UTF-8 representation prepends the following characters "0xEF,0xBB,0xBF"...
        // https://stackoverflow.com/questions/53643191/apache-commons-csv-mapping-not-found/61815006#61815006?newreg=1dd73f390b2f45d5b4d0476d8525e68b
        Reader reader = new InputStreamReader(new BOMInputStream(inputStream), StandardCharsets.UTF_8);

        BufferedReader bufferedReader = new BufferedReader(reader);

        // Set the format for the parsing
        CSVFormat csvFormat = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase(true)
                .withTrim(true)
                .withDelimiter(';');

        CSVParser csvParser = new CSVParser(bufferedReader, csvFormat);

        // Convert all headers in lowercase to allow insensitive case
        List<String> headerFromFile = csvParser.getHeaderNames().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        if (headerFromFile.size() != NUMBER_HEADER_EXPECTED_IN_FILE) {
            throw new InvalidNumberHeaderFieldException(
                    String.format(
                            CSVFileConstant.CSV_INVALID_NUMBER_HEADER_FIELD_MSG,
                            NUMBER_HEADER_EXPECTED_IN_FILE,
                            String.join("; ", HEADERS_EXPECTED_EN)
                    )
            );
        }

        // Get all the headers not found from file
        List<String> headerNotFound = getHeaderNotFound(HEADERS_EXPECTED_EN, headerFromFile);
        if (!headerNotFound.isEmpty()) {
            throw new CSVHeaderFieldNotFoundException(
                    String.format(
                            CSVFileConstant.CSV_HEADER_FIELD_NOT_FOUND_MSG,
                            headerNotFound,
                            String.join("; ", HEADERS_EXPECTED_EN)
                    )
            );
        }

        List<CSVRecord> csvRecordList = csvParser.getRecords();
        // Check if the file is Empty and throw exception: NoItemFoundInFileException
        if (csvRecordList.isEmpty()) {
            throw new NoItemFoundInFileException(
                    CSVFileConstant.NO_ITEM_FOUND_IN_FILE_MSG);
        }


        List<StudentCSV> studentCSVList = new ArrayList<>();
        for (CSVRecord record : csvRecordList) {

            // Get all the inconsistent values
            List<String> valuesPresentInRecord = getValuesPresentInRecord(record);

            // Get all values present in a record
            List<String> allValuesOfRecord = getAllValuesOfRecord(record);

            // Control if the size of the current record matches
            // the size of the header row
            if (valuesPresentInRecord.size() < allValuesOfRecord.size()) {
                // Get the missing value in record
                List<String> missingColumn = getMissingColumnFromRecord(record);
                throw new CSVInconsistentRecordException(
                        String.format(
                                CSVFileConstant.CSV_INCONSISTENT_RECORD_MSG,
                                NUMBER_HEADER_EXPECTED_IN_FILE,
                                String.join(", ", HEADERS_EXPECTED_EN),
                                String.join(", ", missingColumn),
                                String.join("; ", valuesPresentInRecord)
                        )
                );
            } else { // THE RECORD IS CORRECT
                // Each method throws an exception in case of error
                validateColumnOfRecord(record);

                // Create studentCSV object from a record
                StudentCSV studentCSV = createStudentCSVFrom(record);

                // Add to the list result
                studentCSVList.add(studentCSV);
            }

        }

        return studentCSVList;
    }



    // ==================================================
    // == PRIVATE METHOD
    // ==================================================

    /**
     * Create a StudentCSV object from a given CSVRecord
     * The get method throw Exceptions:
     * IllegalStateException – if no header mapping was provided
     * IllegalArgumentException – if name is not mapped or if the record is inconsistent
     *
     * @param csvRecord
     * @return
     */
    private static StudentCSV createStudentCSVFrom(CSVRecord csvRecord) {
//        try {
        return new StudentCSV(
                Long.parseLong(csvRecord.get("no")),
                csvRecord.get("email"),
                csvRecord.get("name"),
                csvRecord.get("surname")
                /*csvRecord.get("role")*/
        );
        /*} catch (IllegalStateException | IllegalArgumentException exp1) {
            exp1.printStackTrace();
        }

        return new StudentCSV();*/
    }

    private static void validateColumnOfRecord(CSVRecord record)
            throws CSVEmailFormatException, CSVNumberFormatException, CSVNameFormatException {

        //initialize the Pattern object
        Pattern pattern = Pattern.compile(RegexConst.NUMBER_REGEX);

        // no column
        if (!pattern.matcher(record.get("no")).matches()) {
            throw new CSVNumberFormatException(
                    String.format(
                            CSVFileConstant.CSV_EXPECTED_NUMBER_MSG,
                            "no", record.get("no"),
                            String.join("; ", getAllValuesOfRecord(record))
                    )
            );
        }

        // email column
        if (!EmailValidatorApache.isValid(record.get("email"))) {
            throw new CSVEmailFormatException(
                    String.format(
                            CSVFileConstant.CSV_INVALID_EMAIL_MSG,
                            "email", record.get("email"),
                            String.join("; ", getAllValuesOfRecord(record))
                    )
            );
        }

        // name and surname column
        pattern = Pattern.compile(RegexConst.NAME_SURNAME_REGEX);
        if (!pattern.matcher(record.get("name")).matches()) {
            throw new CSVNameFormatException(
                    String.format(
                            CSVFileConstant.CSV_INVALID_NAME_FORMAT_MSG,
                            "name", record.get("name"),
                            String.join("; ", getAllValuesOfRecord(record))
                    )
            );
        }
        pattern = Pattern.compile(RegexConst.NAME_SURNAME_REGEX);
        if (!pattern.matcher(record.get("surname")).matches()) {
            throw new CSVNameFormatException(
                    String.format(
                            CSVFileConstant.CSV_INVALID_NAME_FORMAT_MSG,
                            "surname", record.get("surname"),
                            String.join("; ", getAllValuesOfRecord(record))
                    )
            );
        }

    }

    /**
     * Get all not found header(s)
     *
     * @param headersExpected
     * @param headerFromFile
     * @return
     */
    private static List<String> getHeaderNotFound(
            String[] headersExpected,
            List<String> headerFromFile) {

        List<String> headerNotFound = new ArrayList<>();
        for (String hExpected : headersExpected) {
            if (!headerFromFile.contains(hExpected)) {
                headerNotFound.add(hExpected);
            }
        }
        return headerNotFound;
    }

    /**
     * Get all the missing column (header) for a record
     *
     * @param record
     * @return
     */
    private static List<String> getMissingColumnFromRecord(CSVRecord record) {
        List<String> missingColumn = new ArrayList<>();

        for (String column : record.getParser().getHeaderNames()) {
            if (!StringUtils.isNotEmpty(record.get(column))) { // If the column is empty
                missingColumn.add(column);
            }
        }

        return missingColumn;
    }

    /**
     * Get all the values present in a record (row)
     *
     * @param record
     * @return
     */
    private static List<String> getValuesPresentInRecord(CSVRecord record) {
        List<String> valuesListFound = new ArrayList<>();
        int numberOfColumn = record.getParser().getHeaderNames().size();
        for (int i = 0; i < numberOfColumn; i++) {
            if (StringUtils.isNotEmpty(record.get(i))) {
                valuesListFound.add(record.get(i));
            }
        }

        return valuesListFound;
    }


    private static List<String> getAllValuesOfRecord(CSVRecord record) {
        List<String> allValuesListFound = new ArrayList<>();
        int numberOfColumn = record.getParser().getHeaderNames().size();
        for (int i = 0; i < numberOfColumn; i++) {
            allValuesListFound.add(record.get(i));
        }
        return allValuesListFound;
    }

    private static boolean hasCSVExtension(MultipartFile multipartFileCSV) {
        return FILE_TYPE.equals(multipartFileCSV.getContentType());
    }

}
