package info.novatec.testit.livingdoc.server.domain.dao;

import info.novatec.testit.livingdoc.server.domain.SystemInfo;


public interface SystemInfoDao {
    /**
     * @return The SystemInfo
     */
    public SystemInfo getSystemInfo();

    /**
     * Stores the SystemInfo.
     * </p>
     * 
     * @param systemInfo
     */
    public void store(SystemInfo systemInfo);
}
