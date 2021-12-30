package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody
                                               @Validated(UserDto.UserView.RegistrationPost.class)
                                               @JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto) {
        log.debug("POST registerUser userDto received {}", userDto.toString());
        if (userService.existsByUsername(userDto.getUsername())) {
            log.warn("Username {} is Already Taken", userDto.getUsername());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is Already Taken!");
        }
        if (userService.existsByEmail(userDto.getEmail())) {
            log.warn("Email {} is Already Taken", userDto.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email is Already Taken!");
        }
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModel);
        log.debug("POST registerUser userModel saved {}", userModel.toString());
        log.info("User saved successfully userId {}", userModel.getUserId());
        userModel.add(linkTo(methodOn(UserController.class).getOneUser(userModel.getUserId())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }

    @GetMapping("/")
    public String index() {
        log.trace("TRACE"); // visualização mais granular, para rastreamentos
        log.debug("DEBUG"); // utilizado em ambientes de desenvolvimento, para apoiar no desenvolvimento, visualizar valores de variáveis...
        log.info("INFO"); // traz informações úteis e relevantes de fluxos que ocorreram
        log.warn("WARN"); // não são erros, é para apontar alertas
        log.error("ERROR"); // utilizado para apontar erros, populares em blocos catch
        return "Logging Spring Boot...";
    }

}
