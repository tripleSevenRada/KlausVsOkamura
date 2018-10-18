package model

import model.learn.LemmaDataPerClass
import model.learn.NgramGraph

class Model(
		modelList: List<String>,
		modelStats: LemmaDataPerClass,
		modelNgram: NgramGraph,
		modelName: String) {

	public val listOfLemmas = modelList
		get() = field
	public val lemmaFrequenciesPerClass = modelStats
		get() = field
	public val nGramGraph = modelNgram
		get() = field
	public val modelName = modelName
		get() = field

	override fun toString(): String {
		return modelName
	}

}