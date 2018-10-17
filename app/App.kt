package app
import model.learn.read.ModelDataReader
import model.learn.LemmaDataPerClass
import model.learn.NgramGraph
import input.lemmatize.*
import model.ModelWrapper
import java.io.File
import test.*

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

fun main(args:Array<String>){
	val app = App()
	app.runApp()
}

class App{
	
	private var modelList = mutableListOf<ModelWrapper>()
	
	fun runApp(){
		println("$TAG : speech sentiment classifier")

		
		testEyeBallNgramTree()
		testIsNgram0()
		testIsNgram1()
		testIsNgram2()
		testIsNgram3()
		testIsNgram4()
		testIsNgramEdgeCases()
		

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
		if(lemmatizedInput.size == 0)fail("lemmatizedInput.size == 0")
		
		//testPrintNgrams(lemmatizedInput, modelList.get(0))
		//testPrintNgrams(lemmatizedInput, modelList.get(1))
		
	}

	private fun buildModels(){
		if (modelRoots.size != modelNames.size) fail("modelRoots.size != modelNames.size")
		for(i in modelRoots.indices){
			val dataReader = ModelDataReader(modelRoots[i], false)
			val lemmaList = dataReader.getLemmatizedList()
			val stats = LemmaDataPerClass(modelNames[i], lemmaList)
			val nGram = NgramGraph(lemmaList)
			nGram.buildNgramGraph()
			
			print("$TAG INSTANTIATING MODEL: ${modelNames[i]}")
			stats.printStats()
			stats.printSortedByFrequencies(150)
			nGram.printStats()
			val wrapper = ModelWrapper(
					lemmaList,
					stats,
					nGram,
					modelNames[i]
			)
			modelList.add(wrapper)
		}
	}

	private fun lemmatizeInput(){
		val input: Lemmatizer = Lemmatizer(inputDir, inputFileNonLemmatized, inputFileLemmatized)
			input.lemmatize()
	}
}

fun fail(message: String): Nothing{
	throw Exception(message)
	//System.err.println(message)
}
