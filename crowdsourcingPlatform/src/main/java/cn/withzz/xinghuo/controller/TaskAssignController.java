package cn.withzz.xinghuo.controller;

import cn.withzz.crowdsourcing.base.Distribution;
import cn.withzz.xinghuo.domain.ResponseResult;
import cn.withzz.xinghuo.domain.UserInfo;
import cn.withzz.xinghuo.schedules.TaskAssigner;
import cn.withzz.xinghuo.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * User Controller 实现 Restful HTTP 服务
 *
 * Created by svenzzhou on 08/11/2018.
 */
@RestController
public class TaskAssignController {

    @Autowired
    private TaskAssigner taskAssigner;

    @RequestMapping(value = "/api/assign", method = RequestMethod.GET)
    public List<Distribution> assign() {
        try {
            return taskAssigner.assign();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
