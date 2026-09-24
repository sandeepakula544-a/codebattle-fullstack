package com.codebattle.codebattle.repository;

import com.codebattle.codebattle.entity.Room;
import com.codebattle.codebattle.entity.RoomQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomQuestionRepository extends JpaRepository<RoomQuestion, Long> {
    List<RoomQuestion> findByRoomOrderByOrderIndexAsc(Room room);
}
