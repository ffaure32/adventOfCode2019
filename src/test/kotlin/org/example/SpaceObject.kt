package org.example

class SpaceSystem(val spaceObjects: Set<SpaceObject>) {
    public fun calculateTransferCount(firstName: String, secondName: String): Int {
        val you = findObjectsByName(firstName)
        val san = findObjectsByName(secondName)
        val commonParent = findClosestParent(firstName, secondName)

        return you.listParents(spaceObjects).size + san.listParents(spaceObjects).size - 2 * (commonParent.listParents(
            spaceObjects
        ).size + 1)
    }

    public fun findClosestParent(object1: String, object2: String): SpaceObject {
        val spaceObject1 = findObjectsByName(object1)
        val spaceObject2 = findObjectsByName(object2)
        val parents1 = spaceObject1.listParents(spaceObjects)
        val parents2 = spaceObject2.listParents(spaceObjects)
        return parents1.find { parents2.contains(it) }!!
    }

    public fun findObjectsByName(name: String): SpaceObject {
        return spaceObjects.find { it.name == name }!!
    }

    public fun findParent(first: SpaceObject): SpaceObject {
        return spaceObjects.find { it.name == first.parent }!!
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
        return name != AdventOfCodeDay6Test.ROOT
    }


}