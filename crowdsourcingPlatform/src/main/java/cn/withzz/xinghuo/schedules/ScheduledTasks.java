package cn.withzz.xinghuo.schedules;

import cn.withzz.crowdsourcing.experiment.DemoTest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledTasks {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Scheduled(fixedRate = 60000)
    public void reportCurrentTime() {
        try {
            String time = dateFormat.format(new Date());
            System.out.println("现在时间：" + time);
            DemoTest.main(null);
            System.out.println("完成" + time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
