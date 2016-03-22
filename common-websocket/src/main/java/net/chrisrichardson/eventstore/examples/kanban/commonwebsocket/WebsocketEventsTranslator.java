package net.chrisrichardson.eventstore.examples.kanban.commonwebsocket;

import com.example.myeventsourcing.common.event.customer.CargoCustomerDebitFailedDueToInsufficientFoundsEvent;
import com.example.myeventsourcing.common.event.financial.CargoInvoiceGeneratedEvent;
import com.example.myeventsourcing.common.event.cargo.CargoCreatedEvent;
import com.example.myeventsourcing.event.BaseEvent;
import com.example.myeventsourcing.event.Listener;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chrisrichardson.eventstore.examples.kanban.commonwebsocket.model.KanbanWebSocketEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by popikyardo on 15.10.15.
 */

@Component
public class WebsocketEventsTranslator {

    protected SimpMessagingTemplate template;

    private static String DESTINATION_DEFAULT_URL = "/events";

    private static ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private static Logger log = LoggerFactory.getLogger(WebsocketEventsTranslator.class);

    public WebsocketEventsTranslator(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Listener
    public void sendCargoInvoiceGenerated(CargoInvoiceGeneratedEvent event) throws JsonProcessingException {
        Map<String, Object> invoice = new HashMap<>();
        invoice.put("customerId", event.getCustomerId());
        invoice.put("date", event.getDate());
        invoice.put("amount", event.getAmount());

        this.sendEvent(event, DESTINATION_DEFAULT_URL, objectMapper.writeValueAsString(invoice));
    }

    @Listener
    public void sendCargoCustomerDebitFailed(CargoCustomerDebitFailedDueToInsufficientFoundsEvent event) throws JsonProcessingException {
        Map<String, Object> debitFailed = new HashMap<>();

        this.sendEvent(event, DESTINATION_DEFAULT_URL, objectMapper.writeValueAsString(debitFailed));
    }

    private void sendEvent(BaseEvent de, String destination, String eventData) throws JsonProcessingException {
        log.info("Sending board event to websocket : {}", de);
        KanbanWebSocketEvent event = new KanbanWebSocketEvent();
        event.setEntityId(de.getEntityId());
        event.setEventData(eventData);
        event.setEventId(de.getId());
        event.setEventType(de.getType());
        template.convertAndSend(destination, objectMapper.writeValueAsString(event));
    }
}