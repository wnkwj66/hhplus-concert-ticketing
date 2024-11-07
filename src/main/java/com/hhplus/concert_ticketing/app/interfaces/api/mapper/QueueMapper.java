package com.hhplus.concert_ticketing.app.interfaces.api.mapper;

import com.hhplus.concert_ticketing.app.domain.queue.Queue;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.QueueReulst;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.dto.queue.CreateQueueRes;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.dto.queue.SelectQueueRes;
import org.springframework.stereotype.Component;

@Component
public class QueueMapper {

    public CreateQueueRes toCreateQueueRes(Queue queue) {
        return new CreateQueueRes(
                queue.getTokenId()
        );

    }

    public SelectQueueRes toSelectQueueRes(QueueReulst queueReulst) {
        return new SelectQueueRes(
                queueReulst.position(),
                queueReulst.queue().getStatus()
        );
    }
}
