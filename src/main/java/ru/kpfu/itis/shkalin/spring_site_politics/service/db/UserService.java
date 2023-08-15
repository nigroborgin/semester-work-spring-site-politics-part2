package ru.kpfu.itis.shkalin.spring_site_politics.service.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.user.UserFormProfileDto;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.user.UserViewDto;
import ru.kpfu.itis.shkalin.spring_site_politics.exception.CustomAccessDeniedException;
import ru.kpfu.itis.shkalin.spring_site_politics.exception.NotFoundException;
import ru.kpfu.itis.shkalin.spring_site_politics.model.Role;
import ru.kpfu.itis.shkalin.spring_site_politics.model.User;
import ru.kpfu.itis.shkalin.spring_site_politics.repository.RoleRepository;
import ru.kpfu.itis.shkalin.spring_site_politics.repository.UserRepository;
import ru.kpfu.itis.shkalin.spring_site_politics.security.CustomUserDetails;
import ru.kpfu.itis.shkalin.spring_site_politics.util.ConverterUtil;
import ru.kpfu.itis.shkalin.spring_site_politics.util.FileUploaderUtil;
import ru.kpfu.itis.shkalin.spring_site_politics.util.PathRefactorerUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    @Value("${upload.realpath}")
    private String uploadPath;
    @Value("${upload.url.suffix.picture}")
    private String urlSuffixPicture;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void showAll(ModelMap map) {

        List<UserViewDto> userViewDtoList = userRepository.findAll().stream()
                .map(this::getUserViewDto)
                .toList();

        map.put("userList", userViewDtoList);
    }

    public void showOne(CustomUserDetails userSess, Optional<Integer> id, ModelMap map) {

        if (id.isPresent()) {

            User user = userRepository.findById(id.get())
                    .orElseThrow(() -> new NotFoundException("user"));
            UserViewDto userViewDto = getUserViewDto(user);
            boolean showEdit = false;

            if (userSess != null) {
                User userFromSession = userSess.getUser();
                boolean isAccess = Objects.equals(userFromSession.getId(), id.get())
                        || Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN");

                if (isAccess) {
                    showEdit = true;
                }
            }

            map.put("showEdit", showEdit);
            map.put("userView", userViewDto);

        }
    }

    public void showEditForm(CustomUserDetails userSess, Optional<Integer> id, ModelMap map) {

        if (id.isPresent()) {
            boolean showEditRole = false;
            boolean showEditPassword = false;
            User user = userRepository.findById(id.get()).
                    orElseThrow(() -> new NotFoundException("user"));
            UserViewDto userViewDto = getUserViewDto(user);
            User userFromSession = userSess.getUser();
            boolean isAccess = Objects.equals(userFromSession.getId(), id.get())
                    || Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN");

            if (isAccess) {

                if (Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN")) {
                    showEditRole = true;
                }

                if (Objects.equals(userFromSession.getId(), id.get())) {
                    showEditPassword = true;
                }

                map.put("showEditRole", showEditRole);
                map.put("showEditPassword", showEditPassword);
                map.put("userView", userViewDto);
                map.put("userEdit", new UserFormProfileDto());

            } else {
                throw new CustomAccessDeniedException("No rights to edit the user.");
            }
        }
    }

    public void update(CustomUserDetails userSess, Optional<Integer> id, UserFormProfileDto userEdit, MultipartFile picture) throws IOException {

        if (id.isPresent()) {
            User userById = userRepository.findById(id.get()).get();
            User userFromSession = userSess.getUser();
            boolean isAccess = Objects.equals(userFromSession.getId(), id.get())
                    || Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN");

            if (isAccess) {
                String oldPassword = userById.getPassword();
                String editPassword = userEdit.getPassword();

                ConverterUtil.update(userEdit, userById);

                if (editPassword == null || editPassword.isEmpty()) {
                    userById.setPassword(oldPassword);
                } else {
                    editPassword = passwordEncoder.encode(editPassword);
                    userById.setPassword(editPassword);
                }

                List<Role> allRoles = roleRepository.findAll();

                for (Role role : allRoles) {
                    if (Objects.equals(userEdit.getRoleName(), role.getName())) {
                        userById.setRole(role);
                    }
                }

                if (picture != null && !picture.isEmpty()) {
                    String newUploadPath = uploadPath + PathRefactorerUtil.changeSeparator(urlSuffixPicture, "/", File.separator);
                    String mainFileName = id.get() + "_" + userById.getUsername();
                    String newFileName = FileUploaderUtil.uploadFile(picture, newUploadPath, mainFileName);
                    userById.setPictureUrl(urlSuffixPicture + "/" + newFileName);
                }

                userRepository.save(userById);
            } else {
                throw new CustomAccessDeniedException("No rights to update the user.");
            }
        }
    }



    private UserViewDto getUserViewDto(User u) {
        UserViewDto userViewDto = new UserViewDto();
        ConverterUtil.update(u, userViewDto);
        userViewDto.setRoleName(u.getRole().getName());
        return userViewDto;
    }

}
