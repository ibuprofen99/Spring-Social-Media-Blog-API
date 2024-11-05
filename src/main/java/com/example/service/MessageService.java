package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import java.util.Optional;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    //create a new message
    public Message createMessage(Message message) {
        //check if the account ID is valid
        if (!accountRepository.existsById(message.getPostedBy())) { 
            return null;
        }

        //validate message text length
        if (message.getMessageText() != null && !message.getMessageText().isEmpty() && message.getMessageText().length() < 255) {
            return messageRepository.save(message); 
        }

        return null;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    //get messages by account ID
    public List<Message> getMessagesByAccountId(int accountId) {
        return messageRepository.findByPostedBy(accountId); 
    }
    
    public Message getMessageById(int messageId) {
        return messageRepository.findById(messageId).orElse(null); 
    }

    public int deleteMessage(int messageId) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isPresent()) {
            messageRepository.deleteById(messageId); 
            return 1;
        }
        return 0;
    }
    
    public int updateMessage(Integer messageId, String messageText) {
        if (messageText == null || messageText.trim().isEmpty() || messageText.length() > 255) {
            return 0; 
        }

        //find the message by ID
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            message.setMessageText(messageText); 
            messageRepository.save(message); 
            return 1; 
        }
        return 0;
    }
}
