package day16

import day16.Packet.OperatorPacket
import helper.*
import java.util.*


/**
 * The BITS transmission contains a single packet at its outermost layer which itself contains many other packets.
 * the first three bits encode the packet version,
 * and the next three bits encode the packet type ID
 *
 * Packets with type ID 4 represent a literal value. Literal value packets encode a single binary number.
 *  To do this, the binary number is padded with leading zeroes until its length is a multiple of four bits,
 *  and then it is broken into groups of four bits. Each group is prefixed by a 1 bit except the last group,
 *  which is prefixed by a 0 bit. These groups of five bits immediately follow the packet header. For example,
 *  the hexadecimal string
 *
 * Every other type of packet (any packet with a type ID other than 4) represent an operator that performs some
 *  calculation on one or more sub-packets contained within. Right now, the specific operations aren't important;
 *  focus on parsing the hierarchy of sub-packets.
 *
 * An operator packet contains one or more packets. To indicate which subsequent binary data represents its sub-packets,
 *  an operator packet can use one of two modes indicated by the bit immediately after the packet header;
 *  this is called the length type ID:
 *      * If the length type ID is 0, then the next 15 bits are a number that represents the total length in bits of the sub-packets contained by this packet.
 *      * If the length type ID is 1, then the next 11 bits are a number that represents the number of sub-packets immediately contained by this packet.
 */
fun solveA(line: String): Int {
    val binaryData = hexToBinaryDigits(line)
    val rootPacket = parsePacket(LinkedList(binaryData))
    return rootPacket.versionSum()
}

fun solveB(line: String): Long {
    val binaryData = hexToBinaryDigits(line)
    val rootPacket = parsePacket(LinkedList(binaryData))
    return rootPacket.value
}

private fun hexToBinaryDigits(string: String) = string.flatMapTo(LinkedList()) { it.digitToInt(16).toBinaryDigits(4) }

private fun parsePacket(binaryData: LinkedList<Int>): Packet {

    val version = binaryData.removeFirstN(3).digitsToInt(2)
    val typeId = binaryData.removeFirstN(3).digitsToInt(2)

    if (typeId == 4) {
        val bits = mutableListOf<Int>()
        do {
            val chunk = binaryData.removeFirstN(5)
            bits.addAll(chunk.subList(1, chunk.size))
        } while (chunk[0] != 0)
        return Packet.ValuePacket(version, bits.digitsToLong(2))
    } else {
        val lengthType = binaryData.removeFirstN(1)[0]

        if (lengthType == 0) {
            val totalLength = binaryData.removeFirstN(15).digitsToInt(2)
            val bits = LinkedList(binaryData.removeFirstN(totalLength))

            val parsedPackets = mutableListOf<Packet>()
            while (bits.isNotEmpty()) {
                parsedPackets.add(parsePacket(bits))
            }
            return OperatorPacket(version, typeId, parsedPackets)
        } else {
            val numberOfSubPackets = binaryData.removeFirstN(11).digitsToInt(2)
            val parsedPackets = mutableListOf<Packet>()
            repeat(numberOfSubPackets) {
                parsedPackets.add(parsePacket(binaryData))
            }
            return OperatorPacket(version, typeId, parsedPackets)
        }
    }
}

sealed class Packet {
    abstract fun versionSum(): Int

    abstract val version: Int
    abstract val value: Long

    class ValuePacket(override val version: Int, override val value: Long) : Packet() {
        override fun versionSum(): Int = version

        override fun toString(): String = "$value"
    }

    class OperatorPacket(override val version: Int, private val typeId: Int, private val innerPackets: List<Packet>) : Packet() {
        override fun versionSum(): Int = version + innerPackets.sumOf { it.versionSum() }
        override val value: Long
            get() {
                return when (typeId) {
                    0 -> innerPackets.sumOf { it.value }
                    1 -> innerPackets.map { it.value }.product()
                    2 -> innerPackets.minOf { it.value }
                    3 -> innerPackets.maxOf { it.value }
                    5 -> if (innerPackets[0].value > innerPackets[1].value) 1 else 0
                    6 -> if (innerPackets[0].value < innerPackets[1].value) 1 else 0
                    7 -> if (innerPackets[0].value == innerPackets[1].value) 1 else 0
                    else -> throw IllegalArgumentException("Unknown type $typeId")
                }
            }

        override fun toString(): String = name() + innerPackets.joinToString(separator = ", ", prefix = "(", postfix = ")")

        private fun name() = when (typeId) {
            0 -> "sum"
            1 -> "product"
            2 -> "min"
            3 -> "max"
            5 -> "greater"
            6 -> "less"
            7 -> "equal"
            else -> "unknown"
        }
    }
}

