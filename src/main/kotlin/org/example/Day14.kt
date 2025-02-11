class Day1914 {
}

class Reaction(val targetComponent : String, val production : Long, val chemicals : Map<String, Long>)

class Nanofactory(val reactions : Map<String, Reaction>) {
    val rootQuantities = mutableMapOf<String, Long>()
    fun createFuel() {
        val fuelReaction = reactions["FUEL"]!!
        rootQuantities.putAll(fuelReaction.chemicals)
    }

}