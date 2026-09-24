package com.codebattle.codebattle.repository;

import com.codebattle.codebattle.entity.Room;
import com.codebattle.codebattle.entity.RoomPlayer;
import com.codebattle.codebattle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomPlayerRepository extends JpaRepository<RoomPlayer, Long> {
    List<RoomPlayer> findByRoom(Room room);
    Optional<RoomPlayer> findByRoomAndUser(Room room, User user);
    int countByRoom(Room room);
}
