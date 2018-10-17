package model.learn

import app.*

class NgramGraph{
	
	//https://www.dropbox.com/s/hdwlduyfdf3crby/2018-10-17%2011.09.01.jpg?dl=0
	
	private val TAG = this.toString()
	
	private val sizeRawLemmaList: Int
		get
	private val rawLemmaList: List<String>
	
	val lemmaToAdjacents = mutableMapOf<String, MutableSet<String>>()
	
	constructor(lemmaList: List<String>){
		this.sizeRawLemmaList = lemmaList.size
		this.rawLemmaList = lemmaList
	}
	
	//TODO punctuation?
	fun buildNgramGraph(){
		
		if(sizeRawLemmaList == 0) {
			fail("sizeRawLemmaList == 0")
		}
		
		for (i in 0..(sizeRawLemmaList - 2)){
			val lemma: String = rawLemmaList.get(i)
			val nextLemma: String = rawLemmaList.get(i + 1)
			if(lemmaToAdjacents.containsKey(lemma)){
				val lemmaAdj = lemmaToAdjacents.get(lemma)
				if(lemmaAdj != null){
					lemmaAdj.add(nextLemma)
				}else fail("UNEXPECTED NULL in $TAG")
			}else{
				val newAdjSet = mutableSetOf<String>()
				newAdjSet.add(nextLemma)
				lemmaToAdjacents.put(lemma, newAdjSet)
			}
			
			//last lemma is last nextLemma, it may need an empty (leaf) set
			if(i == sizeRawLemmaList - 2){
				if (! lemmaToAdjacents.containsKey(nextLemma))
					lemmaToAdjacents.put(nextLemma, mutableSetOf<String>())
			}
			
		}//for
	}
	
	fun isNgram(lemmasExamined: List<String>, position: Int): Int{
		
		assert(lemmasExamined.size > 0)
		assert(position < (lemmasExamined.size) && position >= 0)
		
		var c = 0
		var currentLemma = lemmasExamined.get(position)
		for(index in position..lemmasExamined.size - 1){
			val adjSet = lemmaToAdjacents.get(currentLemma)
			//UNKNOWN lemma to the MODEL
			if(adjSet == null){
				break
			//KNOWN lemma to the MODEL
			} else {
				//leaf lemma of the MODEL edge case
				if(adjSet.size == 0){
					c ++;
					break
				}
				//NOT last index of lemmasExamined
				if(index < lemmasExamined.size - 1){
					if(adjSet.contains(lemmasExamined.get(index + 1))){
						assert(lemmaToAdjacents.containsKey(lemmasExamined.get(index + 1)))
						currentLemma = lemmasExamined.get(index + 1)
						c ++
					} else {
						c ++
						break
					}
				//LAST index of lemmasExamined
				} else {
					c ++
				}
			}
		}
		return c
	}
	
	fun printDataStructures(){
		println("\n________________________________________________________________________")
		println("$TAG : PRINT DATASTRUCTURES")
		println("_______________________________________")
		println("rawLemmaList: $rawLemmaList")
		println("_______________________________________")
		println("lemmaToAdjacents: $lemmaToAdjacents")
	}
	
	fun printStats(){
		println("\n________________________________________________________________________")
		println("$TAG : PRINT STATS")
		println("_______________________________________")
		println("rawLemmaList size: ${rawLemmaList.size}")
		println("_______________________________________")
		println("lemmaToAdjacents size: ${lemmaToAdjacents.size}")
	}
}
