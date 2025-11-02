package de.hsrm.mi.web.projekt.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class FrontendNachrichtServiceImpl implements FrontendNachrichtService {

    private static final Logger logger = LoggerFactory.getLogger(FrontendNachrichtServiceImpl.class);
    @Autowired
    private SimpMessagingTemplate messaging;

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendEvent(FrontendNachrichtEvent ev) {
        logger.info("Sende FrontendNachrichtEvent an /topic/doener: {}", ev);
        messaging.convertAndSend("/topic/doener", ev);
    }
}