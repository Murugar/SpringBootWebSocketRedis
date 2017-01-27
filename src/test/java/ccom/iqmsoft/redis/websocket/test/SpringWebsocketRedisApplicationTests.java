package ccom.iqmsoft.redis.websocket.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.iqmsoft.redis.websocket.SpringWebsocketRedisApp;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringWebsocketRedisApp.class)
@ActiveProfiles("embedded")
public class SpringWebsocketRedisApplicationTests {

    @Test
    public void contextLoads() {
    }

}
