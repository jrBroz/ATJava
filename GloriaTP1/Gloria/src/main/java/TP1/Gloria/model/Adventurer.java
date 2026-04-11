package TP1.Gloria.model;

import TP1.Gloria.enums.AdventurerType;

public class Adventurer {
    private Long id;
    private String name;
    private AdventurerType adventurerClass;
    private int level;
    private boolean active;
    private Partner partner;

    public Adventurer(Long id, String name, AdventurerType adventurerClass, int level) {
        this.id = id;
        this.name = name;
        this.adventurerClass = adventurerClass;
        this.level = level;
        this.active = true;
        this.partner = null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setClass(AdventurerType adventurerClass) {
        this.adventurerClass = adventurerClass;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }
}
