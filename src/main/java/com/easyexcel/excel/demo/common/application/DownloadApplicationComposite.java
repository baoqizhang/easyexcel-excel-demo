package com.sephora.nbp.common.download.application.appservice;

import static java.util.stream.Collectors.toList;

import com.sephora.nbp.engine.interfaces.api.IRequestFormService;
import com.sephora.nbp.infrastructure.enums.WorkflowClassify;
import com.sephora.nbp.infrastructure.exception.GenericBizException;
import com.sephora.nbp.infrastructure.interfaces.IUserProvider;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DownloadApplicationComposite {

    private final List<DownloadApplication> applications;
    private final IUserProvider userProvider;
    private final IRequestFormService requestFormService;

    public void downloadRequestsSkus(List<Long> requestIds, WorkflowClassify type, String materialType,
        String timezoneId, HttpServletResponse response) {
        var user = userProvider.getUser();
        var requests = requestFormService.findAllByRequestFormIds(requestIds.stream().distinct().collect(toList()));
        var downloadApplication = findAvailableDownloadApplication(type);
        downloadApplication.downloadRequestsSkus(requests, user, materialType, timezoneId, response);
    }

    private DownloadApplication findAvailableDownloadApplication(WorkflowClassify classify) {
        return applications.stream()
            .filter(it -> it.isSupport(classify))
            .findFirst()
            .orElseThrow(() -> new GenericBizException(classify + " download application not implemented."));
    }
}
