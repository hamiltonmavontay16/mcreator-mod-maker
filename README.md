# MultiFandom Character Generator Plugin for MCreator 2026.1

This repository contains a lightweight plugin-style Java module that generates random characters from multiple fandoms for use in MCreator procedures.

## Included fandom pools
- Anime
- Marvel
- Star Wars
- Harry Potter
- Lord of the Rings
- Lalaloopsy (all media)
- My Little Pony (MLP / Friendship is Magic)
- Kirby (all games)
- South Park (all media / video games)
- Dandy's World
- Cookie Run: Kingdom (CRK)
- Cookie Run: OvenBreak (CROB)
- MLP Equestria Girls (all movies & specials)

## Quick usage in custom code
Use the bridge class from custom code snippets or plugin hooks:

```java
String generated = com.mcreator.multifandom.MCreatorPluginBridge.generateCharacterLine("ANY");
// or: "ANIME", "MARVEL", "STAR_WARS", "HARRY_POTTER", "LORD_OF_THE_RINGS",
//     "LALALOOPSY", "MLP", "MLP_EQUESTRIA_GIRLS", "KIRBY", "SOUTH_PARK", "DANDYS_WORLD", "CRK", "CROB"
// aliases supported:
// "MY LITTLE PONY", "KIRBY (ALL GAMES)", "LALALOOPSY (ALL MEDIA)", "SOUTH PARK",
// "DANDY'S WORLD", "COOKIE RUN KINGDOM", "COOKIE RUN OVENBREAK", "EQUESTRIA GIRLS"
```

To fetch available fandom values for UI dropdowns:

```java
java.util.List<String> fandoms = com.mcreator.multifandom.MCreatorPluginBridge.supportedFandoms();

// Character roster for one fandom (or "ANY" across all fandoms)
java.util.List<String> roster = com.mcreator.multifandom.MCreatorPluginBridge.characterRoster("MLP_EQUESTRIA_GIRLS");
```

## Build
```bash
./gradlew build
```

## Notes for MCreator 2026.1
- `plugin.json` describes metadata and entrypoint.
- `MCreatorPluginBridge` is intentionally static so it can be called from generated MCreator code paths.
- Character output format is compact and chat-friendly for dialogs, books, GUIs, or NPC names.
