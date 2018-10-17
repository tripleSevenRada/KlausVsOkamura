package model

import model.learn.LemmaDataPerClass
import model.learn.NgramGraph

class ModelWrapper(
		modelList: List<String>,
		modelStats: LemmaDataPerClass,
		modelNgram: NgramGraph,
		modelName: String){
	
		public val listOfLemmas = modelList
			get() = field //default anyway
		public val lemmaFrequenciesPerClass = modelStats
			get() = field
		public val nGramGraph = modelNgram
			get() = field
		public val modelName = modelName
			get() = field
	
	override fun toString():String{
		return modelName
	}
	
}