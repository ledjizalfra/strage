package it.buniva.strage.service.implementation;

import it.buniva.strage.constant.ClassroomConstant;
import it.buniva.strage.entity.Classroom;
import it.buniva.strage.entity.User;
import it.buniva.strage.exception.classroom.ClassroomNotFoundException;
import it.buniva.strage.exception.classroom.DuplicateClassroomNameException;
import it.buniva.strage.exception.classroom.EmptyClassroomListException;
import it.buniva.strage.payload.request.ClassroomListRequest;
import it.buniva.strage.payload.request.ClassroomRequest;
import it.buniva.strage.repository.ClassroomRepository;
import it.buniva.strage.service.ClassroomService;
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

        Classroom classroom = createNewClassroom(classroomRequest, name);

        return getClassroomByIdAndEnabledTrueAndDeletedFalse(classroom.getId());
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
