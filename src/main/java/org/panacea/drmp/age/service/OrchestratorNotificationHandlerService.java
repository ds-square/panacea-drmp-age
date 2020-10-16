package org.panacea.drmp.age.service;

import org.panacea.drmp.age.domain.notification.DataNotification;
import org.panacea.drmp.age.domain.notification.DataNotificationResponse;

public interface OrchestratorNotificationHandlerService {

    DataNotificationResponse perform(DataNotification notification);

}
