package app

import model.learn.read.ModelDataReader
import model.learn.LemmaDataPerClass
import model.learn.NgramGraph
import model.learn.Ngram
import model.learn.NgramProvider
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

const val HOW_MANY_POPULARITY_TUPLES_INTERESTED = 50

fun main(args: Array<String>) {
	val app = App()
	app.runApp()
}

class App {

	private var modelList = mutableListOf<Model>()

	fun runApp() {
		println("$TAG : speech sentiment classifier")

		testEyeBallNgramTree()
		testIsNgram0()
		testIsNgram1()
		testIsNgram2()
		testIsNgram3()
		testIsNgram4()
		testIsNgramEdgeCases()
		testBagOfNgramsEyeBall()

		//fail("halt")

		var startTime = System.currentTimeMillis()
		println("\n$TAG : Start build models")
		buildModels()
		println("\n$TAG : Time taken build models: " + (System.currentTimeMillis() - startTime))

		startTime = System.currentTimeMillis()
		println("\n$TAG : Start lemmatize input")
		lemmatizeInput()
		println("\n$TAG : Time taken lemmatize input: " + (System.currentTimeMillis() - startTime))

		val readerLemmasInputLemmatized = ModelDataReader(inputDir, true)
		val lemmatizedInput = readerLemmasInputLemmatized.readFile(File(inputDir + PREFIX + inputFileLemmatized))
		if (lemmatizedInput.size == 0) fail("lemmatizedInput.size == 0")

		//testPrintNgrams(lemmatizedInput, modelList.get(0))
		//testPrintNgrams(lemmatizedInput, modelList.get(1))

	}

	private fun buildModels() {
		if (modelRoots.size != modelNames.size) fail("modelRoots.size != modelNames.size")
		for (i in modelRoots.indices) {
			val dataReader = ModelDataReader(modelRoots[i], false)
			val lemmaList = dataReader.getLemmatizedList()
			val lemmaDataPerClass = LemmaDataPerClass(modelNames[i], lemmaList)
			val nGram = NgramGraph(lemmaList)
			val nGramOf2 = Ngram(nGram,"2-GRAM",2)
			val nGramOf3 = Ngram(nGram,"3-GRAM",3)
			val nGrams = listOf<NgramProvider>(nGramOf2,nGramOf3)

			print("$TAG INSTANTIATING MODEL: ${modelNames[i]}")
			val wrapper = Model(
					lemmaList,
					lemmaDataPerClass,
					nGrams,
					nGram,
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
