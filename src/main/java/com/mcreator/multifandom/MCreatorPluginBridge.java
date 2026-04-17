package com.mcreator.multifandom;

import java.util.List;

/**
 * Integration surface intended for MCreator custom code snippets and procedure bridges.
 */
public final class MCreatorPluginBridge {
    private static final MultiFandomCharacterGenerator GENERATOR = new MultiFandomCharacterGenerator();

    private MCreatorPluginBridge() {
    }

    /**
     * Generates a random character line. Pass "ANY" for all fandoms, or one of the supported fandom names.
     */
    public static String generateCharacterLine(String fandom) {
        return GENERATOR.generateCharacter(fandom).toDisplayLine();
    }

    /**
     * Returns supported fandoms for a UI dropdown in MCreator plugin settings.
     */
    public static List<String> supportedFandoms() {
        return GENERATOR.getSupportedFandoms();
    }

    /**
     * Returns sorted base character names for a fandom roster ("ANY" for all fandoms).
     */
    public static List<String> characterRoster(String fandom) {
        return GENERATOR.getCharacterRoster(fandom);
    }
}
