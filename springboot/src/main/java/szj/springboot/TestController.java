package szj.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2019/2/13.
 */
@Controller
public class TestController {

    @ResponseBody
    @RequestMapping("/hello")
    public String hello()
    {
        return "hello woild";
    }
}