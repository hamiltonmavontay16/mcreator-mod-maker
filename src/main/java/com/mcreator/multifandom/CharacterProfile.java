package com.mcreator.multifandom;

import java.util.List;
import java.util.Objects;

/**
 * Immutable model representing a generated character profile.
 */
public record CharacterProfile(
    String fandom,
    String name,
    String role,
    List<String> traits,
    String signatureItem,
    String catchphrase
) {
    public CharacterProfile {
        Objects.requireNonNull(fandom, "fandom");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(role, "role");
        Objects.requireNonNull(traits, "traits");
        Objects.requireNonNull(signatureItem, "signatureItem");
        Objects.requireNonNull(catchphrase, "catchphrase");
    }

    public String toDisplayLine() {
        String joinedTraits = traits.isEmpty() ? "mysterious" : String.join(", ", traits);
        return "%s | %s (%s) | Traits: %s | Item: %s | \"%s\""
            .formatted(fandom, name, role, joinedTraits, signatureItem, catchphrase);
    }
}
