package com.example.savemoney.controller;


import com.example.savemoney.dto.UserResponseDTO;
import com.example.savemoney.entity.User;
import com.example.savemoney.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/check-id")
    public ResponseEntity<?> checkId(@RequestParam String username) {
        if (username == null) {
            return new ResponseEntity<>("아이디를 입력해주세요.", HttpStatus.BAD_REQUEST);
        } else {
            boolean isExists = userService.checkId(username);
            return new ResponseEntity<>(isExists, HttpStatus.OK);
        }

    }

    //username정보로 User의 모든 정보를 가져오는 API
    @GetMapping("")
    public ResponseEntity<?> getUserInfo() { //파라미터 필요없음(로그인된거로)

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username == null || username.isEmpty()) {
            return new ResponseEntity<>("아이디를 입력해주세요.", HttpStatus.BAD_REQUEST);
        }

        User user = userService.getUserInfo(username);

        if (user == null) {
            return new ResponseEntity<>("해당 유저가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new UserResponseDTO(user), HttpStatus.OK); //UserResponseDTO UserDTO로 이름 변경
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username == null || username.isEmpty()) {
            return new ResponseEntity<>("아이디를 입력해주세요.", HttpStatus.BAD_REQUEST);
        }

        boolean isDeleted = userService.deleteUser(username);
        if (isDeleted) {
            return new ResponseEntity<>("삭제 성공", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("삭제 실패", HttpStatus.NOT_FOUND);
        }
    }
}
