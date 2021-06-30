package it.buniva.strage.constant;

import java.util.Locale;

public class AnswerConstant {

    // OTHER ANSWER CODE
    public static final String DEFAULT_ANSWER_CODE = "DEFAULT-00";

    public static final String NO_WANT_ANSWER_CODE = "DEFAULT-01";

    public static final int LENGTH_ANSWER_CODE = 2;

    public static final String ANSWER_NOT_FOUND_BY_CODE_MSG = "Answer not found with name: [%s].";

    public static final String ANSWER_NOT_FOUND_BY_ID_MSG = "Answer not found with answerId: [%s].";

    public static final String EMPTY_ANSWER_LIST_MSG = "Empty answer list.";

    public static final String DUPLICATE_ANSWER_CODE_MSG = "Answer name: [%s] already exist.";

    public static final String INVALID_VALUE_OF_QUESTION_CODE_MSG = "Invalid value of question code. It must not be blank or empty.";

    public static final String INVALID_TYPE_OF_ANSWER_MSG = "Invalid type of answer. You must select only one type.";

    public static final String ANSWER_ALREADY_EXIST_IN_QUESTION_MSG =
            "Answer with content: [%s] already exist in the question with code: [%s]";

    public static final String ANSWER_TRUE_ALREADY_EXIST_IN_QUESTION_MSG =
            "There is already a correct answer for the question with: [%s]";

    public static final String DEFAULT_ANSWER_ALREADY_EXIST_MSG = "The default answer already exist.";

    public static final String NO_WANT_ANSWER_ALREADY_EXIST_MSG = "The no want to answer already exist";

    public static final String DUPLICATE_ANSWER_CONTENT_IN_QUESTION_MSG =
            "Answer with content: [%s] already exist for the question with code: [%s].";
}
