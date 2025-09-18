package top.clarkhg.img_viewer.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import top.clarkhg.img_viewer.util.KrpanoUtil;

@RestController
public class TestController {
    private Logger logger = LoggerFactory.getLogger(TestController.class);
    @GetMapping("/test")
    public String test(String req) {
        logger.debug("Path '/test' requested.");


        int res = KrpanoUtil.sphere2cube("D:\\Tmp\\100_0889_1.jpg", "D:\\Tmp\\");

        return Integer.toString(res);
    }
}
