package info.novatec.testit.livingdoc.server.domain.dao;

import info.novatec.testit.livingdoc.server.domain.SystemInfo;


public interface SystemInfoDao {
    /**
     * @return The SystemInfo
     */
    SystemInfo getSystemInfo();

    /**
     * Stores the SystemInfo.
     * </p>
     * 
     * @param systemInfo
     */
    void store(SystemInfo systemInfo);
}
