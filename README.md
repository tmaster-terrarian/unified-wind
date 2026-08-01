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

### wind drivers
A "wind driver" is a thin layer between Unified Wind's mixins and a "wind source" — any mod that implements wind in a
sufficiently unique and complicated way. This system allows players to decide which wind pattern they prefer. There is
always at least one wind driver available: the built-in behavior that comes with Unified Wind itself.

Wind driver preferences are synced between client and server where possible.

Each wind driver is capable of:
- Calculating wind direction & speed as a non-normalized `Vector3f`, given a position (`BlockPos` or `double` coordinates)
  and a `Level`. Passing in `level` is only required on the server side. The length of the vector represents wind strength.
- Calculating wind direction & speed as an angle on the Y axis and a magnitude value (required for mods that do not
  support vertical motion, such as Grassier Grass).
- Operating performantly, since wind is calculated one or more times per object influenced by wind — including many
  particle effects — in order to support per-block behavior.

Wind drivers may additionally:
- Respect the weather-based wind settings in Unified Wind's config.
- Respect localized weather added by Simple Clouds or Protomanly's weather mod.

Note: not all wind drivers need to implement fancy per-block multi-directional wind. Global wind is acceptable, though
it won't look as dynamic. Per-block behavior is specifically required for effects such as interior vs. exterior wind
speeds and horizontal speed transitions; vertical transitions can be handled by simply checking the Y position.

#### available wind drivers
- **Unified Wind (built-in)** — the default driver; a multi-directional derivative of Particle Rain's wind pattern
  (note: Particle Rain is not required).

#### planned / in-progress wind drivers
- **Project: Atmosphere** — community contributions welcome; feel free to open a Pull Request!
- **Protomanly's Weather** — community contributions welcome; feel free to open a Pull Request!

For anyone interested in contributing a wind driver implementation, please open a Pull Request. Feedback and concerns
are very welcome!

### upcoming/backlog/christmas wishlist
- more robust and versatile mod hooking method
- fabric support via Stonecutter
- wind driver system (see above)
- more accurate wind speed/direction using a voxel grid simulation with terrain slope + temperature + elevation in mind
- Sable/Aeronautics support: wind optionally pushes flying contraptions (may become a separate mod)