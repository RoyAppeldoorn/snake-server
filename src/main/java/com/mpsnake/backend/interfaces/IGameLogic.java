package com.mpsnake.backend.interfaces;

import com.mpsnake.backend.models.Direction;
import com.mpsnake.backend.models.Snake;

public interface IGameLogic {

    void addSnake(Snake snake);

    void removeSnake(String id);

    void setDirection(String id, Direction direction);
}
