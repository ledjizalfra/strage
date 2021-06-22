package it.buniva.strage.constant;

import java.util.Locale;

public class CSVFileConstant {

    public static final String FILE_NOT_PRESENT_MSG = "No selected file to upload! Please do the checking.";
    public static final String CSV_FILE_TYPE_INCORRECT_MSG =
            "Error: the file is not the correct type. " +
            "Please upload a csv file.";

    public static final String CSV_INVALID_NUMBER_HEADER_FIELD_MSG =
            "Invalid number of column in header. " +
            "Expected '%d' column(s): [%s] " ;

    public static final String CSV_HEADER_FIELD_NOT_FOUND_MSG =
            "Header(s) %s not found in csv file. " +
            "Expected: [%s] " ;

    public static final String NO_ITEM_FOUND_IN_FILE_MSG = "There is no item in the file";

    public static final String CSV_INCONSISTENT_RECORD_MSG =
            "Record: the number of column don't matches " +
            "the number of header. " +
            "Expected %d column(s): [%s]. " +
            "Header(s) name missing: [%s] in [%s]" ;

    public static final String CSV_INVALID_NAME_FORMAT_MSG =
            "The column [%s] suppose to be a valid name. " +
            "Incorrect value: [%s] in row: [%s]";

    public static final String CSV_INVALID_EMAIL_MSG =
            "The column [%s] suppose to be an valid email. " +
            "Incorrect value: [%s] in row: [%s]";

    public static final String CSV_EXPECTED_NUMBER_MSG =
            "The column [%s] suppose to be a number. " +
            "Incorrect value: [%s] in row: [%s]";
}
