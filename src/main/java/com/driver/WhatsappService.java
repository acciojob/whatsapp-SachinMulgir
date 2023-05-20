package com.driver;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WhatsappService {

    WhatsappRepository whatsappRepository;

    public WhatsappService() {
        whatsappRepository = new WhatsappRepository();
    }

    public String createUser(String name, String mobile) {
        boolean check = this.whatsappRepository.isUserPresent(mobile);
        if( !check ){
            this.whatsappRepository.createUser(name, mobile);
            return "User Created";
        }
        return "User Already Exists";
    }

    public Group createGroup(List<User> users) throws RuntimeException {
        for( User user : users ){
            createUser(user.getName(), user.getMobile());
        }

        if( users.size() < 2 ){
            return null;
        }
        else if( users.size() == 2 ) {
            String grpName = users.get(1).getName();
            Group group = new Group(grpName, users.size());
            this.whatsappRepository.createGroup(group, users);
            return group;
        }
        else{
            int grpCnt = this.whatsappRepository.getCustomGroupCount();
            String grpName = "Group" + grpCnt;
            Group group = new Group(grpName, users.size());
            this.whatsappRepository.createGroup(group, users);
            return group;
        }
    }

    public int createMessage(String content) {
        int msgId = this.whatsappRepository.getMessageId();
        Message msg = new Message(msgId, content);
        this.whatsappRepository.createMessage(msg);
        return msgId;
    }

    public int sendMessage(Message message, User sender, Group group) {
        boolean check = this.whatsappRepository.isGroupPresent(group);
        if( !check ){
            throw new RuntimeException("Group does not exist");
        }
        List<User> userList = this.whatsappRepository.getUsersFromGroup(group);
        if( userList.contains(sender) == false){
            throw new RuntimeException("You are not allowed to send message");
        }
        this.whatsappRepository.sendMessage(group,sender,message);

        return this.whatsappRepository.getMessageCountInGroup(group);
    }


    public String changeAdmin(User approver, User user, Group group) {
        boolean check = this.whatsappRepository.isGroupPresent(group);
        if( !check ){
            throw new RuntimeException("Group does not exist");
        }
        boolean isAdmin = this.whatsappRepository.checkAdmin(group, approver);
        if( !isAdmin ){
            throw new RuntimeException("Approver does not have rights");
        }
        List<User> userList = this.whatsappRepository.getUsersFromGroup(group);
        if( userList.contains(user) == false){
            throw new RuntimeException("User is not a participant");
        }
        this.whatsappRepository.changeAdmin(user,group);

        return "SUCCESS";
    }
}
