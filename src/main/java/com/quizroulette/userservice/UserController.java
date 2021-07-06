package com.quizroulette.userservice;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

  private final UserRepository userRepository;

  @GetMapping("/{userId}")
  public UserEntity getUserBy(@PathVariable("userId") String userId) {
    log.info("Getting user for {}", userId);
    Optional<UserEntity> byId = userRepository.findById(userId);
    return byId.orElse(null);
  }

  @PutMapping
  public void updateUser(@RequestBody UserRequest userRequest) {
    log.info("Updating user for {} {}", userRequest.getUsername(), userRequest);
    if(userRepository.findById(userRequest.getUsername()).isEmpty()){
      throw new RuntimeException("Username Not Found");
    }
    var userEntity = userRepository.findById(userRequest.getUsername()).get()
        .toBuilder()
        .avatarUrl(userRequest.getAvatarUrl())
        .password(userRequest.getPassword())
        .build();
    userRepository.save(userEntity);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void addUser(@RequestBody UserRequest userRequest) {
    log.info("adding user for {} {}", userRequest.getUsername(), userRequest);
    var userEntity = UserEntity.builder()
        .avatarUrl(userRequest.getAvatarUrl())
        .username(userRequest.getUsername())
        .password(userRequest.getPassword())
        .build();
    userRepository.save(userEntity);
  }

  @GetMapping
  public List<UserEntity> getAll() {
    log.info("getting all user");
    return userRepository.findAll();
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  static class UserRequest {

    private String username;
    private String password;
    private String avatarUrl;
  }
}
