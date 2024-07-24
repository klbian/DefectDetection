package com.ggbond.defectdetection.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 系统状态信息获取类
 * <p>
 * Author: 19461
 * Date: 2024/2/7
 */
@Slf4j
public class SystemStatusUtil {

    //cpu
    private static String CPUUsage;

    //gpu
    private static String GPUUsage;

    //内存
    private static String memoryUsage;


    //启动时间
    private static Long startTime=0L;

    //持续工作时长
    private static int continuousWorkingSeconds;


    public static void init(){
        startTime=System.currentTimeMillis();
    }

    public static int getContinuousWorkingSeconds(){
        continuousWorkingSeconds=(int)(System.currentTimeMillis()-startTime)/1000;
        return continuousWorkingSeconds;
    };


    //获取系统的状态CPU,GPU,内存
    public static String[] getStatus(){
        SystemInfo systemInfo = new SystemInfo();
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        CPUUsage=new DecimalFormat("#.##%").format(1.0-(idle * 1.0 / totalCpu));
//        log.info("cpu利用率{}",totalCpu);

        //获取内存
        GlobalMemory memory=systemInfo.getHardware().getMemory();
        memoryUsage=formatByte((memory.getTotal() - memory.getAvailable())) +"/"+ formatByte(memory.getTotal());

        //获取gpu使用率
        try {
            List<GPU> gpus = NvidiaSmiParser.parseGpu();

            if(gpus==null){
                throw new RuntimeException("未检测到gpu");
            }

            int tolal=0;
            int used=0;
            for(GPU gpu:gpus){
                tolal+=gpu.getFbMemoryUsage().getTotal();
                used+=gpu.getFbMemoryUsage().getUsed();
            }
            GPUUsage=new DecimalFormat("#.##%").format(used * 1.0 / tolal);

        }catch (IOException e){
            GPUUsage="";
        }catch (RuntimeException e){
            GPUUsage=e.getMessage();
        }

//        log.info("cpu使用率={},gpu使用率={},内存使用率={}",CPUUsage,GPUUsage,memoryUsage);
        return new String[]{CPUUsage,memoryUsage,GPUUsage};
    }
    /**
    * @Description   格式转换 byte->kb,mb,gb
    * @param
    * @return java.lang.String
    * @Author 19461
    * @Date 2024/2/8
    * @TEST
    */
    public static String formatByte(long byteNumber){
        double FORMAT = 1024.0;
        double kbNumber = byteNumber/FORMAT;
        if(kbNumber<FORMAT){
            return new DecimalFormat("#.##KB").format(kbNumber);
        }
        double mbNumber = kbNumber/FORMAT;
        if(mbNumber<FORMAT){
            return new DecimalFormat("#.##MB").format(mbNumber);
        }
        double gbNumber = mbNumber/FORMAT;
        if(gbNumber<FORMAT){
            return new DecimalFormat("#.##GB").format(gbNumber);
        }
        double tbNumber = gbNumber/FORMAT;
        return new DecimalFormat("#.##TB").format(tbNumber);
    }
    /**
     * nvidia-smi 内存使用实体
     *
     * @author angelhand
     * @date 2023/11/1
     */
    @JacksonXmlRootElement(localName = "fb_memory_usage")
    public static class FbMemoryUsage {
        @JacksonXmlProperty(localName = "total")
        private int total;

        @JacksonXmlProperty(localName = "used")
        private int used;

        @JacksonXmlProperty(localName = "free")
        private int free;

        public int getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = Integer.parseInt(total.substring(0, total.indexOf(" ")));
        }

        public int getUsed() {
            return used;
        }

        public void setUsed(String used) {
            this.used = Integer.parseInt(used.substring(0, used.indexOf(" ")));
        }

        public int getFree() {
            return free;
        }

        public void setFree(String free) {
            this.free = Integer.parseInt(free.substring(0, free.indexOf(" ")));
        }
    }

    /**
     * nvidia-smi gpu实体
     *
     * @author angelhand
     * @date 2023/11/1
     */
    @JacksonXmlRootElement(localName = "gpu")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GPU {
        @JacksonXmlProperty(isAttribute = true, localName = "id")
        private String id;

        @JacksonXmlProperty(localName = "fb_memory_usage")
        private FbMemoryUsage fbMemoryUsage;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public FbMemoryUsage getFbMemoryUsage() {
            return fbMemoryUsage;
        }

        public void setFbMemoryUsage(FbMemoryUsage fbMemoryUsage) {
            this.fbMemoryUsage = fbMemoryUsage;
        }
    }

    /**
     * nvidia-smi实体
     *
     * @author angelhand
     * @date 2023/11/1
     */
    @JacksonXmlRootElement(localName = "nvidia_smi_log")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NvidiaSmiLog {
        @JacksonXmlProperty(localName = "driver_version")
        private String driverVersion;

        @JacksonXmlProperty(localName = "cuda_version")
        private String cudaVersion;

        @JacksonXmlProperty(localName = "gpu")
        private List<GPU> gpus;

        public String getDriverVersion() {
            return driverVersion;
        }

        public void setDriverVersion(String driverVersion) {
            this.driverVersion = driverVersion;
        }

        public String getCudaVersion() {
            return cudaVersion;
        }

        public void setCudaVersion(String cudaVersion) {
            this.cudaVersion = cudaVersion;
        }

        public List<GPU> getGpus() {
            return gpus;
        }

        public void setGpus(List<GPU> gpus) {
            this.gpus = gpus;
        }
    }

    /**
     * nvidia-smi命令解析为java对象
     * 需要安装好相应的环境
     *
     * @author angelhand
     * @date 2023/11/1
     */
    public static class NvidiaSmiParser {
        private static XmlMapper xmlMapper = XmlMapper.builder()
                .defaultUseWrapper(false)
                .build();

        /**
         * 检查是否有英伟达驱动
         *
         * @return true/false
         */
        public static boolean haveNvidia() throws IOException {
            try (ByteArrayOutputStream std = new ByteArrayOutputStream();
                 ByteArrayOutputStream err = new ByteArrayOutputStream()) {

                CommandLine commandLine = new CommandLine("nvidia-smi");
                DefaultExecutor exec = new DefaultExecutor();
                PumpStreamHandler streamHandler = new PumpStreamHandler(std, err);
                exec.setStreamHandler(streamHandler);

                int exitCode = exec.execute(commandLine);
                if (exitCode != 0) {
                    return false;
                } else {
                    return true;
                }
            }
        }

        /**
         * 获取英伟达显卡信息
         *
         * @return 显卡信息
         */
        public static NvidiaSmiLog parse() throws IOException {
            NvidiaSmiLog result = null;

            try (ByteArrayOutputStream std = new ByteArrayOutputStream();
                 ByteArrayOutputStream err = new ByteArrayOutputStream()) {

                CommandLine commandLine = new CommandLine("nvidia-smi -q -x");
                DefaultExecutor exec = new DefaultExecutor();
                PumpStreamHandler streamHandler = new PumpStreamHandler(std, err);
                exec.setStreamHandler(streamHandler);

                int exitCode = exec.execute(commandLine);
                if (exitCode != 0) {
                    return result;
                }

                result = xmlMapper.readValue(std.toString(), NvidiaSmiLog.class);
            }
            return result;
        }

        /**
         * 只获取gpu信息
         *
         * @return gpu信息
         */
        public static List<GPU> parseGpu() throws IOException {
            List<GPU> result = null;
            NvidiaSmiLog nvidiaSmiLog = parse();
            if (nvidiaSmiLog == null) {
                return result;
            }

            result = nvidiaSmiLog.getGpus();
            return result;
        }
    }


}