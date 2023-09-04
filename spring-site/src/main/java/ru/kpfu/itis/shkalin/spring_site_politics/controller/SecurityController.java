package ru.kpfu.itis.shkalin.spring_site_politics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.user.UserFormLoginDto;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.user.UserFormRegisterDto;
import ru.kpfu.itis.shkalin.spring_site_politics.model.Role;
import ru.kpfu.itis.shkalin.spring_site_politics.model.User;
import ru.kpfu.itis.shkalin.spring_site_politics.repository.UserRepository;
import ru.kpfu.itis.shkalin.spring_site_politics.util.ControllerUtil;
import ru.kpfu.itis.shkalin.spring_site_politics.util.ConverterUtil;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@Controller
public class SecurityController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginGet(ModelMap map) {

        map.put("loginData", new UserFormLoginDto());
        return "/security/login";
    }

    @GetMapping("/login-error")
    public String loginFailure(
            ModelMap map) {

        map.put("loginData", new UserFormLoginDto());
        map.put("loginError", "Login error. Check the username and password");
        return "/security/login";
    }

    @GetMapping("/403")
    public String accessDenied(ModelMap map) {
//        throw new CustomAccessDeniedException("You do not have the rights for open this page.");

        return "/errors/403_default";
    }

    @GetMapping("/error")
    public String error() {

        return "error";
    }

    @GetMapping("/reg")
    public String regGet(ModelMap map) {
        if (map.isEmpty()) {
            map.put("regData", new UserFormRegisterDto());
        }

        return "/security/register";
    }

    @PostMapping("/reg")
    public String regPost(
            @Valid @ModelAttribute("userDto") UserFormRegisterDto userDto,
            BindingResult bindingResult,
            ModelMap map) {

        boolean isValidDefault = ControllerUtil.validateDefault(map, bindingResult);
        boolean isValidUsername = validateUsername(userDto, map);
        boolean isValidPasswords = validatePasswords(userDto, map);

        if (isValidDefault && isValidUsername && isValidPasswords) {

            User user = (User) ConverterUtil.updateAndReturn(
                    userDto,
                    User.builder().role(new Role(2)).build());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(user);
            return "redirect:/login";

        } else {
            return showRegForm(map, userDto);
        }
    }

    private boolean validateUsername(UserFormRegisterDto userDto, ModelMap map) {
        Optional<User> userFromDb = userRepository.findByUsername(userDto.getUsername());
        if (userFromDb.isPresent()) {
            map.addAttribute("usernameError", "User with this nickname already exists");
            return false;
        }
        return true;
    }

    private boolean validatePasswords(UserFormRegisterDto userDto, ModelMap map) {
        if (!userDto.getPassword().equals(userDto.getPassword2())) {
            map.addAttribute("passwordError", "The password fields must match");
            map.addAttribute("password2Error", "The password fields must match");
            return false;
        }
        return true;
    }

    private String showRegForm(ModelMap map, UserFormRegisterDto userDto) {
        map.addAttribute("regData", userDto);
        return "/security/register";
    }

}
