package it.buniva.strage.service;

import it.buniva.strage.entity.Student;
import it.buniva.strage.entity.compositeatributte.PersonalData;
import it.buniva.strage.exception.classroom.ClassroomNotFoundException;
import it.buniva.strage.exception.csvfile.*;
import it.buniva.strage.exception.role.RoleNotFoundException;
import it.buniva.strage.exception.student.DuplicatePersonalDataException;
import it.buniva.strage.exception.student.EmptyStudentListException;
import it.buniva.strage.exception.student.StudentNotFoundException;
import it.buniva.strage.exception.user.DuplicateUsernameException;
import it.buniva.strage.exception.user.UserNotFoundException;
import it.buniva.strage.payload.request.PersonalDataRequest;
import it.buniva.strage.payload.request.StudentRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface StudentService {

    // ============================= CREATE ===============================
    Student registerStudent(StudentRequest studentRequest)
            throws DuplicateUsernameException, UserNotFoundException, RoleNotFoundException,
            StudentNotFoundException, DuplicatePersonalDataException, MessagingException, ClassroomNotFoundException;

    List<Student> registerStudentsFromCsvFile(String classroomName, MultipartFile csvFile) throws NoItemFoundInFileException, FileNotPresentException, CSVHeaderFieldNotFoundException, CSVEmailFormatException, CSVNumberFormatException, IOException, CSVInconsistentRecordException, CSVNameFormatException, TypeFileNotCorrectException, InvalidNumberHeaderFieldException, UserNotFoundException, MessagingException, DuplicateUsernameException, RoleNotFoundException, StudentNotFoundException, DuplicatePersonalDataException, ClassroomNotFoundException;

    // ============================== READ ================================
    Student getStudentByIdAndEnabledTrueAndDeletedFalse(Long studentId) throws StudentNotFoundException;

    Student getStudentByUserIdAndEnabledTrueAndDeletedFalse(Long userId) throws StudentNotFoundException;

    Student getStudentByUserIdAndDeletedFalse(Long userId) throws StudentNotFoundException;

    Student getStudentByIdAndDeletedFalse(Long studentId) throws StudentNotFoundException;

    Student getStudentByEmail(String email) throws StudentNotFoundException;

    List<Student> getAllStudentsByEnabledTrueAndDeletedFalse() throws EmptyStudentListException;

    void existsAlreadyStudentByPersonalData(PersonalData personalData) throws DuplicatePersonalDataException;

    void existsAlreadyStudentByPersonalDataOnUpdate(Long studentId, PersonalData personalData) throws DuplicatePersonalDataException;


    // ============================ UPDATE =================================
    Student updatePersonalData(Long studentId, PersonalDataRequest personalDataRequest) throws StudentNotFoundException, DuplicatePersonalDataException;

    Student enableDisableStudent(Long studentId) throws StudentNotFoundException, UserNotFoundException;

    Student updateEnabled(Long studentId, boolean enabled) throws StudentNotFoundException;

    Student enableDisableScedStudent(Long studentId) throws StudentNotFoundException;

    // ============================= DELETE ================================
    void deleteStudent(Long studentId) throws StudentNotFoundException, UserNotFoundException;

    // ============================== SAVE ==================================
    Student saveStudent(Student student);

    // ============================== OTHER ==================================

}
