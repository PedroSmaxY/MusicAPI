package org.mfnm.musicapi.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.mfnm.musicapi.domain.entity.User;
import org.mfnm.musicapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> findByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping
    @Validated
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        this.userService.create(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    @Validated
    public ResponseEntity<Void> updateById(@PathVariable Long id, @Valid @RequestBody User user) {
        user.setId(id);
        this.userService.update(user);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/username/{username}")
    @Validated
    public ResponseEntity<Void> updateUsername(@PathVariable String username, @Valid @RequestBody User user) {
        User existingUser = this.userService.findByUsername(username);
        user.setId(existingUser.getId());
        this.userService.update(user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        this.userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/username/{username}")
    public ResponseEntity<Void> deleteUsername(@PathVariable String username) {
        User user = this.userService.findByUsername(username);
        this.userService.deleteById(user.getId());
        return ResponseEntity.noContent().build();
    }
}
