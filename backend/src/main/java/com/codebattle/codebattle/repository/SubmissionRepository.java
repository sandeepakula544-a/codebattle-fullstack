package com.codebattle.codebattle.repository;

import com.codebattle.codebattle.entity.Question;
import com.codebattle.codebattle.entity.Room;
import com.codebattle.codebattle.entity.Submission;
import com.codebattle.codebattle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByRoomAndPlayer(Room room, User player);
    List<Submission> findByRoomAndPlayerAndQuestionAndStatus(Room room, User player, Question question, Submission.Status status);
}
