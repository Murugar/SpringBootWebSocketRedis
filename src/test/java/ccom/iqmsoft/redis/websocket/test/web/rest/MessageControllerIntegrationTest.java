package ccom.iqmsoft.redis.websocket.test.web.rest;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.iqmsoft.redis.websocket.SpringWebsocketRedisApp;
import com.iqmsoft.redis.websocket.domain.model.Message;
import com.iqmsoft.redis.websocket.domain.model.Message.MessageBuilder;
import com.iqmsoft.redis.websocket.domain.service.MessageService;
import com.iqmsoft.redis.websocket.web.events.MessageEventPublisher;
import com.iqmsoft.redis.websocket.web.rest.MessageController;

import ccom.iqmsoft.redis.websocket.test.TestUtil;
import ccom.iqmsoft.redis.websocket.test.domain.model.MessageAssertions;


@SpringBootTest(classes = SpringWebsocketRedisApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class MessageControllerIntegrationTest {

    @Mock
    private MessageService mockMessageService;

    @Mock
    private MessageEventPublisher mockMessageEventPublisher;

    private MockMvc restControllerMockMvc;

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);

        MessageController messageController = new MessageController();
        messageController.setMessageService(mockMessageService);
        messageController.setMessageEventPublisher(mockMessageEventPublisher);
        this.restControllerMockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
    }

    @Test
    public void testBroadcastPublishesOnRedis() throws Exception {

        Message testMessage = createRandomMessage();

        callBroadcast(testMessage);

        // Asserts
        ArgumentCaptor<Message> sentPayload = ArgumentCaptor.forClass(Message.class);
        verify(mockMessageEventPublisher).publishMessageReceived(sentPayload.capture());

        MessageAssertions.assertMessageEquals(testMessage, sentPayload.getValue());
    }

    @Test
    public void testGetAll() throws Exception {

        List<Message> mockResults = Arrays.asList(createRandomMessage(), createRandomMessage());
        when(mockMessageService.findAll()).thenReturn(mockResults);

        restControllerMockMvc.perform(get("/api/message"))
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(mockResults.size())))
                .andExpect(jsonPath("$[0].content").value(mockResults.get(0).getContent()))
                .andExpect(jsonPath("$[0].type").value(mockResults.get(0).getType().toString()))
                .andExpect(status().isOk());

    }

    @Test
    public void testDelete() throws Exception {

        String testId = randomAlphabetic(20);
        when(mockMessageService.findById(testId)).thenReturn(createRandomMessage());

        restControllerMockMvc.perform(delete("/api/message/{id}", testId))
                .andExpect(status().isOk());

        verify(mockMessageService).delete(testId);
    }

    private void callBroadcast(Message testMessage) throws Exception, IOException {

        when(mockMessageService.create(any(Message.class))).thenReturn(testMessage);

        restControllerMockMvc.perform(post("/api/message")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(testMessage)))
                .andExpect(status().isCreated());
    }

    private Message createRandomMessage() {
        return MessageBuilder.randomInfo().build();
    }

}
