package it.buniva.strage.service;

import it.buniva.strage.entity.Classroom;
import it.buniva.strage.entity.Professor;
import it.buniva.strage.entity.Student;
import it.buniva.strage.entity.Subject;
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

import java.util.Arrays;
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

    List<Professor> getAllProfessorsInClassroom(String classroomName) throws ClassroomNotFoundException;

    List<Subject> getAllSubjectsInClassroom(String classroomName) throws ClassroomNotFoundException;

    List<Student> getAllStudentsInClassroom(String classroomName) throws EmptyStudentListException;

    void existsAlreadyClassroomByName(String name) throws DuplicateClassroomNameException;

    // ============================ UPDATE =================================
    Classroom enableDisableClassroom(String classroomName) throws ClassroomNotFoundException;

    void addProfessorInClassroom(ProfessorInClassRequest professorInClassRequest) throws ProfessorNotFoundException, ClassroomNotFoundException, ProfessorAlreadyExistsException;

    void addSubjectInClassroom(SubjectInClassRequest subjectInClassRequest) throws ClassroomNotFoundException, SubjectNotFoundException, ProfessorAlreadyExistsException, SubjectAlreadyExistsException;

    // ============================= DELETE ================================


    // ============================== SAVE ==================================
    Classroom saveClassroom(Classroom classRoom);


    // ============================== OTHER ==================================

}
