package TP1.Gloria.model;

import TP1.Gloria.enums.PartnerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Partner {
    private String name;
    private PartnerType partnertype;
    private int loyalty;
}
