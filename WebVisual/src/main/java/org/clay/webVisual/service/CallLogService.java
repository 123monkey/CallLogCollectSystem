package org.clay.webVisual.service;

import org.clay.webVisual.domain.CallLog;
import org.clay.webVisual.domain.CallLogRange;

import java.util.List;

/**
 *
 */
public interface CallLogService {
    public List<CallLog> findAll();

    /**
     * 按照范围查询通话记录
     */
    public List<CallLog> findCallogs(String call, List<CallLogRange> list);
}
