package org.example

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
