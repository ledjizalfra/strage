package it.buniva.strage.constant;

import java.util.Locale;

public class ConfigurationConstant {

    public static final int LENGTH_CONFIGURATION_CODE = 5; //00000-99999

    public static final String CONFIGURATION_NOT_FOUND_BY_CODE_MSG = "Configuration not found with name: [%s].";

    public static final String CONFIGURATION_NOT_FOUND_BY_ID_MSG = "Configuration not found with questionId: [%s].";

    public static final String EMPTY_CONFIGURATION_LIST_MSG = "Empty configuration list.";

    public static final String DUPLICATE_CONFIGURATION_NAME_MSG = "Configuration name: [%s] already exist.";

    public static final String ARGUMENT_NOT_CONTAINS_ENOUGH_QUESTIONS_MSG = "Argument [%s] doesn't contains enough questions";

    public static final String QUESTION_NOT_CONTAINS_ENOUGH_ANSWERS_MSG = "Question [%s] doesn't contains enough answers";

    public static final String MIN_SCORE_TO_PASS_OUT_OF_IN_RANGE_MSG = "Min score to pass [%s] is out of range: [%s] - [%s]";

    public static final String MIN_SCORE_IS_BIGGER_THAN_MAX_SCORE_MSG = "Min score [%s] is bigger than max score [%s]";

    public static final String CORRECT_ANSWER_MARK_IS_NOT_SET_MSG = "The correct answer mark is not set";

    public static final String INCORRECT_ANSWER_MARK_IS_NOT_SET_MSG = "The incorrect answer mark is not set";

    public static final String QUESTION_CORRECT_ANSWER_MARK_IS_NOT_SET_MSG = "The correct answer mark for question [%s] is not set";

    public static final String QUESTION_INCORRECT_ANSWER_MARK_IS_NOT_SET_MSG = "The incorrect answer mark for question [%s] is not set";

    public static final String DEFAULT_ANSWER_NOT_FOUND_MSG = "Default answer not found";

    public static final String NO_WANT_ANSWER_NOT_FOUND_MSG = "No want answer not found";

    public static final String INCORRECT_ANSWER_MARK_MUST_BE_LEAST_THAN_CORRECT_ANSWER_MARK_MSG =
            "In configuration, The absolute value of incorrect answer mark [%s] is bigger than the correct answer mark [%s]";
}
