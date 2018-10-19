package model.learn

import app.fail

class NgramGraph {

	// https://www.dropbox.com/s/hdwlduyfdf3crby/2018-10-17%2011.09.01.jpg?dl=0

	private val TAG = this.toString()

	val sizeRawLemmaList: Int
		get
	val rawLemmaList: List<String>
		get
	private val lemmaToAdjacents = mutableMapOf<String, MutableSet<String>>()

	constructor(lemmaList: List<String>) {
		this.sizeRawLemmaList = lemmaList.size
		this.rawLemmaList = lemmaList
		buildNgramGraph()
	}

	// TODO punctuation?
	private fun buildNgramGraph() {

		if (sizeRawLemmaList == 0) {
			fail("sizeRawLemmaList == 0")
		}

		for (i in 0..(sizeRawLemmaList - 2)) {
			val lemma: String = rawLemmaList.get(i)
			val nextLemma: String = rawLemmaList.get(i + 1)
			if (lemmaToAdjacents.containsKey(lemma)) {
				val lemmaAdj = lemmaToAdjacents.get(lemma)
				if (lemmaAdj != null) {
					lemmaAdj.add(nextLemma)
				} else fail("UNEXPECTED NULL in $TAG")
			} else {
				val newAdjSet = mutableSetOf<String>()
				newAdjSet.add(nextLemma)
				lemmaToAdjacents.put(lemma, newAdjSet)
			}

			// last lemma needs an empty (leaf) set
			if (i == sizeRawLemmaList - 2) {
				if (!lemmaToAdjacents.containsKey(nextLemma))
					lemmaToAdjacents.put(nextLemma, mutableSetOf<String>())
			}

		}//for
	}

	fun isNgramOfLength(lemmasExamined: List<String>, position: Int): Int {

		assert(lemmasExamined.size > 0)
		assert(position < (lemmasExamined.size) && position >= 0)

		var c = 0
		var currentLemma = lemmasExamined.get(position)
		for (index in position until lemmasExamined.size) {
			val adjSet = lemmaToAdjacents.get(currentLemma)
			// UNKNOWN lemma to the MODEL
			if (adjSet == null) {
				break
				// KNOWN lemma to the MODEL
			} else {
				// leaf lemma of the MODEL edge case
				if (adjSet.size == 0) {
					c++;
					break
				}
				// NOT last index of lemmasExamined
				if (index < lemmasExamined.size - 1) {
					if (adjSet.contains(lemmasExamined.get(index + 1))) {
						assert(lemmaToAdjacents.containsKey(lemmasExamined.get(index + 1)))
						currentLemma = lemmasExamined.get(index + 1)
						c++
					} else {
						c++
						break
					}
					// LAST index of lemmasExamined
				} else {
					c++
				}
			}
		}
		return c
	}

	fun printDataStructures() {
		printStats()
		println("\n________________________________________________________________________")
		println("$TAG : PRINT DATASTRUCTURES")
		println("_______________________________________")
		println("rawLemmaList: $rawLemmaList")
		println("_______________________________________")
		println("lemmaToAdjacents: $lemmaToAdjacents")
	}

	private fun printStats() {
		println("\n________________________________________________________________________")
		println("$TAG : PRINT STATS")
		println("_______________________________________")
		println("rawLemmaList size: ${rawLemmaList.size}")
		println("_______________________________________")
		println("lemmaToAdjacents size: ${lemmaToAdjacents.size}")
	}
}

class Ngram : NgramProvider {

	val name: String
		get() = this.toString() + " bound to: $field -$length-GRAM"
	val length: Int
	val graph: NgramGraph
	val rawLemmaList: List<String>
	val nGramsToFrequencies = mutableMapOf<List<String>, Int>()
	val popularityTuplesSorted = mutableListOf<PopularityTuple>()
	val howManyNotUniqueNgramsWillBe: Int
	val TAG: String
	
	constructor(
			graph: NgramGraph,
			name: String,
			length: Int
	) {
		this.graph = graph
		this.name = name
		TAG = "${this.toString()}: $name"
		this.length = length
		rawLemmaList = graph.rawLemmaList
		howManyNotUniqueNgramsWillBe = rawLemmaList.size - length + 1
		if (howManyNotUniqueNgramsWillBe < 1) fail("howManyNgramsWillBe? $howManyNotUniqueNgramsWillBe")
		buildNgramBagOfPopularityTuples(rawLemmaList, length, this::chopper)
	}

	// syntax exercise...
	private fun chopper(lemmas: List<String>, position: Int, lenght: Int): List<String> = lemmas.subList(position, position + lenght)

	private fun buildNgramBagOfPopularityTuples(
			lemmas: List<String>,
			size: Int,
			chop: (List<String>, Int, Int) -> List<String>) {
		for (i in 0 until howManyNotUniqueNgramsWillBe) {
			val nGram = chop(lemmas, i, size)
			// I have seen this nGram
			if(nGramsToFrequencies.containsKey(nGram)){
				var seenSoFar = nGramsToFrequencies.get(nGram)
				if(seenSoFar != null) seenSoFar++ else fail("unexpected null seenSoFar")
				nGramsToFrequencies.put(nGram, seenSoFar)
			// I have not seen this nGram so far	
			} else {
				nGramsToFrequencies.put(nGram, 1)
			}
		}
		// convert map of nGrams -> their frequencies to sortable list of popularity tuples
		nGramsToFrequencies.keys.forEach{
			popularityTuplesSorted.add(PopularityTuple(it, nGramsToFrequencies.get(it)?.toInt() ?: -1))
		}
		// check for unexpected -1 frequency
		popularityTuplesSorted.forEach{
			if(it.frequency == -1)fail("${it.toString()} unexpected -1 meaning null")
		}
		popularityTuplesSorted.sort()
	}

	//NgramProvider
	override fun getDescription(): String  = name
	override fun getPopularityTuplesSize(): Int = popularityTuplesSorted.size
	override fun getNgramSize(): Int = this.length
	override fun getPopularityHeadOfSize(topRank: Int): List<PopularityTuple> = popularityTuplesSorted.subList(0, topRank)
	override fun printDataStructures (howMany: Int) {
		println("\n________________________________________________________________________")
		println("\n________________________________________________________________________")
		println("$TAG : PRINT DATASTRUCTURES")
		println("\n________________________________________________________________________")
		printBasicStats()
		println("\n________________________________________________________________________")
		println("\n\nPOPULARITY_TUPLES_SORTED:")
		val howManyClipped = if (howMany < popularityTuplesSorted.size) howMany else popularityTuplesSorted.size 
		popularityTuplesSorted.subList(0, howManyClipped).forEach{
			println(it)
		}
		println("\n\nNGRAMS_TO_FREQUENCIES:")
		var c = 0
		nGramsToFrequencies.entries.forEach{
			if(c < howManyClipped){
				println(it)
				c++
			} else {
				return@forEach
			}
		}
	}
	
	fun printBasicStats(){
		println("BASIC STATS:")
		println("notUniqueNgrams: $howManyNotUniqueNgramsWillBe")
		println("uniqueNgrams: ${nGramsToFrequencies.entries.size}")
	}
	//
}
