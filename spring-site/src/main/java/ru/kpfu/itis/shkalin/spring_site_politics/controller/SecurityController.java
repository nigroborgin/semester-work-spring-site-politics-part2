package ru.kpfu.itis.shkalin.spring_site_politics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.user.UserFormLoginDto;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.user.UserFormRegisterDto;
import ru.kpfu.itis.shkalin.spring_site_politics.model.Role;
import ru.kpfu.itis.shkalin.spring_site_politics.model.User;
import ru.kpfu.itis.shkalin.spring_site_politics.repository.UserRepository;
import ru.kpfu.itis.shkalin.spring_site_politics.util.ConverterUtil;

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

        map.put("regData", new UserFormRegisterDto());
        return "/security/register";
    }

    @PostMapping("/reg")
    public String regPost(
            RedirectAttributes redirectAttributes,
            @ModelAttribute UserFormRegisterDto userDto,
            BindingResult result,
            ModelMap map) {

        String username = userDto.getUsername();
        String password1 = userDto.getPassword();
        String password2 = userDto.getPassword2();

        Optional<User> userFromDb = userRepository.findByUsername(username);
        if (userFromDb.isPresent()) {
            redirectAttributes.addFlashAttribute("message", "User with this nickname already exists");
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/reg";
        }
        if (!password1.equals(password2)) {
            map.put("regData", new UserFormRegisterDto());
//            map.put("message", "The password fields must match");
            redirectAttributes.addFlashAttribute("message", "The password fields must match");
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/reg";
        }

        User user = (User) ConverterUtil.updateAndReturn(
                userDto, User.builder()
                        .role(new Role(2))
                        .build());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return "redirect:/login";
    }

}
