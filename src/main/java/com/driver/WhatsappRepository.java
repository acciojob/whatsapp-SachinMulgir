package com.driver;

import java.sql.Array;
import java.util.*;

import net.bytebuddy.matcher.FilterableList;
import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {



    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Integer, Message> messageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private HashMap<String, User> mobileUserMap;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.mobileUserMap = new HashMap<String, User>();
        this.userMobile = new HashSet<>();
        this.messageMap = new HashMap<Integer, Message>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public boolean isUserPresent(String mobile) {
        if( userMobile.contains(mobile) ){
            return true;
        }
        return false;
    }

    public void createUser(String name, String mobile) {
        userMobile.add(mobile);
        mobileUserMap.put( mobile, new User(name, mobile));
    }

    public int getCustomGroupCount() {
        customGroupCount++;
        return customGroupCount;
    }

    public void createGroup(Group group, List<User> users) {
        groupUserMap.put(group, users);
        adminMap.put(group, users.get(0));
    }

    public int getMessageId() {
        messageId++;
        return messageId;
    }

    public void createMessage(Message msg) {
        messageMap.put(msg.getId(), msg);
    }

    public boolean isGroupPresent(Group group) {
        for( Group grp : groupUserMap.keySet() ){
            if( grp.getName().equals(group.getName()))return true;
        }
        return false;
    }

    public List<User> getUsersFromGroup(Group group) {
        return groupUserMap.get(group);
    }

    public void sendMessage(Group group, User sender, Message message) {
        //group-message map:
        List<Message> list = groupMessageMap.get(group);
        list.add(message);
        groupMessageMap.put(group, list);

        //message-map:

    }

    public int getMessageCountInGroup(Group group) {
        List<Message> list = groupMessageMap.get(group);
        return list.size();
    }

    public boolean checkAdmin(Group group, User approver) {
        if( adminMap.containsKey(group) ){
            if( adminMap.get(group) == approver ){
                return true;
            }
        }
        return false;
    }

    public void changeAdmin(User user, Group group) {
        //adminMap
        adminMap.put(group, user);
    }
}
