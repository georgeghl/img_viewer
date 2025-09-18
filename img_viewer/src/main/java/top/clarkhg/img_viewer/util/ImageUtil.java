package top.clarkhg.img_viewer.util;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.Rational;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.GpsDirectory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageUtil {

    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    public static boolean genThumbnail(String oldPath, String newPath, double smallSize) {
        try {
            File bigFile = new File(oldPath);
            Image image = ImageIO.read(bigFile);
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            int widthSmall = (int) (width / smallSize);
            int heightSmall = (int) (height / smallSize);
            BufferedImage buffi = new BufferedImage(widthSmall, heightSmall, BufferedImage.TYPE_INT_RGB);
            Graphics g = buffi.getGraphics();
            g.drawImage(image, 0, 0, widthSmall, heightSmall, null);
            g.dispose();
            ImageIO.write(buffi, "jpg", new File(newPath));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean imageCompress(String oldPath, String newPath) throws Exception {
        boolean rs = true;
        File file = new File(oldPath);
        float fileSize = file.length();
        int kb = 100 * 1024;
        if (fileSize > 100 * 1024) {
            int smallSize = (int) (fileSize % kb == 0 ? fileSize / kb : fileSize / kb + 1);
            double size = Math.ceil(Math.sqrt(smallSize));
            rs = ImageUtil.genThumbnail(oldPath, newPath, size);
        }
        return rs;
    }

    public static Map<String, String> getExifInfo(String filepath) throws ImageProcessingException {
        Map<String, String> exifInfo = new HashMap<>();
        try {
            File jpegFile = new File(filepath);
            Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);
            Iterable<Directory> directories = metadata.getDirectories();
            for (Directory directory : directories) {
                for (Tag tag : directory.getTags()) {
                    exifInfo.put(tag.getTagName(), tag.getDescription());
                }
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            // Handle exceptions appropriately based on your application's requirements
        }
        return exifInfo;
    }

    public static Map<String, Double> getGpsInfoDouble(String filepath) throws Exception {
        Map<String, Double> gpsInfo = new HashMap<>();

        try {
            File jpegFile = new File(filepath);
            Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);

            for (Directory directory : metadata.getDirectories()) {
                if (directory instanceof GpsDirectory) {
                    GpsDirectory gpsDirectory = (GpsDirectory) directory;
                    double latitude=0.0;
                    latitude = getDegreesFromRationals(gpsDirectory.getRationalArray(GpsDirectory.TAG_LATITUDE));
                    double longitude=0.0;
                    longitude = getDegreesFromRationals(
                            gpsDirectory.getRationalArray(GpsDirectory.TAG_LONGITUDE));

                    gpsInfo.put("latitude", latitude);
                    gpsInfo.put("longitude", longitude);

                    if (gpsDirectory.containsTag(GpsDirectory.TAG_ALTITUDE)) {
                        double altitude = gpsDirectory.getDouble(GpsDirectory.TAG_ALTITUDE);
                        gpsInfo.put("altitude", altitude);
                    }
                    else{
                        gpsInfo.put("altitude", null);
                    }
                }
            }
        } catch (IOException | ImageProcessingException e) {
            e.printStackTrace();
            // Handle exceptions appropriately based on your application's requirements
        }

        return gpsInfo.isEmpty() ? new HashMap<>() : gpsInfo;
    }

    public static Map<String, String[]> getGpsInfoDms(String filepath) throws Exception {
        Map<String, String[]> gpsInfo = new HashMap<>();

        try {
            File jpegFile = new File(filepath);
            Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);

            for (Directory directory : metadata.getDirectories()) {
                if (directory instanceof GpsDirectory) {
                    GpsDirectory gpsDirectory = (GpsDirectory) directory;

                    String[] latitudeDms = getDmsFromRationals(
                            gpsDirectory.getRationalArray(GpsDirectory.TAG_LATITUDE));
                    if (latitudeDms != null) {
                        gpsInfo.put("latitude", latitudeDms);
                    } else {
                        gpsInfo.put("latitude", null);
                    }

                    String[] longitudeDms = getDmsFromRationals(
                            gpsDirectory.getRationalArray(GpsDirectory.TAG_LONGITUDE));
                    if (longitudeDms != null) {
                        gpsInfo.put("longitude", longitudeDms);
                    } else {
                        gpsInfo.put("longitude", null);
                    }

                    if (gpsDirectory.containsTag(GpsDirectory.TAG_ALTITUDE)) {
                        double altitude = gpsDirectory.getDouble(GpsDirectory.TAG_ALTITUDE);
                        gpsInfo.put("altitude", new String[] { String.valueOf(altitude) });
                    }
                    else{
                        gpsInfo.put("altitude", null);
                    }
                }
            }
        } catch (IOException | ImageProcessingException e) {
            e.printStackTrace();
            // Handle exceptions appropriately based on your application's requirements
        }

        return gpsInfo.isEmpty() ? new HashMap<>() : gpsInfo;
    }

    public static Map<String, String[]> getGpsInfoRaw(String filepath) throws Exception {
        Map<String, String[]> gpsInfo = new HashMap<>();

        try {
            File jpegFile = new File(filepath);
            Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);

            for (Directory directory : metadata.getDirectories()) {
                if (directory instanceof GpsDirectory) {
                    GpsDirectory gpsDirectory = (GpsDirectory) directory;

                    Rational[] latitudeRat = gpsDirectory.getRationalArray(GpsDirectory.TAG_LATITUDE);
                    if (latitudeRat != null) {
                        String[] latitude = new String[] { latitudeRat[0].toString(), latitudeRat[1].toString(),
                                latitudeRat[2].toString() };
                        gpsInfo.put("latitude", latitude);
                    } else {
                        gpsInfo.put("latitude", null);
                    }

                    Rational[] longitudeRat = gpsDirectory.getRationalArray(GpsDirectory.TAG_LONGITUDE);
                    if (longitudeRat != null) {
                        String[] longitude = new String[] { longitudeRat[0].toString(), longitudeRat[1].toString(),
                                longitudeRat[2].toString() };
                        gpsInfo.put("longitude", longitude);
                    } else {
                        gpsInfo.put("longitude", null);
                    }
                    if (gpsDirectory.containsTag(GpsDirectory.TAG_ALTITUDE)) {
                        double altitude = gpsDirectory.getDouble(GpsDirectory.TAG_ALTITUDE);
                        gpsInfo.put("altitude", new String[] { String.valueOf(altitude) });
                    } else {
                        gpsInfo.put("altitude", null);

                    }
                }
            }
        } catch (IOException | ImageProcessingException e) {
            e.printStackTrace();
            // Handle exceptions appropriately based on your application's requirements
        }

        return gpsInfo.isEmpty() ? new HashMap<>() : gpsInfo;
    }

    // 辅助方法，用于将Rational数组转换为十进制度数
    private static double getDegreesFromRationals(Rational[] rationals) {
        if (rationals == null || rationals.length < 3) {
            return 0.0;
        }
        return rationals[0].doubleValue() + (rationals[1].doubleValue() / 60) + (rationals[2].doubleValue() / 3600);
    }

    // 辅助方法，用于将Rational数组转换为度分秒格式的字符串数组
    private static String[] getDmsFromRationals(Rational[] rationals) {
        if (rationals == null || rationals.length < 3) {
            return new String[] { "0", "0", "0" };
        }
        int degrees = rationals[0].intValue();
        int minutes = (int) Math.round((rationals[1].floatValue() / rationals[0].floatValue()) * 60);
        int seconds = (int) Math.round((rationals[2].floatValue() / rationals[0].floatValue()) * 3600);

        return new String[] { String.valueOf(degrees), String.valueOf(minutes), String.valueOf(seconds) };
    }
}
