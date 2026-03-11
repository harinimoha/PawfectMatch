package com.pawfectmatch.backend.messages;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageRepository repo;

    public MessageController(MessageRepository repo) {
        this.repo = repo;
    }

    // GET /messages → returns all messages
    @GetMapping
    public List<Message> getAllMessages() {
        return repo.findAll();
    }

    // GET /messages/inbox/5 → returns all messages where receiver_id = 5
    @GetMapping("/inbox/{userId}")
    public List<Message> getInbox(@PathVariable Integer userId) {
        List<Message> all = repo.findAll();
        List<Message> inbox = new ArrayList<>();
        for (Message m : all) {
            if (m.getReceiverId().equals(userId)) {
                inbox.add(m);
            }
        }
        return inbox;
    }

    // GET /messages/sent/5 → returns all messages where sender_id = 5
    @GetMapping("/sent/{userId}")
    public List<Message> getSent(@PathVariable Integer userId) {
        List<Message> all = repo.findAll();
        List<Message> sent = new ArrayList<>();
        for (Message m : all) {
            if (m.getSenderId().equals(userId)) {
                sent.add(m);
            }
        }
        return sent;
    }

    // GET /messages/conversation?user1=5&user2=1&petId=1
    // → returns the full back-and-forth thread between two users about a specific pet
    @GetMapping("/conversation")
    public List<Message> getConversation(
            @RequestParam Integer user1,
            @RequestParam Integer user2,
            @RequestParam Integer petId) {
        List<Message> all = repo.findAll();
        List<Message> thread = new ArrayList<>();
        for (Message m : all) {
            boolean rightPet = m.getPetId().equals(petId);
            boolean direction1 = m.getSenderId().equals(user1) && m.getReceiverId().equals(user2);
            boolean direction2 = m.getSenderId().equals(user2) && m.getReceiverId().equals(user1);
            if (rightPet && (direction1 || direction2)) {
                thread.add(m);
            }
        }
        return thread;
    }

    // POST /messages → send a new message
    // Body: { "senderId": 5, "receiverId": 1, "petId": 1, "content": "Is Milo still available?" }
    @PostMapping
    public Message sendMessage(@RequestBody Message message) {
        message.setSentAt(LocalDateTime.now());
        message.setIsRead(false);
        return repo.save(message);
    }

    // PATCH /messages/1/read → mark a message as read
    @PatchMapping("/{id}/read")
    public Message markAsRead(@PathVariable Integer id) {
        Message msg = repo.findById(id).get();
        msg.setIsRead(true);
        return repo.save(msg);
    }

    // DELETE /messages/1 → delete a message
    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable Integer id) {
        repo.deleteById(id);
    }
}