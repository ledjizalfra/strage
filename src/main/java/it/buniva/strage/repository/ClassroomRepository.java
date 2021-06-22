package it.buniva.strage.repository;

import it.buniva.strage.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

    boolean existsByClassroomName(String name);

    Classroom findClassroomByIdAndDeletedFalseAndEnabledTrue(Long classId);

    Classroom findByClassroomNameAndDeletedFalseAndEnabledTrue(String classroomName);

    List<Classroom> findAllByDeletedFalseAndEnabledTrue();

    Classroom findByClassroomNameAndDeletedFalse(String classroomName);
}
