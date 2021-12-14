package day14

import helper.minAndMax
import java.util.*

fun solve(lines: List<String>, steps: Int): Long {
    val template = lines.first()
    val rules = lines.subList(2, lines.size)
        .map { it.split(" -> ") }
        .map { (pattern, insert) -> InsertionRule(pattern[0], pattern[1], insert[0]) }

    val nodeMap = mutableMapOf<Char, MutableList<LinkedListNode>>()
    val templateList = template.map { LinkedListNode(it) }
        .onEach { nodeMap.getOrPut(it.value) { LinkedList() }.add(it) }
        .zipWithNext()
        .onEach { (a, b) -> a.next = b }
        .first().first


    val runningCounts = template.groupingBy { it }
        .eachCount()
        .mapValues { (_, value) -> value.toLong() }
        .toMutableMap()

    repeat(steps) {
        applyInsertions(nodeMap, rules, runningCounts)
    }

    val (min, max) = runningCounts.values.minAndMax()

    return max - min
}

fun applyInsertions(nodeMap: MutableMap<Char, MutableList<LinkedListNode>>, rules: List<InsertionRule>, runningCounts: MutableMap<Char, Long>) {
    val insertionCandidates = mutableListOf<Pair<LinkedListNode, InsertionRule>>()

    rules.forEach { rule ->
        val matchingRules = (nodeMap[rule.patternA] ?: emptyList())
            .filter { node -> node.next?.value == rule.patternB }
            .map { node -> node to rule }
        insertionCandidates.addAll(matchingRules)
    }

    insertionCandidates.forEach { (node, rule) ->
        val newNode = LinkedListNode(rule.insert)
        newNode.next = node.next
        node.next = newNode
        runningCounts[rule.insert] = (runningCounts[rule.insert] ?: 0L) + 1
        nodeMap.getOrPut(rule.insert) { LinkedList() }.add(newNode)
    }
}


fun solveB(lines: List<String>): Int {
    return 0
}

data class InsertionRule(val patternA: Char, val patternB: Char, val insert: Char) {
}

data class LinkedListNode(val value: Char) : Iterable<Char> {
    var next: LinkedListNode? = null

    override fun iterator(): Iterator<Char> {
        return object : Iterator<Char> {
            var node: LinkedListNode? = this@LinkedListNode
            override fun hasNext(): Boolean {
                return node != null
            }

            override fun next(): Char {
                if (node == null) {
                    throw IndexOutOfBoundsException("Iterator is empty")
                }
                val value = node!!.value
                node = node?.next
                return value
            }

        }
    }
}