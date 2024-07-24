
package com.ggbond.defectdetection.common;

import lombok.Data;

/**
 * @author DCTANT
 * @version 1.0
 * @date 2024/3/21 11:21:36
 * @description
 */
@Data
public class MonitorInfoModel {
    // INFO: DCTANT: 2024/3/21 JVM堆信息
    /**
     * 使用中的堆内存信息
     */
    private String usedHeapMemoryInfo;

    /**
     * 最大堆内存信息
     */
    private String maxHeapMemoryInfo;

    /**
     * 使用中的非堆内存信息
     */
    private String usedNonHeapMemoryInfo;

    /**
     * 最大非堆内存信息
     */
    private String maxNonHeapMemoryInfo;

    // INFO: DCTANT: 2024/3/21 计算机信息
    /**
     * 系统cpu使用率信息
     */
    private String cpuLoadInfo;

    /**
     * JVM进程 cpu使用率信息
     */
    private String processCpuLoadInfo;

    /**
     * 系统总内存信息
     */
    private String totalMemoryInfo;

    /**
     * 系统空闲内存信息
     */
    private String freeMemoryInfo ;

    /**
     * 使用中的内存信息
     */
    private String useMemoryInfo ;

    /**
     * 内存使用率
     */
    private String memoryUseRatioInfo;

    /**
     * 空闲交换内存信息
     */
    private String freeSwapSpaceInfo;

    /**
     * 总交换内存信息
     */
    private String totalSwapSpaceInfo;

    /**
     * 使用中交换内存信息
     */
    private String useSwapSpaceInfo;

    /**
     * 交换内存使用率信息
     */
    private String swapUseRatioInfo;

    /**
     * 系统架构
     */
    private String arch;

    /**
     * 系统名称
     */
    private String name;


}