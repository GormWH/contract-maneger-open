package com.contractmanager6.api.user;

import com.contractmanager6.api.jwt.JwtUtils;
import com.contractmanager6.api.request.UserLoginRequest;
import com.contractmanager6.api.request.UserPasswordChangeRequest;
import com.contractmanager6.api.request.UserRegisterRequest;
import com.contractmanager6.api.response.JwtResponse;
import com.contractmanager6.api.response.MessageResponse;
import com.contractmanager6.api.response.UserListResponse;
import com.contractmanager6.api.response.UserResponse;
import com.contractmanager6.domain.user.User;
import com.contractmanager6.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Transactional
public class UserController {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @GetMapping("/user")
    public UserListResponse list() {
        UserListResponse result = new UserListResponse();
        List<UserResponse> list = userRepository.findAll().stream()
                .map(user -> {
                    UserResponse userResponse = new UserResponse();
                    userResponse.setId(user.getLoginId());
                    userResponse.setIsAdmin(user.getIsAdmin());
                    return userResponse;
                }).collect(Collectors.toList());
        result.setUsers(list);
        return result;
    }

    @PostMapping("/admin/user")
    public ResponseEntity<MessageResponse> register(@Validated @RequestBody UserRegisterRequest userRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(400).body(new MessageResponse(bindingResult.toString()));
        }
        if (userRepository.findByLoginId(userRequest.getId()).isPresent()) {
            return ResponseEntity.status(400).body(new MessageResponse("user id exists"));
        }
        User user = new User();
        user.setLoginId(userRequest.getId());
        user.setPassword(userRequest.getPassword());
        user.setIsAdmin(false); // 일단 무조건 관리자는 아닌 설정(관리자는 db에 직접 넣는 방식만 허용)

        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("success"));
    }

    @PatchMapping("/admin/user/password")
    public ResponseEntity<MessageResponse> changePasswordAdmin(@RequestBody UserPasswordChangeRequest userRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(400).body(new MessageResponse("invalid input"));
        }
        if (userRepository.findByLoginId(userRequest.getId()).isEmpty()) {
            return ResponseEntity.status(400).body(new MessageResponse("invalid user id"));
        }
        User user = userRepository.findByLoginId(userRequest.getId()).get();
        user.setPassword(userRequest.getPassword());
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("success"));
    }

    @PatchMapping("/user/password")
    public ResponseEntity<MessageResponse> changePassword(@RequestBody UserPasswordChangeRequest userRequest, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(400).body(new MessageResponse("invalid input"));
        }
        String loginId = jwtUtils.getLoginIdFromJwtToken(jwtUtils.getJwtFromRequestHeader(request));
        if (loginId.isBlank() || userRepository.findByLoginId(loginId).isEmpty()) {
            return ResponseEntity.status(400).body(new MessageResponse("invalid user"));
        }

        User user = userRepository.findByLoginId(loginId).get();
        user.setPassword(userRequest.getPassword());
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("success"));
    }

    @PostMapping("/user/login")
    ResponseEntity<?> login(@RequestBody UserLoginRequest userLoginRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(401).body(new MessageResponse("invalid input"));
        }

        Optional<User> userOptional = userRepository.findByLoginId(userLoginRequest.getId());
        if (userOptional.isEmpty() || !userOptional.get().getPassword().equals(userLoginRequest.getPassword())) {
            return ResponseEntity.status(401).body(new MessageResponse("id or password wrong"));
        }
        String jwt = jwtUtils.generateTokenFromLoginId(userLoginRequest.getId());
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setAuthentication(jwt);
        jwtResponse.setMessage("success");
        UserResponse userResponse = new UserResponse();
        userResponse.setId(userOptional.get().getLoginId());
        userResponse.setIsAdmin(userOptional.get().getIsAdmin());
        jwtResponse.setUser(userResponse);
        return ResponseEntity.ok(jwtResponse);
    }

    @DeleteMapping("/admin/user")
    ResponseEntity<?> delete(@RequestParam(value = "id", required = false) String userId) {
        if (userId.isBlank()) {
            return ResponseEntity.status(401).body(new MessageResponse("invalid input"));
        }
        Optional<User> userOptional = userRepository.findByLoginId(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body(new MessageResponse("user doesn't exist"));
        }
        if (userOptional.get().getIsAdmin()) {
            return ResponseEntity.status(401).body(new MessageResponse("cannot delete ADMIN"));
        }
        userRepository.deleteById(userOptional.get().getId());
        return ResponseEntity.ok(new MessageResponse("success"));
    }

}
