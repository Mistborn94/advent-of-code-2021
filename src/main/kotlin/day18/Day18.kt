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

    abstract fun inOrderTraverse(currentDepth: Int = 0): List<Pair<SnailFish, Int>>
    abstract fun magnitude(): Int
    abstract fun copyWithReplacements(replacements: Map<SnailFish, SnailFish>): SnailFish
    operator fun plus(other: SnailFish): SnailFish = PairFish(this, other).reduce()

    fun reduce(): SnailFish {
        var tree = this
        do {
            val old = tree
            tree = tree.reduceOnce()
        } while (old != tree)
        return tree
    }

    fun reduceOnce(): SnailFish {
        val list = inOrderTraverse()
        val deeplyNestedIndex = list.indexOfFirst { (fish, depth) -> fish is PairFish && depth >= 4 }
        if (deeplyNestedIndex != -1) {
            return doExplode(list, deeplyNestedIndex)
        } else {
            val largeNumberNode = list.firstOrNull { (fish, _) -> fish is ValueFish && fish.value >= 10 }?.first
            if (largeNumberNode != null) {
                return doSplit(largeNumberNode as ValueFish)
            }
        }
        return this
    }

    private fun doExplode(list: List<Pair<SnailFish, Int>>, deeplyNestedIndex: Int): SnailFish {
        val deeplyNestedNode = list[deeplyNestedIndex].first as PairFish
        val leftNode = list.subList(0, deeplyNestedIndex - 1).lastOrNull { (fish, _) -> fish is ValueFish }?.first as ValueFish?
        val rightNode = list.subList(deeplyNestedIndex + 2, list.size).firstOrNull { (fish, _) -> fish is ValueFish }?.first as ValueFish?

        val leftValue = (deeplyNestedNode.left as ValueFish).value
        val rightValue = (deeplyNestedNode.right as ValueFish).value
        val replacements = buildMap {
            put(deeplyNestedNode, ValueFish(0))
            leftNode?.let { put(it, ValueFish(it.value + leftValue)) }
            rightNode?.let<ValueFish, Unit> { put(it, ValueFish(it.value + rightValue)) }
        }
        return copyWithReplacements(replacements)
    }

    private fun doSplit(largeValueNode: ValueFish): SnailFish {
        val newNode = PairFish(ValueFish(largeValueNode.value / 2), ValueFish((largeValueNode.value + 1) / 2))
        return copyWithReplacements(mapOf(largeValueNode to newNode))
    }

    class PairFish(val left: SnailFish, val right: SnailFish) : SnailFish() {
        override fun copyWithReplacements(replacements: Map<SnailFish, SnailFish>): SnailFish {
            return replacements[this] ?: replaceChildren(replacements)
        }

        private fun replaceChildren(replacements: Map<SnailFish, SnailFish>): PairFish {
            val leftReplacement = left.copyWithReplacements(replacements)
            val rightReplacement = right.copyWithReplacements(replacements)
            return if (left == leftReplacement && right == rightReplacement) {
                this
            } else {
                PairFish(leftReplacement, rightReplacement)
            }
        }

        override fun inOrderTraverse(currentDepth: Int): List<Pair<SnailFish, Int>> {
            return buildList {
                addAll(left.inOrderTraverse(currentDepth + 1))
                add(this@PairFish to currentDepth)
                addAll(right.inOrderTraverse(currentDepth + 1))
            }
        }

        override fun magnitude(): Int = 3 * left.magnitude() + 2 * right.magnitude()
        override fun toString(): String = "[$left,$right]"
    }

    class ValueFish(val value: Int) : SnailFish() {
        override fun inOrderTraverse(currentDepth: Int) = listOf(this to currentDepth)
        override fun magnitude(): Int = value
        override fun toString() = value.toString()
        override fun copyWithReplacements(replacements: Map<SnailFish, SnailFish>): SnailFish = replacements[this] ?: this
    }

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