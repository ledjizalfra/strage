package it.buniva.strage.service;

import it.buniva.strage.entity.Classroom;
import it.buniva.strage.exception.classroom.ClassroomNotFoundException;
import it.buniva.strage.exception.classroom.DuplicateClassroomNameException;
import it.buniva.strage.exception.classroom.EmptyClassroomListException;
import it.buniva.strage.payload.request.ClassroomListRequest;
import it.buniva.strage.payload.request.ClassroomRequest;

import java.util.List;

public interface ClassroomService {

    // ============================= CREATE ===============================
    Classroom addClassroom(ClassroomRequest classroomRequest) throws DuplicateClassroomNameException, ClassroomNotFoundException;

    List<Classroom> addManyClassroom(ClassroomListRequest classroomListRequest) throws DuplicateClassroomNameException, ClassroomNotFoundException;

    // ============================== READ ================================
    Classroom getClassroomByNameAndEnabledTrueAndDeletedFalse(String classroomName) throws ClassroomNotFoundException;

    Classroom getClassroomByClassroomNameAndDeletedFalse(String classroomName) throws ClassroomNotFoundException;

    Classroom getClassroomByIdAndEnabledTrueAndDeletedFalse(Long classId) throws ClassroomNotFoundException;

    List<Classroom> getAllClassroomsAndEnabledTrueAndDeletedFalse() throws EmptyClassroomListException;

    void existsAlreadyClassroomByName(String name) throws DuplicateClassroomNameException;

    // ============================ UPDATE =================================
    Classroom enableDisableClassroom(String classroomName) throws ClassroomNotFoundException;

    // ============================= DELETE ================================


    // ============================== SAVE ==================================
    Classroom saveClassroom(Classroom classRoom);


    // ============================== OTHER ==================================

}
