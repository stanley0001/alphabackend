package com.example.demo.controllers;

import com.example.demo.model.*;
import com.example.demo.model.models.Reset;
import com.example.demo.model.models.StatusUpdate;
import com.example.demo.model.models.changePas;
import com.example.demo.persistence.entities.Users;
import com.example.demo.services.userService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
@RestController
@CrossOrigin
@Log4j2
@RequestMapping("/users")
public class userController {
    public userService userService;

    public userController(com.example.demo.services.userService userService) {
        this.userService = userService;
    }

    @RequestMapping("/user")
    public Principal user(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization")
                .substring("Basic".length()).trim();
        return () ->  new String(Base64.getDecoder()
                .decode(authToken)).split(":")[0];
    }
    @PostMapping("/create")
    public ResponseEntity<List<Users>> createUser(@RequestBody Users user){
        List<Users> users = userService.saveUser(user);
        return new ResponseEntity<>(users, HttpStatus.CREATED);
    }
    @PostMapping("/update")
    public ResponseEntity<Users> updateUser(@RequestBody Users user){
        log.info("Updating user {}",user);
        Users user1=userService.updateUser(user);

        return new ResponseEntity<>(user1,HttpStatus.CREATED);
        }
        @GetMapping("/all")
    public ResponseEntity<List<Users>> getUsers(){
        List<Users> users=userService.getAll();
        return new ResponseEntity<>(users,HttpStatus.OK);
        }
        @GetMapping("findUserByName{name}")
        public ResponseEntity<Optional<Users>> findUserByName(@PathVariable String name){
            Optional<Users> users=userService.findByName(name);
            return new ResponseEntity<>(users,HttpStatus.OK);
        }
        @GetMapping("/find{id}")
    public ResponseEntity<Optional<Users>> findUsersById(@PathVariable Long id){
        Optional<Users> user= userService.findById(id);
        return new ResponseEntity<>(user,HttpStatus.OK);
        }

        @PostMapping("/createPassword")
    public ResponseEntity<Security> createP(@RequestBody Security security){

        Security security1=userService.createP(security);

        return new ResponseEntity<>(security1,HttpStatus.CREATED);
        }
        @PostMapping ("/updateStatus")
        public ResponseEntity<ResponseModel> updateUserStatus(@RequestBody StatusUpdate update){
           ResponseModel responseModel=userService.changeUserStatus(Long.valueOf(update.getUserId()),update.getStatus());
           return new ResponseEntity<>(responseModel,HttpStatus.OK);
        }


    @PostMapping("/resetPassword")
    public ResponseEntity<ResponseModel> resetPassword(@RequestBody Reset email){
       ResponseModel message=userService.passwordReset(email.getEmail());
       return new ResponseEntity<>(message,HttpStatus.OK);
    }
    @PostMapping("/changePassword")
    public ResponseEntity<ResponseModel> changePassword(@RequestBody changePas password){
        log.info("change password on controller");
        ResponseModel response=userService.changePassword(password.getUserId().toString(),password.getNewPassword());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
        @GetMapping("/findPassword{id}")
        public ResponseEntity<Security> findPassword(@PathVariable String id){
       Security password=userService.findPassword(id);
        return new ResponseEntity<>(password,HttpStatus.OK);
        }
         @PostMapping("/createHistory")
    public ResponseEntity<loginHistory> createHistory(@RequestBody loginHistory history){
        loginHistory loginHistory=userService.createHistory(history);

        return new ResponseEntity<>(HttpStatus.CREATED);
        }

    @GetMapping("/security/roles")
    public ResponseEntity<List<Roles>> getAll(){
        List<Roles> roles =userService.getAllRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
     }
     @PostMapping("/security/createRole")
    public ResponseEntity<Roles> createRole(@RequestBody Roles role){
        Roles roles = userService.createRole(role);
        return new ResponseEntity<>(roles,HttpStatus.CREATED);
     }

}
