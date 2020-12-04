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
import com.achiever.menschenfahren.base.dto.EventDto;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@ApplicationScope
public class EventService extends AbstractBackendService {

    private final String eventServiceEndpoint;

    private final String eventsServiceEndpoint;

    @Autowired
    public EventService(@NonNull final MenschenFahrenConfig appConfig) {
        super(appConfig);
        final var endPointConfig = appConfig.getEventServiceEndpoints();
        this.eventServiceEndpoint = StringUtils.prependIfMissing(endPointConfig.getEvent(), "/");
        this.eventsServiceEndpoint = StringUtils.prependIfMissing(endPointConfig.getEvents(), "/");
    }

    public List<EventDto> getEvents() {
        return getAllRequest(false, false);
    }

    private List<EventDto> getAllRequest(final boolean alsoVoided, final boolean alsoPrivate) {
        List<EventDto> result = new ArrayList<>();
        try {
            final EventsResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path(this.eventsServiceEndpoint).queryParam(CommonConstants.Params.ALSO_VOIDED, alsoVoided)
                            .queryParam(CommonConstants.Params.ALSO_PRIVATE, alsoPrivate).build())
                    .retrieve().bodyToMono(EventsResponse.class).doOnError(this::handleException).onErrorStop().block();
            if (response != null) {
                result = response.getData();
            }
        } catch (final Exception e) {
            handleException(e);
        }
        return result;
    }

    private static class EventsResponse extends DataResponse<List<EventDto>> {
        public EventsResponse() {
            super(new ArrayList());
        }
    }

}
