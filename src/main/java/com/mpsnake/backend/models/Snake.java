package com.mpsnake.backend.models;

import com.mpsnake.backend.utils.LocationUtils;
import com.mpsnake.backend.utils.PlayfieldUtils;
import com.mpsnake.backend.utils.RandomColorGenerator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

@Getter
public class Snake {

    private Location head;
    private final Deque<Location> tail;
    private final String hexColor;

    @Setter
    private final String sessionId;
    @Setter
    private final String uuid;
    @Setter
    private String username;
    @Setter
    private Direction direction;

    public Snake(String sessionId, String uuid, String username) {
        this.sessionId = sessionId;
        this.uuid = uuid;
        this.username = username;
        this.hexColor = RandomColorGenerator.getRandomHexColor();
        this.tail = new ArrayDeque<Location>();
        resetState();
    }

    public void resetState() {
        this.direction = Direction.NONE;
        this.head = LocationUtils.getRandomLocation();
        this.tail.clear();
    }

    public synchronized void updatePosition() {
        Location nextLocation = head.getAdjacentLocation(direction);
        if (nextLocation.x >= PlayfieldUtils.getPlayFieldWidth()) {
            nextLocation.x = 0;
        }
        if (nextLocation.y >= PlayfieldUtils.getPlayFieldHeight()) {
            nextLocation.y = 0;
        }
        if (nextLocation.x < 0) {
            nextLocation.x = PlayfieldUtils.getPlayFieldWidth();
        }
        if (nextLocation.y < 0) {
            nextLocation.y = PlayfieldUtils.getPlayFieldHeight();
        }
        if (direction != Direction.NONE) {
            tail.addFirst(head);
            head = nextLocation;
        }
    }

//    public String getId() {
//        return id;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getHexColor() {
//        return hexColor;
//    }
//
//    public synchronized Location getHead() {
//        return head;
//    }
//
//    public synchronized Collection<Location> getTail() {
//        return tail;
//    }
//
//    public synchronized void setDirection(Direction direction) {
//        this.direction = direction;
//    }
}
