package com.project.api.controller;

import com.project.api.ResponseObject;
import com.project.api.dao.RegisterRepository;
import com.project.api.dto.RegisterDto;
import com.project.api.dto.UpdateUserDto;
import com.project.api.entity.Register;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/api")
public class RegisterController {
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private RegisterRepository registerRepository;

    @PostMapping("/register")
    public ResponseObject Register(@RequestBody Register register) {
        register.setPassword(bCryptPasswordEncoder.encode(register.getPassword()));
        registerRepository.save(register);
        return new ResponseObject("success", null, "Đăng kí thành công");
    }

    @PostMapping("/update-user")
    public ResponseObject UpdateUser(@RequestBody UpdateUserDto register) {
        Register listResult = registerRepository.findById(register.getId()).get();
        if (listResult != null) {
            Register updateUser = new Register();

            updateUser.setId(listResult.getId());
            updateUser.setEmail(listResult.getEmail());
            updateUser.setFirstName(listResult.getFirstName());
            updateUser.setLastName(listResult.getLastName());
            updateUser.setPhone(listResult.getPhone());
            updateUser.setAddress(listResult.getAddress());
            updateUser.setId(listResult.getId());
            updateUser.setPassword(listResult.getPassword());
            updateUser.setRole(listResult.getRole());
            updateUser.setStatus(register.getStatus());

            registerRepository.save(updateUser);
        }
        return new ResponseObject("success", null, "Thành công");
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> Login(@RequestBody Register register) {

        List<Register> listResult = registerRepository.findByEmail(register.getEmail());
        if (listResult.size() == 0) {
            return new ResponseEntity<>(
                    new ResponseObject("fail", null, "Tên tài khoản hoặc mật khẩu không chính xác"),
                    HttpStatus.BAD_REQUEST);

        } else {
            if (!listResult.get(0).getStatus().equals("INACTIVE")) {
                Boolean check = bCryptPasswordEncoder.matches(register.getPassword(), listResult.get(0).getPassword());

                if (check == false) {
                    return new ResponseEntity<>(
                            new ResponseObject("fail", null, "Tên tài khoản hoặc mật khẩu không chính xác"),
                            HttpStatus.BAD_REQUEST);
                }

                else {
                    RegisterDto registerDto = new RegisterDto();
                    registerDto.setId(listResult.get(0).getId());
                    registerDto.setEmail(listResult.get(0).getEmail());
                    registerDto.setFirstName(listResult.get(0).getFirstName());
                    registerDto.setLastName(listResult.get(0).getLastName());
                    registerDto.setPhone(listResult.get(0).getPhone());
                    registerDto.setAddress(listResult.get(0).getAddress());
                    registerDto.setRole(listResult.get(0).getRole());
                    return new ResponseEntity<>(
                            new ResponseObject("success", registerDto, "Thành công"),
                            HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>(
                        new ResponseObject("success", null, "Tài khoản đã bị khóa."),
                        HttpStatus.OK);
            }
        }

    }
}
