package it.buniva.strage.service.implementation;

import it.buniva.strage.constant.ClassroomConstant;
import it.buniva.strage.constant.StudentConstant;
import it.buniva.strage.constant.UserConstant;
import it.buniva.strage.entity.Classroom;
import it.buniva.strage.entity.Student;
import it.buniva.strage.entity.User;
import it.buniva.strage.entity.compositeatributte.PersonalData;
import it.buniva.strage.enumaration.MailObject;
import it.buniva.strage.enumaration.RoleName;
import it.buniva.strage.event.SendMailEvent;
import it.buniva.strage.exception.classroom.ClassroomNotFoundException;
import it.buniva.strage.exception.csvfile.*;
import it.buniva.strage.exception.role.RoleNotFoundException;
import it.buniva.strage.exception.student.DuplicatePersonalDataException;
import it.buniva.strage.exception.student.EmptyStudentListException;
import it.buniva.strage.exception.student.StudentNotFoundException;
import it.buniva.strage.exception.user.DuplicateUsernameException;
import it.buniva.strage.exception.user.UserNotFoundException;
import it.buniva.strage.payload.request.PersonalDataRequest;
import it.buniva.strage.payload.request.SendMailRequest;
import it.buniva.strage.payload.request.StudentRequest;
import it.buniva.strage.payload.request.UserRequest;
import it.buniva.strage.repository.StudentRepository;
import it.buniva.strage.service.ClassroomService;
import it.buniva.strage.service.MailService;
import it.buniva.strage.service.StudentService;
import it.buniva.strage.service.UserService;
import it.buniva.strage.utility.PasswordUtils;
import it.buniva.strage.utility.csvfile.ApacheCommonsCsvUtil;
import it.buniva.strage.utility.csvfile.StudentCSV;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.IllegalWriteException;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(rollbackFor = Exception.class)
public class StudentServiceImpl implements StudentService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordUtils passwordUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private ApplicationEventPublisher publisher;


    // ===================================================================
    // ======================== IMPLEMENTATIONS ==========================
    // ===================================================================

    // ============================= CREATE ===============================
    @Override
    public Student registerStudent(StudentRequest studentRequest)
            throws DuplicateUsernameException, UserNotFoundException, RoleNotFoundException,
            StudentNotFoundException, DuplicatePersonalDataException, MessagingException, ClassroomNotFoundException {

        PersonalData personalData = new PersonalData(
                studentRequest.getEmail(),
                studentRequest.getName(),
                studentRequest.getSurname()
        );

        // Throw an exception if exist a student with the same personalData
        existsAlreadyStudentByPersonalData(personalData);

        // Control if the classroom exist, throw exception if not exist
        Classroom classroom =
                classroomService.getClassroomByNameAndEnabledTrueAndDeletedFalse(
                        studentRequest.getClassroomName());

        // Generate the password
        String password = passwordUtils.generatePassword(UserConstant.LENGTH_PASSWORD_GENERATED);

        // Insert one user
        User newUser = userService.registerUser(new UserRequest(
                studentRequest.getEmail(),
                password,
                RoleName.STUDENT.name()
        ));

        // Create a new student and save in DB
        Student newStudent = createNewStudent(studentRequest, newUser, personalData, classroom);

        // Prepare the sendMailRequest
        SendMailRequest sendMailRequest = SendMailRequest.createFromStudent(
                newStudent,
                MailObject.CREDENTIALS_MAIL,
                password
        );

        // Publish an event, the listener would get it an send the mail to the user
        // we send the email:username and password
        publisher.publishEvent(new SendMailEvent(this, sendMailRequest));

        return getStudentByIdAndEnabledTrueAndDeletedFalse(newStudent.getId());
    }

    @Override
    public List<Student> registerStudentsFromCsvFile(
            String classroomName,
            MultipartFile csvFile)
            throws NoItemFoundInFileException, FileNotPresentException, CSVHeaderFieldNotFoundException,
            CSVEmailFormatException, CSVNumberFormatException, IOException,
            CSVInconsistentRecordException, CSVNameFormatException, TypeFileNotCorrectException,
            InvalidNumberHeaderFieldException, UserNotFoundException, MessagingException,
            DuplicateUsernameException, RoleNotFoundException, StudentNotFoundException,
            DuplicatePersonalDataException, ClassroomNotFoundException {

//        LOGGER.info("CLASSROOM NAME: " + classroomName);

        // Check if the classroom param is not null
        if(!StringUtils.isNotBlank(classroomName)) {
            throw new IllegalArgumentException(ClassroomConstant.ILLEGAL_ARGUMENT_CLASSROOM_NAME_MSG);
        }

        // Read the content of csv file
        List<StudentCSV> studentCSVList = ApacheCommonsCsvUtil.readFromCSVFile(csvFile);

        // Student list result
        List<Student> studentList = new ArrayList<>();

        for(StudentCSV studentCSV: studentCSVList) {
            // Create StudentRequest from StudentCSV
            StudentRequest studentRequest = StudentRequest.createFrom(studentCSV);

            // Add the classroom name in StudentRequest
            studentRequest.setClassroomName(classroomName);

            Student newStudent = registerStudent(studentRequest);

            studentList.add(newStudent);
        }

        return studentList;
    }


    // ============================== READ ================================
    @Override
    public Student getStudentByIdAndEnabledTrueAndDeletedFalse(Long studentId) throws StudentNotFoundException {
        Student student = studentRepository.findByIdAndEnabledTrueAndDeletedFalse(studentId);

        if (student == null) {
            throw new StudentNotFoundException(
                    String.format(StudentConstant.STUDENT_NOT_FOUND_BY_ID_MSG, studentId));
        }

        return student;
    }

    @Override
    public Student getStudentByUserIdAndEnabledTrueAndDeletedFalse(Long userId) throws StudentNotFoundException {
        Student student = studentRepository.findByUserUserIdAndEnabledTrueAndDeletedFalse(userId);

        if (student == null) {
            throw new StudentNotFoundException(
                    String.format(StudentConstant.STUDENT_NOT_FOUND_BY_USER_ID, userId));
        }

        return student;
    }

    @Override
    public Student getStudentByUserIdAndDeletedFalse(Long userId) throws StudentNotFoundException {
        Student student = studentRepository.findByUserUserIdAndDeletedFalse(userId);

        if (student == null) {
            throw new StudentNotFoundException(
                    String.format(StudentConstant.STUDENT_NOT_FOUND_BY_USER_ID, userId));
        }

        return student;
    }

    @Override
    public Student getStudentByIdAndDeletedFalse(Long studentId) throws StudentNotFoundException {
        Student student = studentRepository.findByIdAndDeletedFalse(studentId);

        if (student == null) {
            throw new StudentNotFoundException(
                    String.format(StudentConstant.STUDENT_NOT_FOUND_BY_ID_MSG, studentId));
        }

        return student;
    }

    @Override
    public Student getStudentByEmail(String email) throws StudentNotFoundException {
        Student student = studentRepository.findByPersonalDataEmailAndEnabledTrueAndDeletedFalse(email);
        if(student == null) {
            throw new StudentNotFoundException(
                    String.format(StudentConstant.STUDENT_NOT_FOUND_BY_EMAIL_MSG, email));
        }
        return student;
    }

    @Override
    public List<Student> getAllStudentsByEnabledTrueAndDeletedFalse() throws EmptyStudentListException {
        List<Student> studentList = studentRepository.findAllByEnabledTrueAndDeletedFalse();

        if (studentList.isEmpty()) {
            throw new EmptyStudentListException(StudentConstant.EMPTY_STUDENT_LIST_MSG);
        }

        return studentList;
    }

    @Override
    public List<Student> getAllStudentsInClassroom(String classroomName) throws EmptyStudentListException {
        List<Student> studentList = studentRepository.findAllByClassroomClassroomNameAndEnabledTrueAndDeletedFalse(classroomName);

        if (studentList.isEmpty()) {
            throw new EmptyStudentListException(StudentConstant.EMPTY_STUDENT_LIST_MSG);
        }

        return studentList;
    }

    @Override
    public void existsAlreadyStudentByPersonalData(PersonalData personalData) throws DuplicatePersonalDataException {
        boolean existStudent = studentRepository.existsByPersonalData(personalData);
        if(existStudent) {
            throw new DuplicatePersonalDataException(
                    String.format(StudentConstant.DUPLICATE_PERSONAL_DATA_MSG, personalData.toString()));
        }
    }


    @Override
    public void existsAlreadyStudentByPersonalDataOnUpdate(Long studentId, PersonalData personalData) throws DuplicatePersonalDataException {
        Student student = studentRepository.findStudentByPersonalData(personalData);
        if(student!=null && !student.getId().equals(studentId)) {
            // If another student is found with the same personal data, an exception is throw
            throw new DuplicatePersonalDataException(
                    String.format(StudentConstant.DUPLICATE_PERSONAL_DATA_MSG, personalData.toString()));
        }
    }

    // ============================ UPDATE =================================
    @Override
    public Student updatePersonalData(Long studentId, PersonalDataRequest personalDataRequest) throws StudentNotFoundException, DuplicatePersonalDataException {

        Student student = getStudentByIdAndEnabledTrueAndDeletedFalse(studentId);

        PersonalData personalData = new PersonalData(
                student.getPersonalData().getEmail(),
                personalDataRequest.getName(),
                personalDataRequest.getSurname());

        // Throw an exception if already exist
        existsAlreadyStudentByPersonalDataOnUpdate(studentId, personalData);

        student.setPersonalData(personalData);

        return saveStudent(student);
    }


    @Override
    public Student enableDisableStudent(Long studentId) throws StudentNotFoundException, UserNotFoundException {
        Student student = getStudentByIdAndDeletedFalse(studentId);
        student.setEnabled(!student.isEnabled());

        userService.updateEnable(student.getUser().getUserId(), student.isEnabled());

        return saveStudent(student);
    }

    @Override
    public Student updateEnabled(Long studentId, boolean enabled) throws StudentNotFoundException {
        Student student = getStudentByIdAndDeletedFalse(studentId);
        student.setEnabled(enabled);

        return saveStudent(student);
    }

    @Override
    public Student enableDisableScedStudent(Long studentId) throws StudentNotFoundException {
        Student student = getStudentByIdAndDeletedFalse(studentId);
        student.setScedEnabled(!student.isScedEnabled());

        return saveStudent(student);
    }

    // ============================= DELETE ================================
    @Override
    public void deleteStudent(Long studentId) throws StudentNotFoundException, UserNotFoundException {

        Student student = getStudentByIdAndDeletedFalse(studentId);
        student.setDeleted(true);
        saveStudent(student);

        userService.deleteUser(student.getUser().getUserId());
    }

    // ============================== SAVE ==================================
    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    // ============================== OTHER ==================================





    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================

    private Student createNewStudent(
            StudentRequest studentRequest,
            User user,
            PersonalData personalData,
            Classroom classroom) {

        Student newStudent = new Student();
        newStudent.setClassroom(classroom);
        newStudent.setPersonalData(personalData);
        newStudent.setUser(user);

        newStudent.setEnabled(true);
        newStudent.setDeleted(false);

        newStudent.setScedEnabled(false);

        newStudent.setCurrentQuizCode(null);

        // Save on DB
        return saveStudent(newStudent);
    }
}
