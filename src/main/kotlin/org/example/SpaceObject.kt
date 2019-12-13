package org.example

class SpaceSystem(val spaceObjects: MutableSet<SpaceObject>) {

    companion object {
        const val ROOT : String = "COM"
        val rootObject = SpaceObject(ROOT, ROOT)
    }

    init {
        spaceObjects.add(rootObject)
    }

    fun calculateTransferCount(firstName: String, secondName: String): Int {
        val distanceFromYouToCOM = distanceFromRoot(findObjectsByName(firstName))
        val distanceFromSanToCOM = distanceFromRoot(findObjectsByName(secondName))
        val distanceFromCommonParentToCOM = distanceFromRoot(findClosestParent(firstName, secondName))

        return distanceFromYouToCOM + distanceFromSanToCOM - 2 * (distanceFromCommonParentToCOM + 1)
    }

    private fun distanceFromRoot(spaceObject: SpaceObject): Int {
        return spaceObject.listParents(spaceObjects).size
    }

    fun findClosestParent(object1: String, object2: String): SpaceObject {
        val spaceObject1 = findObjectsByName(object1)
        val spaceObject2 = findObjectsByName(object2)
        val parents1 = spaceObject1.listParents(spaceObjects)
        val parents2 = spaceObject2.listParents(spaceObjects)
        return parents1.find { parents2.contains(it) }!!
    }

    fun findObjectsByName(name: String): SpaceObject {
        return spaceObjects.find { it.name == name }!!
    }

    fun findParent(first: SpaceObject): SpaceObject {
        return spaceObjects.find { it.name == first.parent }!!
    }

    fun countAllParents() : Int {
        return spaceObjects.map { it.getParentCount(spaceObjects) }.sum()
    }

}

data class SpaceObject(val name: String, val parent: String) {
    var countParents: Int = -1
    fun getParentCount(objects: Set<SpaceObject>): Int {
        return if (countParents != -1)
            countParents
        else computeParentsCount(objects)
    }

    private fun computeParentsCount(objects: Set<SpaceObject>): Int {
        countParents = if (hasParent())
            1 + getParent(objects).getParentCount(objects)
        else
            0
        return countParents
    }

    private fun getParent(objects: Set<SpaceObject>): SpaceObject {
        return objects.findLast { it.name == this.parent }!!
    }

    var listInit: Boolean = false
    var listOfParents: MutableList<SpaceObject> = mutableListOf()
    fun listParents(objects: Set<SpaceObject>): MutableList<SpaceObject> {
        if (!listInit) {
            buildParentsList(objects)
            listInit = true
        }
        return listOfParents
    }

    private fun buildParentsList(objects: Set<SpaceObject>) {
        if (hasParent()) {
            val parent = getParent(objects)
            listOfParents.add(parent)
            listOfParents.addAll(parent.listParents(objects))
        }
    }

    private fun hasParent(): Boolean {
        return name != SpaceSystem.ROOT
    }


}