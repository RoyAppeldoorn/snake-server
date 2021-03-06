package com.mpsnake.backend.models;

import com.mpsnake.backend.utils.LocationUtils;
import com.mpsnake.backend.utils.PlayfieldUtils;
import com.mpsnake.backend.utils.RandomColorGenerator;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.mpsnake.backend.utils.RandomDirectionEnumGenerator.randomEnum;

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
    private Boolean roomMaster;

    public Snake(String sessionId, String uuid, String nickname) {
        this.roomMaster = false;
        this.sessionId = sessionId;
        this.uuid = uuid;
        this.nickname = nickname;
        this.direction = randomEnum(Direction.class);
        this.hexColor = RandomColorGenerator.getRandomHexColor();
        this.tail = new ArrayDeque<>();
        this.points = 0;
        resetState();
    }

    public void resetState() {
        this.direction = randomEnum(Direction.class);
        this.head = LocationUtils.getRandomLocation();
        this.tail.clear();
    }

    public synchronized void updatePosition() {
        Location nextLocation = head.getAdjacentLocation(direction);
        if (nextLocation.getX() >= PlayfieldUtils.PLAYFIELD_WIDTH) {
            nextLocation.setX(0);
        }
        if (nextLocation.getY() >= PlayfieldUtils.PLAYFIELD_HEIGHT) {
            nextLocation.setY(0);
        }
        if (nextLocation.getX() < 0) {
            nextLocation.setX(PlayfieldUtils.PLAYFIELD_WIDTH);
        }
        if (nextLocation.getY() < 0) {
            nextLocation.setY(PlayfieldUtils.PLAYFIELD_HEIGHT);
        }
        tail.addFirst(head);
        head = nextLocation;
    }
}
