package com.mpsnake.backend.logic;

import com.mpsnake.backend.models.Direction;
import com.mpsnake.backend.models.Snake;

public interface IGameLogic {
    void addPlayerToGame(Snake snake);
    void removePlayerFromGame(String id);
    void setDirection(String id, Direction direction);
}
