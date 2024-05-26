package com.easyexcel.excel.demo.example.application;

import com.easyexcel.excel.demo.common.application.AbstractDownloadApplication;
import com.easyexcel.excel.demo.common.domain.enums.DownloadType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DemoDownloadApplication extends AbstractDownloadApplication {
    @Override
    public boolean isSupport(DownloadType downloadType) {
        return downloadType == DownloadType.DEMO_DOWNLOAD_TYPE;
    }
}
