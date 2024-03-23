package com.sd.laborator.interfaces

import com.sd.laborator.model.Stack

interface CartesianProductOperation {
    fun executeOperation(A: Set<Int>, B: Set<Int>): Set<Pair<Int, Int>>

    fun generateStack(count: Int): Stack?
}