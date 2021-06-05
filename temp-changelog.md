### Renamed rules:
* fireChargeConvertsToNetherrack to renewableNetherrack
* dragonsBreathConvertsCobbleToEndstone to renewableEndstone
* dispensersMilkCows to dispensersMilkAnimals

### Added rules:
* dispensersStripBlocks
  * Strips logs, waxed copper blocks, and oxidized copper blocks
  * Closes #86
* dispensersUseCauldrons
  * Fills/empties cauldrons with buckets/bottles, etc
  * Undyes leather armor and shulker boxes
  * Takes layers off banners
  * Closes #66
* dispensersPotPlants
  * Puts flowers in flower pots
  * Compatible with flowerPotChunkLoading
  * Closes #61, closes #151

### Changes to rules:
* dispensersCarvePumpkins
  * Now carves the side of the pumpkin facing the dispenser
* dispensersFeedAnimals
  * Now works with axolotl and goats
  * Fixed being able to keep feeding adult animals after breeding
  * Brown mooshrooms can now be fed flowers to give them a stew effect
  * Should now be more future proof (Any future animals or even modded animals will probably automatically work)
* dispensersFillMinecarts
  * Now requires the target minecart not having an entity inside
  * Fixed minecart name not copying over to new minecart
* dispensersMilkAnimals:
  * Goats can now be milked
  * Suspicious stew can now be gotten from mooshrooms that have a stew effect
  * Now plays the milking sound of cows or goats
* dispensersTillSoil
  * Fixes #174
* dispensersToggleThings
  * Can now make dragon eggs teleport
* renewableEndstone
  * Now has dragon fireball sound and some dragon breath particles when converting
* renewableNetherrack:
  * Now has fire charge sound and some fire particles when converting
* flowerPotChunkLoading
  * Fixed chunks staying force loaded when a wither rose flower pot is not broken by a player
* chickenShearing
  * Now works with dispensers
