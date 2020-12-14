package com.achiever.menschenfahren.admin.data.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import com.achiever.menschenfahren.admin.config.MenschenFahrenConfig;
import com.achiever.menschenfahren.base.constants.CommonConstants;
import com.achiever.menschenfahren.base.dto.DataResponse;
import com.achiever.menschenfahren.base.dto.UserDto;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@ApplicationScope
public class UserService extends AbstractBackendService {

    private final String userServiceEndpoint;

    private final String usersServiceEndpoint;

    @Autowired
    public UserService(@NonNull final MenschenFahrenConfig appConfig) {
        super(appConfig);
        final var endPointConfig = appConfig.getUserServiceEndpoints();
        this.userServiceEndpoint = StringUtils.prependIfMissing(endPointConfig.getUser(), "/");
        this.usersServiceEndpoint = StringUtils.prependIfMissing(endPointConfig.getUsers(), "/");

    }

    /**
     * Get all users both active and deactivated user.
     * 
     * @return
     */
    public List<UserDto> getAllUsers() {
        return getAllRequest(true);
    }

    /**
     * Get all user based on the voided filter.
     *
     * @param alsoVoided
     * @return
     */
    private List<UserDto> getAllRequest(final boolean alsoVoided) {
        List<UserDto> result = new ArrayList<>();
        try {
            final UserResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path(usersServiceEndpoint).queryParam(CommonConstants.Params.ALSO_VOIDED, alsoVoided).build()).retrieve()
                    .bodyToMono(UserResponse.class).doOnError(this::handleException).onErrorStop().block();
            if (response != null) {
                result = response.getData();
            }
        } catch (final Exception e) {
            handleException(e);
        }
        return result;
    }

    /**
     * Returns the size of active users.
     *
     * @return
     */
    public int getUserSize() {
        return getAllRequest(false).size();
    }

    private static class UserResponse extends DataResponse<List<UserDto>> {
        public UserResponse() {
            super(new ArrayList());
        }
    }

}
