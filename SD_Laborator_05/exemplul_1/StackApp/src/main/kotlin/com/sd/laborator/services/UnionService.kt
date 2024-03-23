package com.sd.laborator.services

import com.sd.laborator.interfaces.CartesianProductOperation
import com.sd.laborator.interfaces.UnionOperation
import com.sd.laborator.model.Stack
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UnionService: UnionOperation {

    @Autowired
    private lateinit var cartesianProduct: CartesianProductOperation
    override fun executeOperation(A: Set<Int>, B: Set<Int>): Set<Pair<Int, Int>> {
        val partialResult1 = cartesianProduct.executeOperation(A, B)
        val partialResult2 = cartesianProduct.executeOperation(B, B)

        return partialResult1 union partialResult2
    }

    override fun generateStack(count: Int): Stack? {
        return cartesianProduct.generateStack(count)
    }
}