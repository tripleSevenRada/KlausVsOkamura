package app

import model.learn.read.ModelDataReader
import model.learn.LemmaDataPerClass
import model.learn.NgramGraph
import model.learn.Ngram
import model.learn.NgramProvider
import model.learn.PrunePunctuation
import input.lemmatize.*
import model.Model
import java.io.*
import test.*
import java.io.FileInputStream
import java.util.Properties

private val TAG = "APP"

fun main(args: Array<String>) {

	// https://chercher.tech/kotlin/properties-file-kotlin

    val mapProp = mutableMapOf<String, String>()
	var fis: FileInputStream? = null
	var prop: Properties
	try{
		fis = FileInputStream("properties.properties")
		prop = Properties()
		prop.load(fis)
	}catch(e: IOException){
		fail(e.getLocalizedMessage())
	}finally{
		if(fis != null)fis.close()
	}

    val enumKeys = prop.keys()
    while (enumKeys.hasMoreElements()) {
        val key = enumKeys.nextElement() as String
        val value = prop.getProperty(key) as String
        mapProp[key] = value
    }
	
	println("$TAG : Speech sentiment classifier")
    println("Properties : $mapProp")
	
	val publishedMapProp: Map<String,String> = mapProp
	
	val app = App(publishedMapProp)
	app.runApp()
}

class App{

	private val rootK = "modelDataLemmatized/ClassK"
	private val modelKName = "modelK"
	private val rootO = "modelDataLemmatized/ClassO"
	private val modelOName = "modelO"

	private val modelRoots = arrayOf(rootK, rootO)
	private val modelNames = arrayOf(modelKName, modelOName)

	private val inputDir = "/home/radim/eclipse-workspace-kotlin/KlausVsOkamura/input/"
	private val inputFileNonLemmatized = "inputNonLemmatized.txt"
	private val inputFileLemmatized = "inputLemmatized.txt"
	
	private val howManyOutputItems: Int
	private val howManyOutputItemsDefault = 12
	
	private val properties: Map<String,String>
	
	constructor(mapProp: Map<String, String>){
		this.properties = mapProp
		val howMany: String? = mapProp.get("howManyOutputItems")
		howManyOutputItems = if(howMany != null) {
				val howManyOrNull: Int? = howMany.toIntOrNull()
				if(howManyOrNull != null) howManyOrNull else howManyOutputItemsDefault
			} else {
				howManyOutputItemsDefault
			}
	}
	
	private var modelList = mutableListOf<Model>()

	fun runApp() {
		
		
 		test.howManyOutputItems = this.howManyOutputItems
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
		println("\n$TAG : Start lemmatize input")
		lemmatizeInput()
		println("\n$TAG : Time taken lemmatize input: " + (System.currentTimeMillis() - startTime))
		
		startTime = System.currentTimeMillis()
		println("\n$TAG : Start build models")
		buildModels()
		println("\n$TAG : Time taken build models: " + (System.currentTimeMillis() - startTime))

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

			lemmaDataPerClass_nGramOf1.printDataStructures(howManyOutputItems)
			//nGramGraph.printDataStructures() // prints all!
			nGrams.forEach{it.printDataStructures(howManyOutputItems)}
			
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
