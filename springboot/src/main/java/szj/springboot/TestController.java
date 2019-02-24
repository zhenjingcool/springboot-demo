package szj.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2019/2/13.
 */
@CrossOrigin
@Controller
public class TestController {

    @ResponseBody
    @RequestMapping("/hello")
    public String hello(HttpServletRequest request)
    {
        //跨域调用时会有问题，解决跨域问题
        String callback = request.getParameter("callback");
        String json = "{ \"firstName\":\"John\" , \"lastName\":\"Doe\" }";
        return callback + "(" + json + ")";
    }
}