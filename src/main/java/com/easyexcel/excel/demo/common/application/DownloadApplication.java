package com.sephora.nbp.common.download.application.appservice;

import com.sephora.nbp.engine.domain.model.RequestFormModel;
import com.sephora.nbp.infrastructure.enums.WorkflowClassify;
import com.sephora.nbp.infrastructure.model.User;
import org.springframework.lang.NonNull;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DownloadApplication {

    boolean isSupport(@NonNull WorkflowClassify classify);

    void downloadRequestsSkus(@NonNull List<RequestFormModel> requestIds, @NonNull User user, @NonNull String materialType,
        @NonNull String timezoneId, @NonNull HttpServletResponse response);
}
