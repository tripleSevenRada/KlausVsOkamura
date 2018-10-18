package model

import model.learn.NgramProvider
import model.learn.NgramGraph

class Model(
		modelList: List<String>,
		modelUnigram: NgramProvider,
		modelNgrams: List<NgramProvider>,
		modelNgramGraph: NgramGraph,
		modelName: String) {

	public val listOfLemmas = modelList
		get() = field
	public val lemmaFrequenciesPerClass = modelUnigram
		get() = field
	public val nGramGraph = modelNgramGraph
		get() = field
	public val nGrams = modelNgrams
		get() = field
	public val modelName = modelName
		get() = field

	override fun toString(): String {
		return modelName
	}

}