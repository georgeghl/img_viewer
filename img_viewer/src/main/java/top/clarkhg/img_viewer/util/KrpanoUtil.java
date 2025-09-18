package top.clarkhg.img_viewer.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import top.clarkhg.img_viewer.mapper.MyConfigItemMapper;

@Component
public class KrpanoUtil {
    private static Logger logger = LoggerFactory.getLogger(KrpanoUtil.class);
    @Autowired
    private MyConfigItemMapper myConfigItemMapper;
    private static KrpanoUtil krpanoUtil;

    @PostConstruct
    public void init() {
        krpanoUtil = this;
        krpanoUtil.myConfigItemMapper = this.myConfigItemMapper;
    }



    public static int sphere2cube(String imgPath, String outputPath) {
        try {
            logger.debug("==================================================================================");
            logger.debug("sphere2cube is called. Image file path: " + imgPath + "  outputPath: " + outputPath);
    
            String KRPANO_HOME = krpanoUtil.myConfigItemMapper.selectConfigValueByName("krpano_home");
            logger.debug(KRPANO_HOME);
            String PLATFORM_NAME = krpanoUtil.myConfigItemMapper.selectConfigValueByName("platform_name");
            logger.debug(PLATFORM_NAME);
            String JPEG_QUALITY=krpanoUtil.myConfigItemMapper.selectConfigValueByName("jpeg_quality");
            logger.debug(JPEG_QUALITY);
            String JPEG_SUBSAMP=krpanoUtil.myConfigItemMapper.selectConfigValueByName("jpeg_subsamp");
            logger.debug(JPEG_SUBSAMP);

    
            String command = null;
            if (PLATFORM_NAME.equals("WIN64")) {
                command = KRPANO_HOME + "krpanotools64.exe sphere2cube CUBE " + imgPath +" -jpegquality="+JPEG_QUALITY+" -jpegsubsamp="+JPEG_SUBSAMP;
            } else if (PLATFORM_NAME.equals("WIN32")) {
                command = KRPANO_HOME + "krpanotools32.exe sphere2cube CUBE " + imgPath +" -jpegquality="+JPEG_QUALITY+" -jpegsubsamp="+JPEG_SUBSAMP;
            } else if (PLATFORM_NAME.equals("LINUX")) {
                command = KRPANO_HOME + "krpanotools sphere2cube CUBE " + imgPath + " "+ imgPath +" -jpegquality="+JPEG_QUALITY+" -jpegsubsamp="+JPEG_SUBSAMP;
            } else {
                logger.error("Platform illegal!");
            }
    
            ProcessBuilder processBuilder = new ProcessBuilder(command.split("\\s+"));
            Process process = processBuilder.start();
            logger.debug(command);
            // 关闭输出流，防止产生阻塞
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.debug("krpanoUtil@ " + line);
                }
            }
    
            int exitValue = process.waitFor();
    
            logger.debug("==================================================================================");
    
            return exitValue == 0 ? 1 : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            logger.debug("finally");
        }
    }
    


    public static int makePreview(String imgPath, String outputPath) {
        try {
            logger.debug("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            logger.debug("makePreview is called. Image file path: " + imgPath + "  outputPath: " + outputPath);
    
            String KRPANO_HOME = krpanoUtil.myConfigItemMapper.selectConfigValueByName("krpano_home");
            logger.debug(KRPANO_HOME);
            String PLATFORM_NAME = krpanoUtil.myConfigItemMapper.selectConfigValueByName("platform_name");
            logger.debug(PLATFORM_NAME);
    
            String command = null;
            if (PLATFORM_NAME.equals("WIN64")) {
                command = KRPANO_HOME + "krpanotools64.exe makepreview " + imgPath + " -o=" + outputPath;
            } else if (PLATFORM_NAME.equals("WIN32")) {
                command = KRPANO_HOME + "krpanotools32.exe makepreview " + imgPath + " -o=" + outputPath;
            }else if (PLATFORM_NAME.equals("LINUX")) {
                command = KRPANO_HOME + "krpanotools makepreview " + imgPath + " -o=" + outputPath;
            }
            else {
                logger.error("Platform illegal!");
            }
            logger.debug(command);
            ProcessBuilder processBuilder = new ProcessBuilder(command.split("\\s+"));
            Process process = processBuilder.start();
    
            // 关闭输出流，防止产生阻塞
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.debug("krpanoUtil@ " + line);
                }
            }
    
            int exitValue = process.waitFor();
    
            logger.debug("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    
            return exitValue == 0 ? 1 : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            logger.debug("Finally");
        }
    }


}
