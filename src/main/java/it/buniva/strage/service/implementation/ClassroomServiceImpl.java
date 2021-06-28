package it.buniva.strage.service.implementation;

import it.buniva.strage.constant.ClassroomConstant;
import it.buniva.strage.entity.*;
import it.buniva.strage.exception.classroom.ClassroomNotFoundException;
import it.buniva.strage.exception.classroom.DuplicateClassroomNameException;
import it.buniva.strage.exception.classroom.EmptyClassroomListException;
import it.buniva.strage.exception.professor.ProfessorAlreadyExistsException;
import it.buniva.strage.exception.professor.ProfessorNotFoundException;
import it.buniva.strage.exception.student.EmptyStudentListException;
import it.buniva.strage.exception.subject.SubjectAlreadyExistsException;
import it.buniva.strage.exception.subject.SubjectNotFoundException;
import it.buniva.strage.payload.request.ClassroomListRequest;
import it.buniva.strage.payload.request.ClassroomRequest;
import it.buniva.strage.payload.request.ProfessorInClassRequest;
import it.buniva.strage.payload.request.SubjectInClassRequest;
import it.buniva.strage.payload.response.ClassroomResponse;
import it.buniva.strage.repository.ClassroomRepository;
import it.buniva.strage.service.ClassroomService;
import it.buniva.strage.service.ProfessorService;
import it.buniva.strage.service.StudentService;
import it.buniva.strage.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(rollbackFor = Exception.class)
public class ClassroomServiceImpl implements ClassroomService {

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private StudentService studentService;


    // ===================================================================
    // ======================== IMPLEMENTATIONS ==========================
    // ===================================================================

    // ============================= CREATE ===============================
    @Override
    public Classroom addClassroom(ClassroomRequest classroomRequest) throws DuplicateClassroomNameException, ClassroomNotFoundException {

        // Generate the classroomName
        String name = generateClassroomName(classroomRequest);

        // Can throw exception
        existsAlreadyClassroomByName(name);

        return createNewClassroom(classroomRequest, name);
    }

    @Override
    public List<Classroom> addManyClassroom(ClassroomListRequest classroomListRequest) throws DuplicateClassroomNameException, ClassroomNotFoundException {

        List<Classroom> classroomList = new ArrayList<>();
        for (ClassroomRequest classroomRequest: classroomListRequest.getClassroomRequestList()) {
            classroomList.add(addClassroom(classroomRequest));
        }

        return classroomList;
    }


    // ============================== READ ================================
    @Override
    public Classroom getClassroomByNameAndEnabledTrueAndDeletedFalse(String classroomName) throws ClassroomNotFoundException {
        Classroom classroom = classroomRepository.findByClassroomNameAndDeletedFalseAndEnabledTrue(classroomName);
        if(classroom == null) {
            throw new ClassroomNotFoundException(
                    String.format(ClassroomConstant.CLASSROOM_NOT_FOUND_BY_NAME_MSG, classroomName));
        }

        return classroom;
    }

    @Override
    public Classroom getClassroomByClassroomNameAndDeletedFalse(String classroomName) throws ClassroomNotFoundException {
        Classroom classroom = classroomRepository.findByClassroomNameAndDeletedFalse(classroomName);
        if(classroom == null) {
            throw new ClassroomNotFoundException(
                    String.format(ClassroomConstant.CLASSROOM_NOT_FOUND_BY_NAME_MSG, classroomName));
        }

        return classroom;
    }

    @Override
    public Classroom getClassroomByIdAndEnabledTrueAndDeletedFalse(Long classId) throws ClassroomNotFoundException {
        Classroom classroom = classroomRepository.findClassroomByIdAndDeletedFalseAndEnabledTrue(classId);
        if(classroom == null) {
            throw new ClassroomNotFoundException(
                    String.format(ClassroomConstant.CLASSROOM_NOT_FOUND_BY_ID_MSG, classId));
        }
        return classroom;
    }

    @Override
    public List<Classroom> getAllClassroomsAndEnabledTrueAndDeletedFalse() throws EmptyClassroomListException {
        List<Classroom> classroomList = classroomRepository.findAllByDeletedFalseAndEnabledTrue();
        if(classroomList.isEmpty()) {
            throw new EmptyClassroomListException(ClassroomConstant.EMPTY_CLASSROOM_LIST_MSG);
        }
        return classroomList;
    }

    @Override
    public List<Professor> getAllProfessorsInClassroom(String classroomName) throws ClassroomNotFoundException {
        Classroom classroom = getClassroomByClassroomNameAndDeletedFalse(classroomName);
        return new ArrayList<>(classroom.getProfessorSet());
    }

    @Override
    public List<Subject> getAllSubjectsInClassroom(String classroomName) throws ClassroomNotFoundException {
        Classroom classroom = getClassroomByClassroomNameAndDeletedFalse(classroomName);
        return new ArrayList<>(classroom.getSubjectSet());
    }

    @Override
    public List<Student> getAllStudentsInClassroom(String classroomName) throws EmptyStudentListException {
        return studentService.getAllStudentsInClassroom(classroomName);
    }

    @Override
    public void existsAlreadyClassroomByName(String name) throws DuplicateClassroomNameException {
        boolean existByName = classroomRepository.existsByClassroomName(name);
        if(existByName) {
            throw new DuplicateClassroomNameException(
                    String.format(
                            ClassroomConstant.DUPLICATE_CLASSROOM_NAME_MSG, name));
        }
    }

    // ============================ UPDATE =================================
    @Override
    public Classroom enableDisableClassroom(String classroomName) throws ClassroomNotFoundException {
        Classroom classroom = getClassroomByClassroomNameAndDeletedFalse(classroomName);
        classroom.setEnabled(!classroom.isEnabled());

        return saveClassroom(classroom);
    }


    @Override
    public void addProfessorInClassroom(ProfessorInClassRequest professorInClassRequest) throws ProfessorNotFoundException, ClassroomNotFoundException, ProfessorAlreadyExistsException {
        Professor professor = professorService.getProfessorByEmail(professorInClassRequest.getProfessorEmail());
        Classroom classroom = getClassroomByNameAndEnabledTrueAndDeletedFalse(professorInClassRequest.getClassroomName());
        if(classroom.getProfessorSet().contains(professor)) {
            throw new ProfessorAlreadyExistsException(
                    String.format(
                            ClassroomConstant.PROFESSOR_ALREADY_EXIST_IN_CLASSROOM_MSG,
                            professorInClassRequest.getProfessorEmail(),
                            professorInClassRequest.getClassroomName()));
        }

        classroom.getProfessorSet().add(professor);
        saveClassroom(classroom);
    }

    @Override
    public void addSubjectInClassroom(SubjectInClassRequest subjectInClassRequest) throws ClassroomNotFoundException, SubjectNotFoundException, SubjectAlreadyExistsException {
        Subject subject = subjectService.getSubjectBySubjectCodeAndEnabledTrueAndDeletedFalse(subjectInClassRequest.getSubjectCode());
        Classroom classroom = getClassroomByNameAndEnabledTrueAndDeletedFalse(subjectInClassRequest.getClassroomName());

        if(classroom.getSubjectSet().contains(subject)) {
            throw new SubjectAlreadyExistsException(
                    String.format(
                            ClassroomConstant.SUBJECT_ALREADY_EXIST_IN_CLASSROOM_MSG,
                            subjectInClassRequest.getSubjectCode(),
                            subjectInClassRequest.getClassroomName()));
        }

        classroom.getSubjectSet().add(subject);
        saveClassroom(classroom);
    }

    // ============================= DELETE ================================

    // ============================== SAVE ==================================
    @Override
    public Classroom saveClassroom(Classroom classRoom) {
        return classroomRepository.save(classRoom);
    }

    // ============================== OTHER ==================================



    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================

    private Classroom createNewClassroom(ClassroomRequest classroomRequest, String classroomName) {

        Classroom c = new Classroom();
        c.setClassroomName(classroomName);

        c.setAnnuity(classroomRequest.getAnnuity());
        c.setSection(classroomRequest.getSection());
        c.setDiscipline(classroomRequest.getDiscipline());

        c.setEnabled(true);
        c.setDeleted(false);

        // Save in DB
        return saveClassroom(c);
    }

    private String generateClassroomName(ClassroomRequest classroomRequest) {
        return classroomRequest.getAnnuity()
                + classroomRequest.getSection()
                + " " + classroomRequest.getDiscipline();
    }

}
