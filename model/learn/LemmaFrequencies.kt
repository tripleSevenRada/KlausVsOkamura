package model.learn

import kotlin.Comparable

class LemmaDataPerClass{
	private var name: String
		get() = this.toString() + " bound to: " + field
	private val list: List<String>
		get
	private var lemmasToFrequencies = mutableMapOf<String, Int>()
	private var sortedByFrequencies = mutableListOf<PopularityTuple>()
	private val sizeRawListLemmas: Int
		get
	private val sizeUniqueLemmas: Int
		get
	private val TAG = "LEMMA_DATA_PER_CLASS"
	
	constructor(className: String, listLemmas: List<String>){
		name = className
		list = listLemmas
		sizeRawListLemmas = listLemmas.size
		
		list.forEach{
			if(lemmasToFrequencies.keys.contains(it)){
				val lemmasCountNullable: Int? = lemmasToFrequencies.get(it)
				val lemmasCount: Int = if(lemmasCountNullable != null)lemmasCountNullable else -1 
				assert(lemmasCount > 0)
				lemmasToFrequencies.put(it, (lemmasCount + 1))
			} else {
				lemmasToFrequencies.put(it, 1)
			}
		}
		
		sizeUniqueLemmas = lemmasToFrequencies.keys.size
		
		lemmasToFrequencies.keys.forEach{
			val popularity: Int? = lemmasToFrequencies.get(it)
			assert(popularity != null && popularity > 0)
			sortedByFrequencies.add(PopularityTuple(it, if(popularity == null) -1 else popularity))
		}
		sortedByFrequencies.sort()
	}
	
	fun printDataStructures(howMany: Int){
		printStats()
		printMap()
		printSortedByFrequencies(howMany)
	}
	
	fun printStats(){
		println("\n________________________________________________________________________")
		println("$TAG : STATS")
		println("name: $name")
		println("sizeRawListLemmas: $sizeRawListLemmas")
		println("sizeUniqueLemmas: $sizeUniqueLemmas")
	}
	
	fun printMap(){
		println("\n________________________________________________________________________")
		println("$TAG : LEMMAS_TO_FREQUENCIES")
		println(lemmasToFrequencies)
	}
	
	fun printSortedByFrequencies(howMany: Int){
		if(sortedByFrequencies.size == 0) return
		var iterate = howMany
		println("\n________________________________________________________________________")
		println("$TAG : SORTED_BY_FREQUENCIES")
		println("Head: " + sortedByFrequencies.get(0))
		if(iterate > sortedByFrequencies.size) iterate = sortedByFrequencies.size
		if(iterate < 1) iterate = 1
		for(i in 0..iterate -1){
			println(sortedByFrequencies.get(i))
		}
	}
}

data class PopularityTuple(val lemma: String, val frequency: Int): Comparable<PopularityTuple>{
	override fun compareTo(other: PopularityTuple): Int{
		return other.frequency .compareTo(this.frequency)
	}
}