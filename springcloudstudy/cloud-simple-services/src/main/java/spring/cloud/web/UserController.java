package spring.cloud.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spring.cloud.conf.DataSourceProperties;
import spring.cloud.domain.UserService;
import spring.cloud.model.User;

import java.util.List;

/**
 * Created by Administrator on 2019/3/2.
 */
@RestController
public class UserController {

    Logger LOGGER = Logger.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @RequestMapping(value="/user",method=RequestMethod.GET)
    public List<User> readUserInfo(){
        List<User> ls=userService.searchAll();
        return ls;
    }

}
