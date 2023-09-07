package ru.kpfu.itis.shkalin.spring_site_politics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.user.UserFormLoginDto;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.user.UserFormRegisterDto;
import ru.kpfu.itis.shkalin.spring_site_politics.model.AuthName;
import ru.kpfu.itis.shkalin.spring_site_politics.model.AuthThirdParty;
import ru.kpfu.itis.shkalin.spring_site_politics.model.Role;
import ru.kpfu.itis.shkalin.spring_site_politics.model.User;
import ru.kpfu.itis.shkalin.spring_site_politics.model.json_mapping.VkAuthEntity;
import ru.kpfu.itis.shkalin.spring_site_politics.model.json_mapping.VkResponseWithUser;
import ru.kpfu.itis.shkalin.spring_site_politics.model.json_mapping.VkUser;
import ru.kpfu.itis.shkalin.spring_site_politics.repository.AuthNameRepository;
import ru.kpfu.itis.shkalin.spring_site_politics.repository.AuthThirdPartyRepository;
import ru.kpfu.itis.shkalin.spring_site_politics.repository.RoleRepository;
import ru.kpfu.itis.shkalin.spring_site_politics.repository.UserRepository;
import ru.kpfu.itis.shkalin.spring_site_politics.security.CustomUserDetails;
import ru.kpfu.itis.shkalin.spring_site_politics.util.ControllerUtil;
import ru.kpfu.itis.shkalin.spring_site_politics.util.ConverterUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@Controller
public class SecurityController {

    @Value("${vk.getCode.url}")
    private String getUrlVkCode;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthNameRepository authNameRepository;
    @Autowired
    private AuthThirdPartyRepository authThirdPartyRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    ObjectMapper objectMapper = new ObjectMapper();
    OkHttpClient client = new OkHttpClient();
    String urlForAccessToken = "https://oauth.vk.com/access_token";
    String urlForRequests = "https://api.vk.com/method";

    @GetMapping("/login")
    public String login(ModelMap map) {

        map.put("loginData", new UserFormLoginDto());
        map.put("vkCodeUrl", getUrlVkCode);
        return "/security/login";
    }

    @GetMapping("/vk-auth/code")
    public String vkAuth(
            HttpServletRequest httpRequest,
            @RequestParam(value = "code", required = false) String userToken,
            ModelMap map) throws IOException {

        VkAuthEntity vkAuthEntity = requestVkAuthEntity(userToken);

        Optional<User> userByEmail = userRepository.findByEmail(vkAuthEntity.getEmail());

        if (userByEmail.isPresent()) {
            // Есть зарегистрированный пользователь с таким email
            User user = userByEmail.get();
            AuthThirdParty vkAuthThirdParty;

            if (user.getAuthThirdParty() == null) {
                // Пользователь ещё не авторизовался через ВК --> добавляем к существующему связь с ВК
                vkAuthThirdParty = getVkAuthThirdParty(vkAuthEntity);
                user.setAuthThirdParty(vkAuthThirdParty);
            } else {
                // Пользователь уже авторизовался через ВК --> обновляем на всякий случай токен
                vkAuthThirdParty = user.getAuthThirdParty();
                vkAuthThirdParty.setToken(vkAuthEntity.getAccessToken());
            }
            // В обоих случаях сохраняем пользователя и авторизуем на нашем сайте
            authThirdPartyRepository.save(vkAuthThirdParty);
            userRepository.save(user);
            setUserAuth(user);
        } else {
            // Нет зарегистрированных пользователей с таким email
            Optional<User> userByIdInThirdPartyService = userRepository.findByIdInThirdPartyService(
                    vkAuthEntity.getUserId().toString());

            if (userByIdInThirdPartyService.isPresent()) {
                // Есть пользователь с таким id на стороннем сервисе (vk-id пользователя)
                // --> авторизуем пользователя на сайте
                setUserAuth(userByIdInThirdPartyService.get());
            } else {
                // Нет пользователя с таким id на стороннем сервисе (vk-id пользователя)
                // --> регистрируем нового пользователя
                AuthThirdParty vkAuthThirdParty = getVkAuthThirdParty(vkAuthEntity);
                VkUser vkUser = requestVkUser(vkAuthEntity.getUserId(), vkAuthEntity.getAccessToken());
                User newUser =
                        User.builder()
                                .username(vkUser.getFirstName() + " " + vkUser.getLastName() + " " + vkUser.getId())
                                .email(vkAuthEntity.getEmail())
                                .role(roleRepository.getReferenceById(2))
                                .authThirdParty(vkAuthThirdParty)
                                // генерация пароля
                                .password(passwordEncoder.encode(vkAuthEntity.toString()))
                                .build();

                authThirdPartyRepository.save(vkAuthThirdParty);
                userRepository.save(newUser);
                // и сразу авторизуем
                setUserAuth(newUser);
            }
        }
        return "redirect:/main";
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
    public String reg(ModelMap map) {
        if (map.isEmpty()) {
            map.put("regData", new UserFormRegisterDto());
        }

        return "/security/register";
    }

    @PostMapping("/reg")
    public String regHandle(
            @Valid @ModelAttribute("userDto") UserFormRegisterDto userDto,
            BindingResult bindingResult,
            ModelMap map) {

        boolean isValidDefault = ControllerUtil.validateDefault(map, bindingResult);
        boolean isValidEmail = validateEmail(userDto, map);
        boolean isValidPasswords = validatePasswords(userDto, map);

        if (isValidDefault && isValidEmail && isValidPasswords) {

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

    private boolean validateEmail(UserFormRegisterDto userDto, ModelMap map) {
        Optional<User> userFromDb = userRepository.findByEmail(userDto.getUsername());
        if (userFromDb.isPresent()) {
            map.addAttribute("emailError", "User with this email already exists");
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

    private VkUser requestVkUser(Integer userId, String accessToken) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(urlForRequests + "/users.get").newBuilder();
        urlBuilder.addQueryParameter("user_ids", userId.toString());
        urlBuilder.addQueryParameter("access_token", accessToken);
        urlBuilder.addQueryParameter("v", "5.131");

        Call call = client.newCall(
                new Request.Builder()
                        .url(urlBuilder.build().toString())
                        .build()
        );
        Response response = call.execute();
        String vkUserJson = response.body().string();
        VkResponseWithUser vkResponseWithUser = objectMapper.readValue(vkUserJson, VkResponseWithUser.class);

        return vkResponseWithUser.getUsers()[0];
    }

    private VkAuthEntity requestVkAuthEntity(String userToken) throws IOException {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(urlForAccessToken).newBuilder();
        urlBuilder.addQueryParameter("client_id", "51744552");
        urlBuilder.addQueryParameter("client_secret", "51aWaFDvXFP3dNNxiDiX");
        urlBuilder.addQueryParameter("redirect_uri", "http://localhost:8080/vk-auth/code");
        urlBuilder.addQueryParameter("code", userToken);

        Call call = client.newCall(
                new Request.Builder()
                        .url(urlBuilder.build().toString())
                        .build()
        );

        Response response = call.execute();
        String vkAuthEntityJson = response.body().string();
        VkAuthEntity vkAuthEntity = objectMapper.readValue(vkAuthEntityJson, VkAuthEntity.class);
        return vkAuthEntity;
    }

    private AuthThirdParty getVkAuthThirdParty(VkAuthEntity vkAuthEntity) {
        AuthName vkAuthServiceName = authNameRepository.findByName("vk").get();
        String vkAccessToken = vkAuthEntity.getAccessToken();
        String vkUserId = vkAuthEntity.getUserId().toString();
        AuthThirdParty vkAuthThirdParty = AuthThirdParty.builder()
                .authName(vkAuthServiceName)
                .token(vkAccessToken)
                .idInService(vkUserId)
                .build();
        return vkAuthThirdParty;
    }

    private void setUserAuth(User user) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        CustomUserDetails userDetails = new CustomUserDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities());
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

}
