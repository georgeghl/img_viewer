package top.clarkhg.img_viewer.controller.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import top.clarkhg.img_viewer.pojo.Image;
import top.clarkhg.img_viewer.pojo.User;
import top.clarkhg.img_viewer.service.crud.ImageCRUDService;
import top.clarkhg.img_viewer.service.crud.UserCRUDService;
import top.clarkhg.img_viewer.util.TokenUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;

@Controller
public class PathController {
    private Logger logger = LoggerFactory.getLogger(PathController.class);
    @Autowired
    private ImageCRUDService imageCRUDService;
    @Autowired
    private UserCRUDService userCRUDService;

    @RequestMapping("/")
    public String index(Model model, String token) {
        logger.debug("Path / requested.");
        List<Image> images = imageCRUDService.getAllPublicImages();
        // logger.debug(images.toString());
        model.addAttribute("images", images);
        model.addAttribute("token", token);
        return "index";
    }

    @RequestMapping("/map_pano")
    public String map_pano(Model model, int v, String token) {
        logger.debug("Path /map_pano requested.");
        Image image = imageCRUDService.getImageAndOwnerByImageId(v);
        // --Only use gps double
        String gpsString = image.getGps();
        Map<String, String> gpsJson = JSON.parseObject(gpsString, new TypeReference<HashMap<String, String>>() {});
        // logger.debug(gpsJson.toString());
        Map<String,Double> gpsInfoDouble = JSON.parseObject(gpsJson.get("gpsInfoDouble"), new TypeReference<HashMap<String, Double>>() {});
        // logger.debug(gpsInfoDouble.toString());
        image.setGps(JSON.toJSONString(gpsInfoDouble));
        // logger.debug(image.getGps());
        // -------------------
        // logger.debug(image.toString());
        if (image.getPublic_() == 1) {
            model.addAttribute("image", image);
            return "map_pano";
        } else if (image.getPublic_() == 0) {
            if (token == null) {
                return "error/403";
            }
            String username = TokenUtil.getUsername(token);
            User user = userCRUDService.getUserByUsername(username);
            if (user != null && user.getUsername().equals(image.getOwnerUsername())) {
                model.addAttribute("image", image);
                return "map_pano";
            } else {
                return "error/403";
            }
        } else {
            return "error/403";
        }
    }

    @RequestMapping("/image")
    public String image(Model model, int v, String token) {
        logger.debug("Path /map_pano requested.");
        Image image = imageCRUDService.getImageAndOwnerByImageId(v);
        // --Only use gps double
        String gpsString = image.getGps();
        Map<String, String> gpsJson = JSON.parseObject(gpsString, new TypeReference<HashMap<String, String>>() {});
        // logger.debug(gpsJson.toString());
        Map<String,Double> gpsInfoDouble = JSON.parseObject(gpsJson.get("gpsInfoDouble"), new TypeReference<HashMap<String, Double>>() {});
        // logger.debug(gpsInfoDouble.toString());
        image.setGps(JSON.toJSONString(gpsInfoDouble));
        // logger.debug(image.getGps());
        // -------------------
        // logger.debug(image.toString());
        if (image.getPublic_() == 1) {
            model.addAttribute("image", image);
            return "image";
        } else if (image.getPublic_() == 0) {
            if (token == null) {
                return "error/403";
            }
            String username = TokenUtil.getUsername(token);
            User user = userCRUDService.getUserByUsername(username);
            if (user != null && user.getUsername().equals(image.getOwnerUsername())) {
                model.addAttribute("image", image);
                return "image";
            } else {
                return "error/403";
            }
        } else {
            return "error/403";
        }
    }

    @RequestMapping("/map_video")
    public String map_video(Model model, int v) {
        logger.debug("Path /map_video requested.");
        return "map_video";
    }

    @RequestMapping("/login")
    public String login(Model model) {
        logger.debug("Path /login requested.");
        return "login";
    }

    @RequestMapping("/register")
    public String register(Model model) {
        logger.debug("Path /register requested.");
        return "register";
    }

    @RequestMapping("/home")
    public String home(Model model, String token) {
        logger.debug("Path /home requested.");
        if (token != null && !token.equals("null")) {
            String username = TokenUtil.getUsername(token);
            // logger.debug(username);
            List<Image> images = imageCRUDService.getImagesByUsername(username);
            // logger.debug(images.toString());
            model.addAttribute("images", images);
            model.addAttribute("token", token);
            return "home";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping("/upload")
    public String upload(Model model, String token) {
        logger.debug("Path /upload requested.");
        if (token != null) {
            String username = TokenUtil.getUsername(token);
            // logger.debug(username);
            if (userCRUDService.getUserByUsername(username) != null) {
                model.addAttribute("token", token);
                return "upload";
            } else {
                return "redirect:/login";
            }
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping("/modify")
    public String modify(Model model, String token, @RequestParam("imageId") int imageId) {
        logger.debug("Path /mkdify requested.");
        // logger.debug(token, imageId);
        if (token != null) {
            String username = TokenUtil.getUsername(token);
            // logger.debug(username);
            if (userCRUDService.getUserByUsername(username) != null) {
                if (!(imageId + " ").equals(" ")) {
                    Image image = imageCRUDService.getImageByImageId(imageId);
                    model.addAttribute("image", image);
                    return "modify";
                } else {
                    return "error/403";
                }
            } else {
                return "redirect:/login";
            }
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping("/policy")
    public String policy(Model model) {
        return "policy";
    }

    @RequestMapping("/privacypolicy")
    public String privacypolicy(Model model) {
        return "privacypolicy";
    }

    @RequestMapping("/privacy")
    public String privacy(Model model) {
        return "privacy";
    }

    @RequestMapping("/contract")
    public String contract(Model model) {
        return "contract";
    }

    @RequestMapping("/cancellaction")
    public String cancellaction(Model model) {
        return "cancellaction";
    }

}
