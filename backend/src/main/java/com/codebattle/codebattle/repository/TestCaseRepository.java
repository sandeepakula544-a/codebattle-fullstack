package com.codebattle.codebattle.repository;

import com.codebattle.codebattle.entity.Question;
import com.codebattle.codebattle.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
    List<TestCase> findByQuestion(Question question);
    List<TestCase> findByQuestionAndHiddenFalse(Question question);
}
