package com.codebattle.codebattle.repository;

import com.codebattle.codebattle.entity.Question;
import com.codebattle.codebattle.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByTopic(Room.Topic topic);
}
