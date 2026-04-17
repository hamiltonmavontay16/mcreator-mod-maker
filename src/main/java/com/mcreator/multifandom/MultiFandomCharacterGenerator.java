package com.mcreator.multifandom;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

/**
 * MultiFandom Character Generator plugin core logic for MCreator 2026.1 projects.
 *
 * <p>Use {@link #generateCharacter(String)} from custom code or plugin bridge blocks.
 */
public class MultiFandomCharacterGenerator {
    private final Random random;
    private final Map<String, List<CharacterSeed>> seedsByFandom;
    private final Map<String, String> aliasesToCanonical;

    public MultiFandomCharacterGenerator() {
        this(new SecureRandom());
    }

    public MultiFandomCharacterGenerator(Random random) {
        this.random = Objects.requireNonNull(random, "random");
        this.seedsByFandom = buildSeedData();
        this.aliasesToCanonical = buildAliases(seedsByFandom.keySet());
    }

    public CharacterProfile generateCharacter(String fandomSelection) {
        String normalized = normalizeFandom(fandomSelection);
        String chosenFandom = normalized.equals("ANY")
            ? pickRandomFandom()
            : validateAndResolveFandom(normalized);

        CharacterSeed seed = pickRandom(seedsByFandom.get(chosenFandom));
        String generatedName = seed.nameRoots().get(random.nextInt(seed.nameRoots().size()))
            + seed.nameSuffixes().get(random.nextInt(seed.nameSuffixes().size()));

        return new CharacterProfile(
            chosenFandom,
            generatedName,
            pickRandom(seed.roles()),
            pickDistinctTraits(seed.traits(), 2 + random.nextInt(2)),
            pickRandom(seed.signatureItems()),
            pickRandom(seed.catchphrases())
        );
    }

    public List<String> getSupportedFandoms() {
        List<String> names = new ArrayList<>(seedsByFandom.keySet());
        Collections.sort(names);
        return names;
    }

    private String normalizeFandom(String fandomSelection) {
        if (fandomSelection == null || fandomSelection.isBlank()) {
            return "ANY";
        }
        return fandomSelection.trim().toUpperCase(Locale.ROOT);
    }

    private String pickRandomFandom() {
        List<String> fandoms = new ArrayList<>(seedsByFandom.keySet());
        return fandoms.get(random.nextInt(fandoms.size()));
    }

    private String validateAndResolveFandom(String normalizedFandom) {
        Optional<String> matching = Optional.ofNullable(aliasesToCanonical.get(normalizedFandom));

        if (matching.isEmpty()) {
            throw new IllegalArgumentException(
                "Unknown fandom '" + normalizedFandom + "'. Supported: " + getSupportedFandoms()
            );
        }
        return matching.get();
    }

    private <T> T pickRandom(List<T> values) {
        return values.get(random.nextInt(values.size()));
    }

    private List<String> pickDistinctTraits(List<String> values, int count) {
        List<String> copy = new ArrayList<>(values);
        Collections.shuffle(copy, random);
        return copy.subList(0, Math.min(count, copy.size()));
    }

    private Map<String, List<CharacterSeed>> buildSeedData() {
        Map<String, List<CharacterSeed>> data = new LinkedHashMap<>();

        data.put("ANIME", List.of(
            CharacterSeed.of(
                List.of("Aka", "Hoshi", "Yuki", "Rin", "Kage"),
                List.of("to", "mi", "nari", "shi", "ra"),
                List.of("Wandering Swordsman", "Spirit Tactician", "Tech Alchemist"),
                List.of("determined", "flashy", "loyal", "awkward", "strategic"),
                List.of("Moonlit Katana", "Seal Scroll", "Neon Familiar"),
                List.of("Believe in your own opening theme!", "My resolve is my power level.")
            ),
            CharacterSeed.of(
                List.of("Sora", "Neko", "Mika", "Ken", "Tora"),
                List.of("hana", "ryu", "ka", "zen", "ri"),
                List.of("Rival Idol", "Arcane Student", "Mecha Pilot"),
                List.of("dramatic", "optimistic", "disciplined", "chaotic"),
                List.of("Training Charm", "Pocket Drone", "Festival Mask"),
                List.of("This is only episode one!", "My heart sync is at 120%!")
            )
        ));

        data.put("MARVEL", List.of(
            CharacterSeed.of(
                List.of("Nova", "Astra", "Quake", "Pulse", "Vega"),
                List.of("strike", "byte", "flare", "guard", "ion"),
                List.of("Street Hero", "Cosmic Defender", "Lab Inventor"),
                List.of("sarcastic", "fearless", "resourceful", "protective"),
                List.of("Nano Gauntlet", "Quantum Badge", "Arc Battery"),
                List.of("Avengers level: handled.", "I brought science and snacks.")
            ),
            CharacterSeed.of(
                List.of("Iron", "Scarlet", "Shadow", "Photon", "Storm"),
                List.of("line", "spark", "cloak", "nova", "pulse"),
                List.of("Multiverse Scout", "Mystic Agent", "Reformed Vigilante"),
                List.of("bold", "quick-witted", "intense", "team-first"),
                List.of("Portal Card", "Vibranium Clip", "Spell Sigil"),
                List.of("Timeline secured. You're welcome.", "Suit up. We have variants.")
            )
        ));

        data.put("STAR_WARS", List.of(
            CharacterSeed.of(
                List.of("Ka", "Re", "Tal", "Vos", "Zen"),
                List.of("dor", "vex", "rin", "kar", "sol"),
                List.of("Jedi Archivist", "Smuggler Captain", "Republic Scout"),
                List.of("calm", "clever", "idealistic", "stubborn"),
                List.of("Kyber Compass", "Holo-Map", "Modified Blaster"),
                List.of("I've got a bad feeling... and a better plan.", "The Force favors the prepared.")
            ),
            CharacterSeed.of(
                List.of("Ly", "Ori", "Mav", "Kor", "Sha"),
                List.of("nix", "tal", "ven", "dra", "len"),
                List.of("Droid Engineer", "Sith Defector", "Cantina Informant"),
                List.of("secretive", "brave", "analytical", "charming"),
                List.of("Scomp Link", "Encrypted Holocron", "Twin Sabers"),
                List.of("Punch it.", "Balance is a moving target.")
            )
        ));

        data.put("HARRY_POTTER", List.of(
            CharacterSeed.of(
                List.of("El", "Theo", "Mar", "Luna", "Cass"),
                List.of("rick", "wyn", "dora", "bert", "ley"),
                List.of("Curse Breaker", "Potion Prodigy", "Forbidden Forest Ranger"),
                List.of("bookish", "curious", "rebellious", "kind"),
                List.of("Runed Wand", "Phoenix Feather Quill", "Mirror Charm"),
                List.of("Mischief managed... mostly.", "I revised that spell last night.")
            ),
            CharacterSeed.of(
                List.of("Row", "Ivy", "Perc", "Dahl", "Finn"),
                List.of("ena", "wick", "ion", "wood", "frey"),
                List.of("Auror Trainee", "Magical Botanist", "House Strategist"),
                List.of("ambitious", "witty", "patient", "fearless"),
                List.of("Self-Stirring Cauldron", "Enchanted Satchel", "Nimbus Compass"),
                List.of("Bravery is a practiced habit.", "Let's keep this off the notice board.")
            )
        ));

        data.put("LORD_OF_THE_RINGS", List.of(
            CharacterSeed.of(
                List.of("Ara", "Bel", "Fin", "Eri", "Thal"),
                List.of("diel", "grim", "anor", "wyn", "ric"),
                List.of("Ranger of the North", "Elven Loremaster", "Dwarven Cartographer"),
                List.of("honorable", "patient", "grim", "steadfast"),
                List.of("Starlit Cloak", "Runic Axe", "Leaf-Brooch"),
                List.of("The road answers only the persistent.", "Even small lights break long shadows.")
            ),
            CharacterSeed.of(
                List.of("Meri", "Pip", "Ros", "Hal", "Gil"),
                List.of("brand", "wise", "thorn", "ion", "dor"),
                List.of("Shire Courier", "Gondor Sentinel", "Rivendell Envoy"),
                List.of("cheerful", "loyal", "cautious", "resolute"),
                List.of("Traveler's Pipe", "Signal Horn", "Map of Eriador"),
                List.of("Second breakfast can wait.", "Stand fast; dawn is close.")
            )
        ));

        data.put("LALALOOPSY", List.of(
            CharacterSeed.of(
                List.of("Bea", "Crumbs", "Dot", "Jewel", "Mittens"),
                List.of(" Spark", " Stitch", " Buttons", " Star", " Patch"),
                List.of("Playroom Inventor", "Storybook Tailor", "Pet Parade Leader"),
                List.of("crafty", "bubbly", "helpful", "imaginative"),
                List.of("Button Wand", "Patchwork Journal", "Mini Tea Set"),
                List.of("Stitch it, fix it, sparkle on!", "Every oopsie can turn into magic.")
            ),
            CharacterSeed.of(
                List.of("Peanut", "Pillow", "Rosy", "Spot", "Sunny"),
                List.of(" Doodle", " Twirl", " Cupcake", " Bloom", " Whimsy"),
                List.of("Circus Star", "Garden Explorer", "Cupcake Courier"),
                List.of("playful", "bright", "kind", "adventurous"),
                List.of("Confetti Umbrella", "Paint Splatter Scarf", "Fairy Picnic Basket"),
                List.of("Let's make today extra colorful!", "Patchwork pals save the day.")
            )
        ));

        data.put("MLP", List.of(
            CharacterSeed.of(
                List.of("Twilight", "Rainbow", "Apple", "Pinkie", "Rarity"),
                List.of(" Gleam", " Dazzle", " Bloom", " Swirl", " Shine"),
                List.of("Friendship Scholar", "Wonderbolt Trainee", "Crystal Messenger"),
                List.of("loyal", "kind", "brave", "optimistic"),
                List.of("Harmony Pendant", "Cloud Saddle", "Cutie Map Scroll"),
                List.of("Friendship is always the best spell.", "Let's make this twenty percent cooler.")
            ),
            CharacterSeed.of(
                List.of("Flutter", "Sunset", "Starlight", "Spike", "Trixie"),
                List.of(" Breeze", " Beam", " Glimmer", " Trail", " Melody"),
                List.of("Animal Whisperer", "Arcane Performer", "Equestria Diplomat"),
                List.of("gentle", "curious", "dramatic", "determined"),
                List.of("Friendship Journal", "Magic Lantern", "Festival Ribbon"),
                List.of("Everypony gets a second chance.", "The magic is in teamwork.")
            )
        ));

        data.put("KIRBY", List.of(
            CharacterSeed.of(
                List.of("Kirby", "Meta", "Bandana", "Adeleine", "Ribbon"),
                List.of(" Star", " Dream", " Puff", " Nova", " Warp"),
                List.of("Star Warrior", "Dream Friend", "Treasure Road Scout"),
                List.of("cheery", "fearless", "hungry", "heroic"),
                List.of("Copy Essence", "Warp Star Fragment", "Maxim Tomato"),
                List.of("Poyo! Adventure time.", "Dream Land won't fall on my watch.")
            ),
            CharacterSeed.of(
                List.of("Dedede", "Magolor", "Taranza", "Susie", "Elfilin"),
                List.of(" Knight", " Gear", " Crown", " Beam", " Wisp"),
                List.of("Dimension Traveler", "Halberd Engineer", "Planet Popstar Guardian"),
                List.of("eccentric", "loyal", "inventive", "bold"),
                List.of("Lor Starcutter Chip", "Energy Sphere", "Friend Heart"),
                List.of("Let's clear this stage in style!", "Another boss rush? Bring it on.")
            )
        ));

        data.put("SOUTH_PARK", List.of(
            CharacterSeed.of(
                List.of("Stan", "Kyle", "Cartman", "Kenny", "Butters"),
                List.of(" Broflovski", " Marsh", " Stotch", " McCormick", " Cartman"),
                List.of("New Kid Ally", "Superhero Coon Friend", "Fractured Tactician"),
                List.of("snarky", "chaotic", "stubborn", "clever"),
                List.of("Coonstagram Phone", "Summon Taco", "Class Ability Sheet"),
                List.of("Respect my authoritah!", "This side quest is seriously messed up.")
            ),
            CharacterSeed.of(
                List.of("Wendy", "Craig", "Tweek", "Token", "Jimmy"),
                List.of(" Testaburger", " Tucker", " Black", " Valmer", " Donovan"),
                List.of("Stick of Truth Veteran", "South Park Quest Giver", "Chaos Counter-Unit"),
                List.of("dry-humored", "anxious", "strategic", "sarcastic"),
                List.of("Cheesy Poof Ration", "Friendship Perk Card", "Quest Marker Badge"),
                List.of("Screw you guys, I'm completing this mission.", "Another turn-based fight? Nice.")
            )
        ));

        data.put("DANDYS_WORLD", List.of(
            CharacterSeed.of(
                List.of("Dandy", "Astro", "Pebble", "Vee", "Shelly"),
                List.of(" Toon", " Twist", " Bloom", " Spark", " Spin"),
                List.of("Gardenview Runner", "Twisted Tracker", "Machine Floor Scout"),
                List.of("quirky", "quick", "cooperative", "tense"),
                List.of("Extractor Toolkit", "Ichor Compass", "Toon Trinket"),
                List.of("Keep extracting and keep moving!", "Don't get twisted out there.")
            ),
            CharacterSeed.of(
                List.of("Sprout", "Cosmo", "Poppy", "Boxten", "Razzle"),
                List.of(" Patch", " Whirl", " Dash", " Gear", " Flick"),
                List.of("Stealth Support", "Team Reviver", "Floor Objective Specialist"),
                List.of("alert", "curious", "brave", "supportive"),
                List.of("Stamina Snack", "Emergency Bandage", "Distraction Decoy"),
                List.of("One more machine and we're clear.", "Stick with the team and survive.")
            )
        ));

        data.put("CRK", List.of(
            CharacterSeed.of(
                List.of("Hollyberry", "Pure Vanilla", "Moonlight", "Sea Fairy", "Frost Queen"),
                List.of(" Cookie", " Warden", " Herald", " Oracle", " Knight"),
                List.of("Kingdom Guardian", "Arena Commander", "Ancient Storykeeper"),
                List.of("noble", "resilient", "strategic", "hopeful"),
                List.of("Soul Jam Fragment", "Kingdom Crest", "Relic Charm"),
                List.of("For the kingdom, we stand together.", "Let's rebuild Earthbread, one step at a time.")
            ),
            CharacterSeed.of(
                List.of("Black Pearl", "Golden Cheese", "White Lily", "Dark Cacao", "Cream Ferret"),
                List.of(" Cookie", " Voyager", " Sentinel", " Sage", " Envoy"),
                List.of("Guild Vanguard", "Story Expedition Leader", "Cookie Alliance Specialist"),
                List.of("fierce", "wise", "determined", "dramatic"),
                List.of("Ancient Insignia", "Topping Satchel", "Treasure Ticket"),
                List.of("No topping can fix bad teamwork.", "The kingdom needs brave hearts.")
            )
        ));

        data.put("CROB", List.of(
            CharacterSeed.of(
                List.of("GingerBrave", "Ninja", "Pirate", "Wizard", "Strawberry"),
                List.of(" Cookie", " Runner", " Dash", " Jumper", " Blazer"),
                List.of("Trophy Race Sprinter", "Breakout Specialist", "Champion's League Climber"),
                List.of("speedy", "focused", "playful", "persistent"),
                List.of("Jelly Pouch", "Magnetic Candy", "Revive Token"),
                List.of("Run fast, collect everything!", "Perfect timing wins every race.")
            ),
            CharacterSeed.of(
                List.of("Lemon", "Fire Spirit", "Wind Archer", "Sea Fairy", "Moonlight"),
                List.of(" Cookie", " Strider", " Comet", " Skater", " Bolt"),
                List.of("Relay Event Ace", "Guild Run Challenger", "Trial Record Breaker"),
                List.of("flashy", "confident", "agile", "competitive"),
                List.of("Super Epic Pet Whistle", "Candy Blessing", "Legendary Map Piece"),
                List.of("Another high score incoming.", "No obstacle is faster than me.")
            )
        ));

        return data;
    }

    private Map<String, String> buildAliases(Iterable<String> canonicalNames) {
        Map<String, String> aliases = new LinkedHashMap<>();
        for (String canonical : canonicalNames) {
            aliases.put(canonical, canonical);
        }

        aliases.put("MY_LITTLE_PONY", "MLP");
        aliases.put("MY LITTLE PONY", "MLP");
        aliases.put("FRIENDSHIP_IS_MAGIC", "MLP");
        aliases.put("FRIENDSHIP IS MAGIC", "MLP");
        aliases.put("LALALOOPSY_ALL_MEDIA", "LALALOOPSY");
        aliases.put("LALALOOPSY (ALL MEDIA)", "LALALOOPSY");
        aliases.put("KIRBY_ALL_GAMES", "KIRBY");
        aliases.put("KIRBY (ALL GAMES)", "KIRBY");
        aliases.put("SOUTH_PARK_ALL_MEDIA", "SOUTH_PARK");
        aliases.put("SOUTH_PARK_ALL_VIDEO_GAMES", "SOUTH_PARK");
        aliases.put("SOUTH PARK", "SOUTH_PARK");
        aliases.put("SOUTH PARK ALL VIDEO GAMES/MEDIA", "SOUTH_PARK");
        aliases.put("DANDY'S WORLD", "DANDYS_WORLD");
        aliases.put("DANDYS WORLD", "DANDYS_WORLD");
        aliases.put("COOKIE_RUN_KINGDOM", "CRK");
        aliases.put("COOKIE RUN KINGDOM", "CRK");
        aliases.put("COOKIE RUN: KINGDOM", "CRK");
        aliases.put("COOKIE_RUN_OVENBREAK", "CROB");
        aliases.put("COOKIE RUN OVENBREAK", "CROB");
        aliases.put("COOKIE RUN: OVENBREAK", "CROB");

        return aliases;
    }

    private record CharacterSeed(
        List<String> nameRoots,
        List<String> nameSuffixes,
        List<String> roles,
        List<String> traits,
        List<String> signatureItems,
        List<String> catchphrases
    ) {
        static CharacterSeed of(
            List<String> nameRoots,
            List<String> nameSuffixes,
            List<String> roles,
            List<String> traits,
            List<String> signatureItems,
            List<String> catchphrases
        ) {
            return new CharacterSeed(nameRoots, nameSuffixes, roles, traits, signatureItems, catchphrases);
        }
    }
}
