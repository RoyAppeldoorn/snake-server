package com.mpsnake.backend.logic;

import com.mpsnake.backend.model.Snake;

public interface IGameLogic {

    void addSnake(Snake snake);

    void removeSnake(String id);
}
