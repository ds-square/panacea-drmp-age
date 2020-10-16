package org.panacea.drmp.age.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.panacea.drmp.age.domain.notification.DataNotification;
import org.panacea.drmp.age.domain.notification.DataNotificationResponse;
import org.panacea.drmp.age.service.OrchestratorNotificationHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(path = "/age", produces = "application/json")
public class APIPostNotifyData {

    @Autowired
    OrchestratorNotificationHandlerService orchestratorNotificationHandler;

    @Operation(description = "Post a Data Notification")
    @PostMapping(value = "/notify/data")
    public DataNotificationResponse postNotifyData(@RequestBody DataNotification request) {
        return orchestratorNotificationHandler.perform(request);
    }

}