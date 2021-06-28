package it.buniva.strage.constant;

public class QuestionConstant {

    public static final int LENGTH_QUESTION_CODE = 5; //00000-99999

    public static final String QUESTION_NOT_FOUND_BY_CODE_MSG = "Question not found with name: [%s].";

    public static final String QUESTION_NOT_FOUND_BY_ID_MSG = "Question not found with questionId: [%s].";

    public static final String EMPTY_QUESTION_LIST_MSG = "Empty question list.";

    public static final String DUPLICATE_QUESTION_CODE_MSG = "Question name: [%s] already exist.";

    public static final String DUPLICATE_QUESTION_CONTENT_IN_ARGUMENT_MSG =
            "Question with content: [%s] already exist for the argument with code: [%s].";
}
