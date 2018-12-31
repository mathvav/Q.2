package net.karlshaffer.q.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtAuthorizationDto {
    private String token;
}
