package com.achiever.menschenfahren.admin.data.service;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import com.achiever.menschenfahren.admin.config.MenschenFahrenConfig;
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

    private static class EventTypeResponse extends DataResponse<EventTypeDto> {
        public EventTypeResponse() {
            super(new EventTypeDto());
        }
    }

}
