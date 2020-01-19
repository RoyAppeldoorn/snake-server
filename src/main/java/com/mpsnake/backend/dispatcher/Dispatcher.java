package com.mpsnake.backend.dispatcher;

import com.mpsnake.backend.models.Message;

public interface Dispatcher {
    void dispatch(Message message);
}
