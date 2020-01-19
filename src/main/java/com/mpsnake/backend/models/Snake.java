package com.mpsnake.backend.models;

import com.mpsnake.backend.utils.LocationUtils;
import com.mpsnake.backend.utils.PlayfieldUtils;
import com.mpsnake.backend.utils.RandomColorGenerator;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.Deque;

@Getter
@Setter
public class Snake {

    private Location head;
    private final Deque<Location> tail;
    private final String hexColor;

    private int points;
    private final String sessionId;
    private final String uuid;
    private String nickname;
    private Direction direction;

    public Snake(String sessionId, String uuid, String nickname) {
        this.sessionId = sessionId;
        this.uuid = uuid;
        this.nickname = nickname;
        this.hexColor = RandomColorGenerator.getRandomHexColor();
        this.tail = new ArrayDeque<Location>();
        this.points = 0;
        resetState();
    }

    public void resetState() {
        this.direction = Direction.NONE;
        this.head = LocationUtils.getRandomLocation();
        this.points = 0;
        this.tail.clear();
    }

    public synchronized void updatePosition() {
        Location nextLocation = head.getAdjacentLocation(direction);
        if (nextLocation.x >= PlayfieldUtils.PLAYFIELD_WIDTH) {
            nextLocation.x = 0;
        }
        if (nextLocation.y >= PlayfieldUtils.PLAYFIELD_HEIGHT) {
            nextLocation.y = 0;
        }
        if (nextLocation.x < 0) {
            nextLocation.x = PlayfieldUtils.PLAYFIELD_WIDTH;
        }
        if (nextLocation.y < 0) {
            nextLocation.y = PlayfieldUtils.PLAYFIELD_HEIGHT;
        }
        if (direction != Direction.NONE) {
            tail.addFirst(head);
            head = nextLocation;
        }
    }
}
