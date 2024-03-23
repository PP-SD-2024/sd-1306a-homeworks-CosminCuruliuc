package com.sd.laborator.interfaces

import com.sd.laborator.model.Stack

interface UnionOperation {
    fun executeOperation(A: Set<Int>, B: Set<Int>): Set<Pair<Int, Int>>

    fun generateStack(count: Int): Stack?
}