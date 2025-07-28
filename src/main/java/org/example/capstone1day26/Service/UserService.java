package org.example.capstone1day26.Service;

import lombok.RequiredArgsConstructor;
import org.example.capstone1day26.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserService {
    ArrayList<User> users = new ArrayList<>();
    public ArrayList<User> getAllUsers(){
        return users;
    }

    public boolean addUser(User user){

        for (User u : users){
            if(u.getID().equals(user.getID())){
                return false;
            }
        }
        users.add(user);
        return true;
    }

    public boolean updateUser(User user){
        for (User u : users){
            if(u.getID().equals(user.getID())){
                users.set(users.indexOf(u),user);
                return true;
            }
        }
        return false;
    }

    public boolean deleteUser(String ID){
        for (User u : users){
            if(ID.equals(u.getID())){
                users.remove(u);
                return true;
            }
        }
        return false;
    }





}
