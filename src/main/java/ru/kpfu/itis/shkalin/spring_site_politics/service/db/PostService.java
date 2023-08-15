package ru.kpfu.itis.shkalin.spring_site_politics.service.db;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.post.PostFormDto;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.post.PostViewDto;
import ru.kpfu.itis.shkalin.spring_site_politics.exception.CustomAccessDeniedException;
import ru.kpfu.itis.shkalin.spring_site_politics.exception.NotFoundException;
import ru.kpfu.itis.shkalin.spring_site_politics.model.Post;
import ru.kpfu.itis.shkalin.spring_site_politics.model.User;
import ru.kpfu.itis.shkalin.spring_site_politics.repository.PostRepository;
import ru.kpfu.itis.shkalin.spring_site_politics.repository.UserRepository;
import ru.kpfu.itis.shkalin.spring_site_politics.security.CustomUserDetails;
import ru.kpfu.itis.shkalin.spring_site_politics.util.ConverterUtil;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    public void showAll(CustomUserDetails userSess, ModelMap map) {

        boolean showEdit = false;
        boolean showDelete = false;

        List<PostViewDto> postViewDtoList = postRepository.findAll().stream()
                .map(this::getPostViewDto)
                .sorted(new Comparator<PostViewDto>() {
                    @Override
                    public int compare(PostViewDto o1, PostViewDto o2) {
                        if (o1.getId() == o2.getId())
                            return 0;
                        else if (o1.getId() < o2.getId())
                            return 1;
                        else
                            return -1;
                    }})
                .toList();

        if (userSess != null) {
            User userFromSession = userSess.getUser();
            boolean isAccess = Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN");

            if (isAccess) {
                showEdit = true;
                showDelete = true;

            }
        }


        map.put("showNew", true);
        map.put("showEdit", showEdit);
        map.put("showDelete", showDelete);

        map.put("postList", postViewDtoList);
    }

    public void showOne(CustomUserDetails userSess, Optional<Integer> id, ModelMap map) {

        if (id.isPresent()) {

            Post post = postRepository.findById(id.get())
                    .orElseThrow(() -> new NotFoundException("post"));
            PostViewDto postViewDto = getPostViewDto(post);
            boolean showEdit = false;
            boolean showDelete = false;

            if (userSess != null) {
                User userFromSession = userSess.getUser();
                boolean isAccess = Objects.equals(post.getUser().getId(), userFromSession.getId())
                        || Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN");

                if (isAccess) {
                    showEdit = true;
                    showDelete = true;
                }
            }

            map.put("showNew", true);
            map.put("showEdit", showEdit);
            map.put("showDelete", showDelete);

            map.put("postView", postViewDto);

        }
    }

    public void showNewForm(CustomUserDetails userSess, ModelMap map) {

        if (userSess != null) {
            map.put("showNew", false);
            map.put("showDelete", false);
            map.put("showEdit", false);

            PostViewDto postViewDto = new PostViewDto();
            postViewDto.setAuthorOfPost(userSess.getUsername());
            map.put("postView", postViewDto);
            map.put("postForm", new PostFormDto());

        } else {
            throw new CustomAccessDeniedException("No rights to create the post.");
        }
    }

    public void showEditForm(CustomUserDetails userSess, Optional<Integer> id, ModelMap map) {

        if (id.isPresent()) {
            Post post = postRepository.findById(id.get())
                    .orElseThrow(() -> new NotFoundException("post"));
            PostViewDto postViewDto = getPostViewDto(post);
            User userFromSession = userSess.getUser();
            boolean isAccess = Objects.equals(post.getUser().getId(), userFromSession.getId())
                    || Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN");

            if (isAccess) {
                map.put("showNew", true);
                map.put("showEdit", false);
                map.put("showDelete", true);

                map.put("postView", postViewDto);
                map.put("postForm", new PostFormDto());

            } else {
                LoggerFactory.getLogger(PostService.class).error("GET EDIT. No rights to edit the post.");
                throw new CustomAccessDeniedException("No rights to edit the post.");
            }
        }
    }

    public void create(CustomUserDetails userSess, PostFormDto postFormDto) {

        Post newPost = (Post) ConverterUtil.updateAndReturn(postFormDto, new Post());
        newPost.setUser(userSess.getUser());
        newPost.setAuthorOfPost(userSess.getUsername());
        newPost.setDate(LocalDateTime.now());

        postRepository.save(newPost);
    }

    public void update(CustomUserDetails userSess, PostFormDto newData, Optional<Integer> id) {

        if (id.isPresent()) {
            Post postById = postRepository.findById(id.get())
                    .orElseThrow(() -> new NotFoundException("post"));
            User userFromSession = userSess.getUser();
            boolean isAccess = Objects.equals(postById.getUser().getId(), userFromSession.getId())
                    || Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN");

            if (isAccess) {
                ConverterUtil.update(newData, postById);
                postRepository.save(postById);

            } else {
                throw new CustomAccessDeniedException("No rights to update the post.");
            }
        }
    }

    public void delete(CustomUserDetails userSess, Optional<Integer> id) {

        if (id.isPresent()) {

            Post postById = postRepository.findById(id.get())
                    .orElseThrow(() -> new NotFoundException("post"));
            User userFromSession = userSess.getUser();
            boolean isAccess = Objects.equals(postById.getUser().getId(), userFromSession.getId())
                    || Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN");

            if (isAccess) {
                postRepository.deleteById(id.get());

            } else {
                throw new CustomAccessDeniedException("No rights to delete the post.");
            }
        }
    }

    public void showByUserId(CustomUserDetails userSess, Optional<Integer> userId, ModelMap map) {

        if (userId.isPresent()) {
            User byUser = userRepository.findById(userId.get())
                    .orElseThrow(() -> new NotFoundException("user"));
            boolean showEdit = false;
            boolean showDelete = false;
            List<PostViewDto> postsByUserId = postRepository.findAllByUser(userId.get()).stream()
                    .map(this::getPostViewDto)
                    .sorted(new Comparator<PostViewDto>() {
                        @Override
                        public int compare(PostViewDto o1, PostViewDto o2) {
                            if (o1.getId() == o2.getId())
                                return 0;
                            else if (o1.getId() < o2.getId())
                                return 1;
                            else
                                return -1;
                        }})
                    .toList();

            if (userSess != null) {
                User userFromSession = userSess.getUser();
                boolean isAccess = Objects.equals(userId.get(), userFromSession.getId())
                        || Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN");

                if (isAccess) {
                    showEdit = true;
                    showDelete = true;

                }
            }

            map.put("showNew", true);
            map.put("showEdit", showEdit);
            map.put("showDelete", showDelete);

            map.put("postList", postsByUserId);
            map.put("nameByUser", byUser.getUsername());
        }
    }


    private PostViewDto getPostViewDto(Post post) {

        PostViewDto postViewDto = new PostViewDto();
        ConverterUtil.update(post, postViewDto);
        LocalDateTime postDate = post.getDate();

        if (postDate != null) {
            postViewDto.setDate(getFormattedDateTime(postDate));
        }

        return postViewDto;
    }

    private String getFormattedDateTime(LocalDateTime date) {

        StringBuilder s = new StringBuilder();

        int year = date.getYear();
        int month = date.getMonth().getValue();
        int dayOfMonth = date.getDayOfMonth();
        int hour = date.getHour();
        int minute = date.getMinute();

        if (dayOfMonth < 10) {
            s.append("0");
        }
        s.append(dayOfMonth).append(".");
        if (month < 10) {
            s.append("0");
        }
        s.append(month).append(".").append(year).append("\n");
        if (hour < 10) {
            s.append("0");
        }
        s.append(hour).append(":");
        if (minute < 10) {
            s.append("0");
        }
        s.append(minute);

        return s.toString(); // Format: dd.MM.yyyy   hh:mm
    }

}
