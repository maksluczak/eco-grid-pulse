package io.github.maksluczak.backend.energy;

import io.github.maksluczak.backend.energy.dto.external.GenerationApiResponse;
import io.github.maksluczak.backend.exception.EnergyDataUnavailableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
public class GenerationApiService {

    private static final DateTimeFormatter API_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'").withZone(ZoneOffset.UTC);

    private final RestClient restClient;

    public GenerationApiService(RestClient restClient) {
        this.restClient = restClient;
    }

    public GenerationApiResponse fetchGeneration(Instant from, Instant to) {
        GenerationApiResponse response;
        try {
            response = restClient.get()
                    .uri("/generation/" + API_DATE_FORMAT.format(from) + "/" + API_DATE_FORMAT.format(to))
                    .retrieve()
                    .body(GenerationApiResponse.class);
        } catch (RestClientException e) {
            throw new EnergyDataUnavailableException("Generation mix API not available");
        }

        if (response == null || response.getData() == null || response.getData().isEmpty()) {
            throw new EnergyDataUnavailableException("Generation mix API returned no data");
        }
        return response;
    }
}
