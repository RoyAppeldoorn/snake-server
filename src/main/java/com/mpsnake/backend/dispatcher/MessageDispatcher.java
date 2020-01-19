package com.mpsnake.backend.dispatcher;

import com.mpsnake.backend.models.Message;
import lombok.NoArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

@NoArgsConstructor
public class MessageDispatcher implements Dispatcher {

    private final Log log = LogFactory.getLog(MessageDispatcher.class);

    @Autowired
    private SimpMessageSendingOperations messageTemplate;

    @Override
    public void dispatch(Message message) {
        log.info(message);
        messageTemplate.convertAndSend("/topic/public", message);
    }
}
