package model.learn

class PrunePunctuation{
	
	private val interPunctChars: Set<Char> = setOf('.','?','!',',',';',':','_','-','(',')','{','}','[',']','“','„','"','/')
	
	fun prune(rawList: List<String>): List<String>{
		return rawList.filter { !(it.length == 1 && interPunctChars.contains(it.get(0))) }
	}
}
