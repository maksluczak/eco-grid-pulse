package io.github.maksluczak.backend.energy.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenerationPeriod {

    private String from;
    private String to;
    private List<GenerationMixItem> generationmix;
}
