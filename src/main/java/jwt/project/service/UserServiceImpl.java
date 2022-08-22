package jwt.project.service;

import jwt.project.entity.Role;
import jwt.project.entity.User;
import jwt.project.entity.UserRole;
import jwt.project.repository.RoleRepo;
import jwt.project.repository.UserRepo;
import jwt.project.repository.UserRoleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final UserRoleRepo userRoleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            log.error("User not found in te database");
            throw new UsernameNotFoundException("User not found in te database");
        } else {
            log.info("User found in te database: {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        userRoleRepo.findByUserId(user.getId())
                .forEach(role -> authorities
                        .add(new SimpleGrantedAuthority(roleRepo
                                .findById(role.getRoleId()).orElseThrow().getName())));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public User saveUser(User user) {
        log.info("Saving new user {} to the database", user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Add role {} to user {}", roleName, username);

        User user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);

        userRoleRepo.save(new UserRole(new Random().nextInt(), user.getId(), role.getId()));
    }

    @Override
    public List<Role> getUserRoles(Long userId) {
        return userRoleRepo.findByUserId(userId)
                .stream().map(x -> roleRepo.findById(x.getRoleId()).orElseThrow())
                .collect(Collectors.toList());

    }

    @Override
    public User getUser(String username) {
        log.info("Fetching user {}", username);
        return userRepo.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching all users");
        return userRepo.findAll();
    }
}
