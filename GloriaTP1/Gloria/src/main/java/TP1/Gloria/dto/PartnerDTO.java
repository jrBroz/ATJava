package TP1.Gloria.dto;

import TP1.Gloria.enums.PartnerType;

public record PartnerDTO( 
        String name,
        PartnerType specie,
        int loyalty
) {}