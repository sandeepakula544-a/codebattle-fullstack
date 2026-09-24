package com.codebattle.codebattle.repository;

import com.codebattle.codebattle.entity.BattleResult;
import com.codebattle.codebattle.entity.Room;
import com.codebattle.codebattle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BattleResultRepository extends JpaRepository<BattleResult, Long> {
    List<BattleResult> findByRoom(Room room);
    List<BattleResult> findByPlayer(User player);
    List<BattleResult> findByPlayerOrderByIdDesc(User player);
}
