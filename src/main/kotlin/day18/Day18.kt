package day18

import java.util.*

fun solveA(lines: List<String>): Int {
    return lines.map { SnailFish.parse(it) }
        .reduce { acc, fish -> acc + fish }
        .magnitude()
}

fun solveB(lines: List<String>): Int {
    val allFish = lines.map { SnailFish.parse(it) }
    return allFish.flatMap { first -> allFish.map { second -> first to second } }
        .filter { (first, second) -> first != second }
        .maxOf { (first, second) -> (first + second).magnitude() }
}

sealed class SnailFish {

    abstract var parent: PairFish?

    class PairFish(var left: SnailFish, var right: SnailFish) : SnailFish() {

        init {
            left.parent = this
            right.parent = this
        }

        override var parent: PairFish? = null
        override fun copy(): PairFish = PairFish(this.left.copy(), this.right.copy())
        override fun infixTraverse(currentDepth: Int): List<Pair<SnailFish, Int>> = left.infixTraverse(currentDepth + 1) + listOf(this to currentDepth) + right.infixTraverse(currentDepth + 1)
        override fun magnitude(): Int = 3 * left.magnitude() + 2 * right.magnitude()
        override fun toString(): String = "[$left,$right]"
    }

    class ValueFish(var value: Int) : SnailFish() {
        override var parent: PairFish? = null
        override fun copy(): ValueFish = ValueFish(this.value)
        override fun infixTraverse(currentDepth: Int): List<Pair<SnailFish, Int>> = listOf(this to currentDepth)
        override fun magnitude(): Int = value
        override fun toString() = value.toString()
    }

    operator fun plus(other: SnailFish): SnailFish {
        return PairFish(this, other).reduce()
    }

    fun reduce(): SnailFish {
        val copy = this.copy()
        do {
            val traversedList = copy.infixTraverse()
            val deeplyNestedIndex = traversedList.indexOfFirst { (fish, depth) -> depth >= 4 && fish is PairFish }
            val largeNumberIndex = traversedList.indexOfFirst { (fish, _) -> fish is ValueFish && fish.value >= 10 }
            if (deeplyNestedIndex != -1) {
                doExplode(traversedList, deeplyNestedIndex)
            } else if (largeNumberIndex != -1) {
                doSplit(traversedList, largeNumberIndex)
            }
        } while (deeplyNestedIndex != -1 || largeNumberIndex != -1)
        return copy
    }

    private fun doExplode(traversedFish: List<Pair<SnailFish, Int>>, deeplyNestedIndex: Int) {
        val deeplyNestedNode = traversedFish[deeplyNestedIndex].first as PairFish
        val leftNode = traversedFish.subList(0, deeplyNestedIndex - 1).lastOrNull { (fish, _) -> fish is ValueFish }
        val rightNode = traversedFish.subList(deeplyNestedIndex + 2, traversedFish.size).firstOrNull { (fish, _) -> fish is ValueFish }

        val leftValue = (deeplyNestedNode.left as ValueFish).value
        val rightValue = (deeplyNestedNode.right as ValueFish).value

        (leftNode?.first as ValueFish?)?.apply { value += leftValue }
        (rightNode?.first as ValueFish?)?.apply { value += rightValue }
        deeplyNestedNode.replaceWith(ValueFish(0))
    }

    private fun doSplit(traversedList: List<Pair<SnailFish, Int>>, largeNumberIndex: Int) {
        val largeValueNode = traversedList[largeNumberIndex].first as ValueFish
        val newNode = PairFish(ValueFish(largeValueNode.value / 2), ValueFish((largeValueNode.value + 1) / 2))
        largeValueNode.replaceWith(newNode)
    }

    protected fun replaceWith(new: SnailFish) {
        parent?.let { parent ->
            when (this) {
                parent.left -> parent.left = new
                parent.right -> parent.right = new
                else -> throw IllegalArgumentException("Can't do replace, original is not a child")
            }
            new.parent = parent
        }
    }

    abstract fun infixTraverse(currentDepth: Int = 0): List<Pair<SnailFish, Int>>
    abstract fun copy(): SnailFish
    abstract fun magnitude(): Int

    companion object {
        fun parse(line: String): SnailFish {
            val stack = Stack<SnailFish>()

            line.forEach {
                if (it.isDigit()) {
                    stack.push(ValueFish(it.digitToInt()))
                } else if (it == ']') {
                    val right = stack.pop()
                    val left = stack.pop()
                    val newFish = PairFish(left, right)
                    stack.push(newFish)
                }
            }

            if (stack.size != 1) {
                throw IllegalStateException("Expect only one snail fish left")
            }
            return stack.pop()
        }
    }
}