package model.learn

class PrunePunctuation{
	
	private val interPunctChars: Set<Char> = setOf('.','?','!',',',';',':','_','-','(',')','{','}','[',']','“','„','"','/')
	
	fun prune(rawList: List<String>): List<String>{
		val pruned = mutableListOf<String>()
		val returned: List<String> = pruned
		rawList.forEach{
			if(it.length == 1 && interPunctChars.contains(it.get(0))) return@forEach
			pruned.add(it)
		}
		return returned
	}
}
