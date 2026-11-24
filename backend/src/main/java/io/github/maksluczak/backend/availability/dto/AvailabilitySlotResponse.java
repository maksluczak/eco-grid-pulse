package io.github.maksluczak.backend.availability.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilitySlotResponse {

    private LocalDateTime startTime;
    private boolean availableFor15Minutes;
    private boolean availableFor30Minutes;
    private boolean availableFor60Minutes;
}
