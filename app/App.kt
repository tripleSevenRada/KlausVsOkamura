package app

import model.learn.read.ModelDataReader
import model.learn.LemmaDataPerClass
import model.learn.NgramGraph
import model.learn.Ngram
import model.learn.NgramProvider
import model.learn.PrunePunctuation
import input.lemmatize.*
import model.Model
import java.io.File
import test.*
//
private val rootK = "/home/radim/eclipse-workspace-kotlin/KlausVsOkamura/modelDataLemmatized/ClassK"
private val modelKName = "modelK"
private val rootO = "/home/radim/eclipse-workspace-kotlin/KlausVsOkamura/modelDataLemmatized/ClassO"
private val modelOName = "modelO"

private val modelRoots = arrayOf(rootK, rootO)
private val modelNames = arrayOf(modelKName, modelOName)

private val inputDir = "/home/radim/eclipse-workspace-kotlin/KlausVsOkamura/input/"
private val inputFileNonLemmatized = "inputNonLemmatized.txt"
private val inputFileLemmatized = "inputLemmatized.txt"
private val TAG = "APP"

const val HOW_MANY_OUTPUT_ITEMS = 20

fun main(args: Array<String>) {
	val app = App()
	app.runApp()
}

class App {

	private var modelList = mutableListOf<Model>()

	fun runApp() {
		println("$TAG : Speech sentiment classifier")

		/*
		testEyeBallNgramTree()
		testIsNgram0()
		testIsNgram1()
		testIsNgram2()
		testIsNgram3()
		testIsNgram4()
		testIsNgramEdgeCases()
		testBagOfNgramsEyeBall()
		*/
			
		//fail("halt")

		var startTime = System.currentTimeMillis()
		println("\n$TAG : Start build models")
		buildModels()
		println("\n$TAG : Time taken build models: " + (System.currentTimeMillis() - startTime))

		startTime = System.currentTimeMillis()
		println("\n$TAG : Start lemmatize input")
		lemmatizeInput()
		println("\n$TAG : Time taken lemmatize input: " + (System.currentTimeMillis() - startTime))

		val prunePunct = PrunePunctuation()
		
		val readerLemmasInputLemmatized = ModelDataReader(inputDir, true)
		val lemmatizedInput = prunePunct.prune(readerLemmasInputLemmatized.readFile(File(inputDir + PREFIX + inputFileLemmatized)))
		if (lemmatizedInput.size == 0) fail("lemmatizedInput.size == 0")
		
	}

	private fun buildModels() {
		if (modelRoots.size != modelNames.size) fail("modelRoots.size != modelNames.size")
		val prunePunct = PrunePunctuation()
		for (i in modelRoots.indices) {
			println("\n\n===========================================================================================")
			println("BUILDING: ${modelNames[i]}")
			println("===========================================================================================")
			val dataReader = ModelDataReader(modelRoots[i], false)
			val lemmaList = prunePunct.prune(dataReader.getLemmatizedList())
			val lemmaDataPerClass_nGramOf1 = LemmaDataPerClass(modelNames[i], lemmaList)// NGramProvider
			val nGramGraph = NgramGraph(lemmaList)
			val nGramOf2 = Ngram(nGramGraph,"2-GRAM",2)// NGramProvider
			val nGramOf3 = Ngram(nGramGraph,"3-GRAM",3)// NGramProvider
			val nGrams = listOf<NgramProvider>(nGramOf2,nGramOf3)

			lemmaDataPerClass_nGramOf1.printDataStructures(HOW_MANY_OUTPUT_ITEMS)
			//nGramGraph.printDataStructures() // prints all!
			nGrams.forEach{it.printDataStructures(HOW_MANY_OUTPUT_ITEMS)}
			
			print("\n\n$TAG INSTANTIATING MODEL: ${modelNames[i]}")
			val wrapper = Model(
					lemmaList,
					lemmaDataPerClass_nGramOf1,
					nGrams,
					nGramGraph,
					modelNames[i]
			)
			modelList.add(wrapper)
		}
	}

	private fun lemmatizeInput() {
		val input: Lemmatizer = Lemmatizer(inputDir, inputFileNonLemmatized, inputFileLemmatized)
		input.lemmatize()
	}
}

fun fail(message: String): Nothing {
	throw Exception(message)
	//System.err.println(message)
}
