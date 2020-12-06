package com.achiever.menschenfahren.admin.data.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import com.achiever.menschenfahren.admin.config.MenschenFahrenConfig;
import com.achiever.menschenfahren.base.constants.CommonConstants;
import com.achiever.menschenfahren.base.dto.DataResponse;
import com.achiever.menschenfahren.base.dto.EventTypeCreateDto;
import com.achiever.menschenfahren.base.dto.EventTypeDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@ApplicationScope
public class EventTypeService extends AbstractBackendService {

    private final String eventTypesServiceEndpoint;

    private final String eventTypeServiceEndpoint;

    public EventTypeService(@Nonnull MenschenFahrenConfig appConfig) {
        super(appConfig);
        final var endPointConfig = appConfig.getEventTypeServiceEndpoints();
        this.eventTypesServiceEndpoint = StringUtils.prependIfMissing(endPointConfig.getEventTypes(), "/");
        this.eventTypeServiceEndpoint = StringUtils.prependIfMissing(endPointConfig.getEventType(), "/");
    }

    public EventTypeDto createEventType(@Nonnull final EventTypeCreateDto request) {
        return postRequest(request);
    }

    private EventTypeDto postRequest(@Nonnull final EventTypeCreateDto request) {
        EventTypeDto result = null;
        try {
            final EventTypeResponse response = this.webClient.post().uri(this.eventTypeServiceEndpoint).bodyValue(request).retrieve()
                    .bodyToMono(EventTypeResponse.class).doOnError(this::handleException).onErrorStop().block();

            if (response != null) {
                result = response.getData();
            }
        } catch (final Exception e) {
            handleException(e);
        }
        return result;
    }

    public List<EventTypeDto> getAllEventType() {
        return getAllRequest(true);
    }

    private List<EventTypeDto> getAllRequest(final boolean alsoVoided) {
        List<EventTypeDto> result = new ArrayList<>();
        try {
            final EventTypesResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path(this.eventTypesServiceEndpoint).queryParam(CommonConstants.Params.ALSO_VOIDED, alsoVoided).build())
                    .retrieve().bodyToMono(EventTypesResponse.class).doOnError(this::handleException).onErrorStop().block();
            if (response != null) {
                result = response.getData();
            }
        } catch (final Exception e) {
            handleException(e);
        }
        return result;
    }

    private static class EventTypesResponse extends DataResponse<List<EventTypeDto>> {
        public EventTypesResponse() {
            super(new ArrayList());
        }
    }

    private static class EventTypeResponse extends DataResponse<EventTypeDto> {
        public EventTypeResponse() {
            super(new EventTypeDto());
        }
    }

}
