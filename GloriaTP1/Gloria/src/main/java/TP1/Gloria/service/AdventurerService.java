package TP1.Gloria.service;

import TP1.Gloria.dto.AdventurerDTO;
import TP1.Gloria.enums.AdventurerType;
import TP1.Gloria.model.Adventurer;
import TP1.Gloria.model.Partner;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AdventurerService {

    private final List<Adventurer> adventurers = new ArrayList<>();
    private final AtomicLong generateID = new AtomicLong(101);

    private static final String[] FIRST_NAMES = {
            "katuch", "Arthos", "Mira", "Arthur", "Skye",
            "Orin", "Gabriela", "Iris", "Kieran", "Cassian",
            "Sylas", "Hollow", "Drake", "Nyra", "Kael"
    };

    private static final String[] LAST_NAMES = {
            "Blackwood", "Souza", "Vex", "Ashen", "Valen",
            "Moonshade", "Hollow", "Almeida", "Draven", "Grim"
    };

    public AdventurerService() {
        initDatabase();
    }

    private void initDatabase() {
        for (long id = 1; id <= 100; id++) {
            adventurers.add(createRandomAdventurer(id));
        }
    }

    private Adventurer createRandomAdventurer(long id) {
        String name = randomFullName();
        AdventurerType type = randomEnum(AdventurerType.class);
        int level = ThreadLocalRandom.current().nextInt(1, 101);

        Adventurer adventurer = new Adventurer(id, name, type, level);
        adventurer.setActive(true);
        return adventurer;
    }

    private String randomFullName() {
        String firstName = FIRST_NAMES[ThreadLocalRandom.current().nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[ThreadLocalRandom.current().nextInt(LAST_NAMES.length)];
        return firstName + " " + lastName;
    }

    private <T extends Enum<T>> T randomEnum(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }

    public List<Adventurer> getAllAdventurers() {
        return List.copyOf(adventurers);
    }

    public Adventurer getDataByID(Long id) {
        return adventurers.stream()
                .filter(adv -> adv.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Adventurer não encontrado com o ID: " + id
                ));
    }

    public Adventurer createPartner(Long id, Partner partner) {
        Adventurer adventurer = getDataByID(id);
        adventurer.setPartner(partner);
        return adventurer;
    }

    public Adventurer deletePartner(Long id) {
        Adventurer adventurer = getDataByID(id);
        adventurer.setPartner(null);
        return adventurer;
    }

    public Adventurer createAdventurer(AdventurerDTO dto) {
        Adventurer adventurer = new Adventurer(
                generateID.getAndIncrement(),
                dto.name(),
                dto.adventurerClass(),
                dto.level()
        );
        adventurer.setActive(true);
        adventurers.add(adventurer);
        return adventurer;
    }

    public Adventurer update(Long id, AdventurerDTO dto) {
        Adventurer adventurer = getDataByID(id);

        adventurer.setName(dto.name());
        adventurer.setClass(dto.adventurerClass());
        adventurer.setLevel(dto.level());

        return adventurer;
    }

    public void delete(Long id) {
        Adventurer adventurer = getDataByID(id);
        adventurer.setActive(false);
    }
}