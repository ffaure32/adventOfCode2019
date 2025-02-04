package org.example

import kotlin.math.ceil

class Reaction(val outputChemical: Chemical, val inputChemicals: Set<Chemical>) {
}

data class Chemical(val quantity: Int, val chemicalLitteral: String) {

}

class Reactions(val reactionSet: Set<Reaction>) {
    private val reactionsReducted = mutableSetOf<Reaction>()
    val needs = mutableMapOf<String, Int>()
    val wastes = mutableMapOf<String, Int>()
    fun computeNeeds(): MutableMap<String, Int> {
        val lastReaction = findReaction("FUEL")
        lastReaction.inputChemicals.forEach { needs.put(it.chemicalLitteral, it.quantity) }
        reactionsReducted.add(lastReaction)
        calculateORENeeds()
        return needs
    }

    fun computeTrillionORE() : Long {
        var oreCount = 0L
        var fuelCount = 0L
        val needs = mutableMapOf<String, Int>()
        val stock = mutableMapOf<String, Int>()
        while(oreCount < 1000000000000) {
            val lastReaction = findReaction("FUEL")
            lastReaction.inputChemicals.forEach { needs.put(it.chemicalLitteral, it.quantity) }
            while(needs.any { it.key != "ORE" }) {
                val newNeeds = mutableMapOf<String, Int>()
                needs.filter { it.key != "ORE" }.forEach { key, neededQuantity ->
                    val existingStock = stock.getOrDefault(key, 0)
                    val toProduceQuantity = neededQuantity - existingStock
                    if(toProduceQuantity<=0) {
                        stock[key] = existingStock - neededQuantity
                    } else {
                        stock[key] = 0
                        val targetReaction = findReaction(key)
                        val producibleQuantity = targetReaction.outputChemical.quantity

                        val toProduce = ceil(toProduceQuantity.toFloat()/producibleQuantity.toFloat()).toInt()
                        val waste = toProduce * producibleQuantity - toProduceQuantity
                        stock[key] = waste
                        newNeeds.putAll(targetReaction.inputChemicals.map { it.chemicalLitteral to it.quantity * toProduce + newNeeds.getOrDefault(it.chemicalLitteral, 0)})
                    }
                }
                needs.keys.filter { it != "ORE" }.forEach { needs.remove(it) }
                newNeeds.forEach { (key, value) -> needs[key] = value + needs.getOrDefault(key, 0) }
            }
            oreCount+=needs.getOrDefault("ORE", 0)
            needs.remove("ORE")
            fuelCount++
        }
        return fuelCount
    }





    private fun calculateORENeeds() {
        var exit = false
        while (!exit) {
            val reactionsWithoutParent = reactionSet.minus(reactionsReducted)
            val reactionsNeeded = reactionsWithoutParent.map { it.outputChemical }.filter {
                needs.keys.contains(it.chemicalLitteral)
            }
            val componentsNotUsedAsInputs = reactionsNeeded.filter { need ->
                    reactionsWithoutParent.flatMap { it.inputChemicals }
                    .none { it.chemicalLitteral == need.chemicalLitteral }
            }.toSet()
            exit = componentsNotUsedAsInputs.isEmpty()
            if(!exit) {
                replaceComponentsWhithChildren(componentsNotUsedAsInputs)
            }
        }
        println(needs)
    }


    private fun replaceComponentsWhithChildren(
        componentsNotUsedAsInputs: Set<Chemical>
    ) {
        val inputChemicalsToTransform = needs.filter {
            componentsNotUsedAsInputs.map {it.chemicalLitteral }.contains(it.key)
        }
        inputChemicalsToTransform.forEach {
            val target = it.value
            val reactionForComponent = findReaction(it.key)
            val quantityToProduce = reactionForComponent.outputChemical.quantity
            val rate = calculateSimpleNeed(quantityToProduce, target)
            computeWaste(reactionForComponent.outputChemical, rate)
            reactionForComponent.inputChemicals.forEach {
                computeNeeds(it, rate)
            }
            needs.remove(it.key)
            reactionsReducted.add(reactionForComponent)
        }
    }

    fun computeWaste(chemical: Chemical, rate : Int) {
        val quantityWasted = chemical.quantity * rate - needs.getOrDefault(chemical.chemicalLitteral, 1)
        if(quantityWasted>0) {
            val existingWaste = wastes[chemical.chemicalLitteral]
            if (existingWaste == null) {
                wastes[chemical.chemicalLitteral] = quantityWasted
            } else {
                wastes[chemical.chemicalLitteral] = quantityWasted + existingWaste
            }
        }
    }

    fun computeNeeds(chemical: Chemical, rate: Int) {
        val existingQuantity = needs[chemical.chemicalLitteral]
        if (existingQuantity == null) {
            needs[chemical.chemicalLitteral] = chemical.quantity * rate
        } else {
            needs[chemical.chemicalLitteral] = chemical.quantity * rate + existingQuantity
        }
    }

    private fun findReaction(chemicalName: String): Reaction {
        return reactionSet.first { it.outputChemical.chemicalLitteral == chemicalName }
    }



}

fun calculateSimpleNeed(quantity: Int, target: Int): Int {
    return if (target % quantity == 0) {
        target / quantity
    } else {
        target / quantity + 1
    }
}
