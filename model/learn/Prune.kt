package model.learn

import app.*

class PrunePunctuation{
	
	private val interPunctChars: Set<Char> = setOf('.','?','!',',',';',':','_','-','(',')','{','}','[',']','“','„','"','/')
	
	fun prune(rawList: List<String>): List<String>{
		fail("PRUNE NOT IMPLEMENTED rawList.size == ${rawList.size}")
		/*
		val pruned = mutableListOf<String>()
		pruned.addAll(rawList)
		val published: List<String> = pruned
		return published
		 */
	}
	
}
