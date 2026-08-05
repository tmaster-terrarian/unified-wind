# unified wind

A lightweight and highly configurable mod that changes how "wind" works in several mods to utilize shared behavior. Not
strictly required server-side but some effects will not work unless it's installed on both sides. No sound effects or
wind particles included.

### but why??
I got really annoyed by how often I'd see two different particle effects from two different mods move in two different
directions. A textbook "once you see it you can't unsee it" situation lol.

### compatibility
No, I will not backport/update the mod to other Minecraft versions!!!

Currently supported mods:
- Minecraft (several particles are affected by wind!)
- [Particle Rain](https://modrinth.com/mod/particle-rain)
- [Burnt Basic](https://modrinth.com/mod/burnt-basic)
- [Sooty Chimneys](https://modrinth.com/mod/sooty-chimneys)
- [Simple Clouds](https://modrinth.com/mod/simple-clouds)
- [Falling Leaves](https://modrinth.com/mod/fallingleaves)
- [Grassier Grass](https://modrinth.com/mod/grassier-grass)
- [Visuality: Reforged](https://modrinth.com/mod/visuality-forge)
- [Particular Reforged](https://modrinth.com/mod/particular-reforged)
- [Supplementaries](https://modrinth.com/mod/supplementaries)
- ...and more to come (please feel free to suggest any!!)

Mods that will never be supported (and therefore may be broken):
- [Immersive Winds](https://modrinth.com/mod/immersive-winds)

Strictly incompatible mods:
- none yet!

### how does it work?
Wind is generally calm when there's no weather. The wind parameters change based on the weather, and gets weaker when
entering interior spaces like caves. Currently, the actual wind pattern in the mod is a multi-directional derivative of
the wind used by Particle Rain's particles (note: this does not mean Particle Rain is required).

This mod operates by using fairly invasive mixin injections to change the wind behavior of specific mods, rather than
just being an api. This way, mod authors are not required to update their mod to make it work with unified wind. The
primary drawback to this approach is that any/all mods involved need a new, manually written, implementation within
unified wind's code in order to function. For mods seeking to interface with unified wind on their end, see
UnifiedWind.java for available API methods (wiki coming eventually!).

HOPEFULLY it's not too broken, and it's really really unlikely to crash your game (but of course, if it does somehow
crash your game please [report an Issue](https://github.com/tmaster-terrarian/unified-wind/issues)!).

### upcoming/backlog/christmas wishlist
- more robust and versatile mod hooking method
- fabric support via Stonecutter
- more accurate wind speed/direction using a voxel grid simulation with terrain slope + temperature + elevation in mind
- Sable/Aeronautics support: wind optionally pushes flying contraptions (may become a separate mod)
