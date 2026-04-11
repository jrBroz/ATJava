package TP1.Gloria.dto;
import TP1.Gloria.enums.AdventurerType;
import TP1.Gloria.model.Partner;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;


public record AdventurerDTO (
        @NotBlank Long id,
        @NotBlank String name,
        @NotBlank AdventurerType adventurerClass,
        @NotBlank @Positive @Min(1) int level,
        @NotBlank Boolean active,
        Partner partner
    )
    {}