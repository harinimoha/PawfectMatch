package com.pawfectmatch.backend;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;
import com.pawfectmatch.backend.messages.Message;
import com.pawfectmatch.backend.messages.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageRepository messageRepository;

    // GET /messages
    @Test
    void getAllMessages() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/messages"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        List<Message> messages = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Message>>() {});
        assertTrue(messages.size() > 0);
    }

    // GET /messages/inbox/{userId}
    @Test
    void getInboxByUserId_provider1() throws Exception {
        // User 1 (Happy Paws) received messages from Alice (id 5) and Carol (id 7)
        MockHttpServletResponse response = mockMvc.perform(get("/messages/inbox/1"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        List<Message> inbox = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Message>>() {});
        assertTrue(inbox.size() > 0);

        // Every message in the inbox must have receiverId = 1
        for (Message message : inbox) {
            assertEquals(1, message.getReceiverId());
        }
    }

    @Test
    void getInbox_userWithNoMessages() throws Exception {
        // User 16 (Liam) never received any messages in seed data
        MockHttpServletResponse response = mockMvc.perform(get("/messages/inbox/16"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        List<Message> inbox = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Message>>() {});
        assertEquals(0, inbox.size());
    }

    // GET /messages/sent/{userId}
    @Test
    void getSent_Alice() throws Exception {
        // User 5 (Alice) sent messages to provider 1 about pet 1
        MockHttpServletResponse response = mockMvc.perform(get("/messages/sent/5"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        List<Message> sent = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Message>>() {});
        assertTrue(sent.size() > 0);

        // Every message must have senderId = 5
        for (Message message : sent) {
            assertEquals(5, message.getSenderId());
        }
    }

    @Test
    void getSent_userWithNoSentMessages() throws Exception {
        // User 16 (Liam) never sent any messages in seed data
        MockHttpServletResponse response = mockMvc.perform(get("/messages/sent/16"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        List<Message> sent = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Message>>() {});
        assertEquals(0, sent.size());
    }

    // GET /messages/conversation
    @Test
    void getConversation_aliceAndProvider1AboutMilo() throws Exception {
        // Alice (5) and Happy Paws (1) about Milo (pet 1): 3 messages in seed data
        MockHttpServletResponse response = mockMvc.perform(
                        get("/messages/conversation")
                                .param("user1", "5")
                                .param("user2", "1")
                                .param("petId", "1"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        List<Message> thread = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Message>>() {});
        assertTrue(thread.size() > 0);

        // Every message in the thread must be about pet 1
        for (Message message : thread) {
            assertEquals(1, message.getPetId());
        }
    }



    // POST /messages
    @Test
    void sendMessage() throws Exception {
        ObjectNode messageJSON = objectMapper.createObjectNode();
        messageJSON.put("senderId", 7);
        messageJSON.put("receiverId", 2);
        messageJSON.put("petId", 5);
        messageJSON.put("content", "Is Charlie good with cats?");

        MockHttpServletResponse response = mockMvc.perform(
                        post("/messages")
                                .contentType("application/json")
                                .content(messageJSON.toString()))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        // get the auto-generated ID
        Message returnedMessage = objectMapper.readValue(response.getContentAsString(), Message.class);
        Integer newId = returnedMessage.getId();

        // verify the message exists in DB with correct details
        assertTrue(messageRepository.findById(newId).isPresent());
        Message savedMessage = messageRepository.findById(newId).get();

        assertEquals(7, savedMessage.getSenderId());
        assertEquals(2, savedMessage.getReceiverId());
        assertEquals(5, savedMessage.getPetId());
        assertEquals("Is Charlie good with cats?", savedMessage.getContent());
        assertEquals(false, savedMessage.getIsRead());   // auto-set to false by controller
        assertNotNull(savedMessage.getSentAt());          // auto-set timestamp by controller?
    }

    // PATCH /messages/{id}/read
    @Test
    void setsIsReadToTrue() throws Exception {
        // Message 3 has is_read = FALSE in seed data (confirm this)
        assertTrue(messageRepository.findById(3).isPresent());
        assertEquals(false, messageRepository.findById(3).get().getIsRead());

        MockHttpServletResponse response = mockMvc.perform(patch("/messages/3/read"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        // Verify -> now marked as read
        assertTrue(messageRepository.findById(3).isPresent());
        assertEquals(true, messageRepository.findById(3).get().getIsRead());
    }

    @Test
    void markAsRead_nonExistent() throws Exception {
        // Message 9999 does not exist 
        assertThrows(Exception.class, () ->
                mockMvc.perform(patch("/messages/9999/read")).andReturn()
        );
        assertFalse(messageRepository.findById(9999).isPresent());
    }

    
    // DELETE /messages/{id}
    @Test
    void deleteMessage() throws Exception {
        // confirm message 12 exists
        assertTrue(messageRepository.findById(12).isPresent());

        MockHttpServletResponse response = mockMvc.perform(delete("/messages/12"))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        // verify message 12 is no longer in the DB
        assertFalse(messageRepository.findById(12).isPresent());
    }
}

