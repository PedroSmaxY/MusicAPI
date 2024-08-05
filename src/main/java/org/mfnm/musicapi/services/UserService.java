package org.mfnm.musicapi.services;

import lombok.AllArgsConstructor;
import org.mfnm.musicapi.domain.user.User;
import org.mfnm.musicapi.domain.user.UserRequestDTO;
import org.mfnm.musicapi.repositories.PlaylistRepository;
import org.mfnm.musicapi.repositories.UserRepository;
import org.mfnm.musicapi.services.exceptions.BusinessLogicException;
import org.mfnm.musicapi.services.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;

    public User findById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(() -> new UserNotFoundException(
                "User with ID " + id + " not found in the system."
        ));
    }

    public User findByUsername(String username) {
        Optional<User> user = this.userRepository.findByUsername(username);
        return user.orElseThrow(() -> new UserNotFoundException(
                "User with username '" + username + "' not found in the system."
        ));
    }

    public User login(UserRequestDTO userRequestDTO) {
        String usernameOrEmail = userRequestDTO.usernameOrEmail();
        String password = userRequestDTO.password();

        User user = userRepository.findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new UserNotFoundException(
                        "User with username or email " + usernameOrEmail + " not found in the system."
                ));

        if (!password.equals(user.getPassword())) {
            throw new BusinessLogicException("Incorrect password");
        }

        return user;
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

    @Transactional
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found in the system."));

        playlistRepository.deleteAll(user.getPlaylists());

        userRepository.deleteById(id);
    }
}
