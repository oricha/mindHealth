package com.mindhealth.mindhealth.security;

import com.mindhealth.mindhealth.domain.User;
import com.mindhealth.mindhealth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String email = (String) attributes.getOrDefault("email", "");
        String name = (String) attributes.getOrDefault("name", email);

        userRepository.findByEmail(email).orElseGet(() -> {
            User u = new User();
            u.setEmail(email);
            u.setName(name);
            u.setFirstName(name);
            u.setLastName("");
            u.setRole("ROLE_USER");
            u.setAvatarUrl((String) attributes.getOrDefault("picture", null));
            // provider fields will be added on entity update
            return userRepository.save(u);
        });

        return oAuth2User;
    }
}

