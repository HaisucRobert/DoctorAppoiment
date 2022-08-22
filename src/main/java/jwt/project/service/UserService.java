package jwt.project.service;

import jwt.project.entity.Role;
import jwt.project.entity.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);

    void addRoleToUser(String username, String roleName);

    List<Role> getUserRoles(Long userId);

    User getUser(String username);
    
    List<User> getUsers();
}
