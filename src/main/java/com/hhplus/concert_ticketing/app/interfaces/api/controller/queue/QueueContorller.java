package com.hhplus.concert_ticketing.app.interfaces.api.controller.queue;

import com.hhplus.concert_ticketing.app.application.facade.QueueFacade;
import com.hhplus.concert_ticketing.app.domain.queue.Queue;
import com.hhplus.concert_ticketing.app.interfaces.api.common.ApiResponse;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.QueueReulst;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.queue.dto.EnterQueueReq;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.queue.dto.EnterQueueRes;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.queue.dto.SelectQueueRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "대기열 큐 API", description = "콘서트 대기열에 관련된 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/queue")
public class QueueContorller {

    private final QueueFacade queueFacade;

}
