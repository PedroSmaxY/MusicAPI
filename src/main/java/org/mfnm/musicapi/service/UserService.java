package org.mfnm.musicapi.service;

import lombok.AllArgsConstructor;
import org.mfnm.musicapi.domain.entity.User;
import org.mfnm.musicapi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(() -> new RuntimeException(
                "User not found! Id: " + id + ", Type: " + User.class.getName()
        ));
    }

    public User findByUsername(String username) {
        Optional<User> user = this.userRepository.findByUsername(username);
        return user.orElseThrow(() -> new RuntimeException(
                "User not found! Username: " + username
        ));
    }

    @Transactional
    public User create(User user) {
        user.setId(null);
        user = this.userRepository.save(user);
        return user;
    }

    @Transactional
    public User update(User user) {
        User newUser = findById(user.getId());
        newUser.setPassword(user.getPassword());
        newUser.setUsername(user.getUsername());
        return this.userRepository.save(newUser);
    }

    public void deleteById(Long id) {
        findById(id);
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Not possible to delete because exist related entities!"
            );
        }
    }
}
