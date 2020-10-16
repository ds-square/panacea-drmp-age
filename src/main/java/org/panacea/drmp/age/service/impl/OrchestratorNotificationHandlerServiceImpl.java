package org.panacea.drmp.age.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.panacea.drmp.age.DBImportService;
import org.panacea.drmp.age.domain.exception.AGEException;
import org.panacea.drmp.age.domain.notification.DataNotification;
import org.panacea.drmp.age.domain.notification.DataNotificationResponse;
import org.panacea.drmp.age.service.OrchestratorNotificationHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Service
public class OrchestratorNotificationHandlerServiceImpl implements OrchestratorNotificationHandlerService {
    public static final String INVALID_NOTIFICATION_ERR_MSG = "Invalid Data Notification Body.";

    @Autowired
    DBImportService dbImportService;

    @Override
    @ResponseBody
    public DataNotificationResponse perform(DataNotification notification) throws AGEException {
        log.info("Received notification from Orchestrator");
        try {
            if (notification.getEnvironment() == null) {
                throw new AGEException("No environment defined for notification.");
            }
            dbImportService.importAttackGraph(notification.getSnapshotId());

            return new DataNotificationResponse(notification.getEnvironment(), notification.getSnapshotId(), notification.getSnapshotTime());
        } catch (AGEException e) {
            log.info("AGEException occurred: ", e);
            throw new AGEException(INVALID_NOTIFICATION_ERR_MSG, e);
        }
    }
}

