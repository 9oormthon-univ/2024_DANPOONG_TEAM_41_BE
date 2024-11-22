package com.allgoing.domain.traditional.service;

import com.allgoing.domain.traditional.domain.Traditional;
import com.allgoing.domain.traditional.dto.TraditionalDto;
import com.allgoing.domain.traditional.repository.TraditionalRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Builder
@RequiredArgsConstructor
public class TraditionalService {
    private final TraditionalRepository traditionalRepository;


    public List<TraditionalDto> allTraditional() {
        List<TraditionalDto> traditionalDtos = traditionalRepository.findAll().stream()
                .map(traditional -> TraditionalDto.builder()
                        .traditionalId(traditional.getTraditionalId())
                        .traditionalName(traditional.getTraditionalName())
                        .traditionalLatitude(traditional.getTraditionalLatitude())
                        .traditionalLongitude(traditional.getTraditionalLongitude())
                        .build()).toList();
        return traditionalDtos;
    }
}
