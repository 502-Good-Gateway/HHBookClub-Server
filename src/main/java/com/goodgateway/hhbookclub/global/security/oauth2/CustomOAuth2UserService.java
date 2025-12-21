package com.goodgateway.hhbookclub.global.security.oauth2;

import com.goodgateway.hhbookclub.domain.user.entity.User;
import com.goodgateway.hhbookclub.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = extractEmail(attributes);
        String providerId = extractProviderId(attributes);
        String nickname = extractNickname(attributes);
        String profileImage = extractProfileImage(attributes);

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(User.builder()
                        .email(email)
                        .nickname(nickname)
                        .profileImage(profileImage)
                        .provider(registrationId)
                        .providerId(providerId)
                        .build()));

        return new OAuth2UserPrincipal(user, attributes);
    }

    private String extractEmail(Map<String, Object> attributes) {
        return (String) attributes.get("email");
    }

    private String extractProviderId(Map<String, Object> attributes) {
        Object sub = attributes.get("sub");
        return sub != null ? sub.toString() : null;
    }

    private String extractNickname(Map<String, Object> attributes) {
        String name = (String) attributes.get("name");
        return name != null ? name : "User_" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String extractProfileImage(Map<String, Object> attributes) {
        return (String) attributes.get("picture");
    }
}
