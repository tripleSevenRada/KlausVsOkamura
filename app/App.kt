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

	private var modelRoots = listOf<String>() // (rootK, rootO ...)
	private var modelNames = listOf<String>() // (modelKName, modelOName ...)

	private val inputDir: String
	private val inputFileNonLemmatized: String
	private val inputFileLemmatized: String
	
	private val howManyOutputItems: Int
	private val howManyOutputItemsDefault = 12
	
	private val properties: Map<String,String>
	
	constructor(mapProp: Map<String, String>){
		this.properties = mapProp
		val howMany: String? = properties.get("howManyOutputItems")
		howManyOutputItems = if(howMany != null) {
				val howManyOrNull: Int? = howMany.toIntOrNull()
				if(howManyOrNull != null) howManyOrNull else howManyOutputItemsDefault
			} else {
				howManyOutputItemsDefault
			}
		try{
			modelRoots = properties.get("modelDataRoots")!!.split(",")
			modelNames = properties.get("modelNames")!!.split(",")
			if(	modelRoots.size != modelNames.size ||
				modelRoots.size == 0	// should never happen
					) fail("modelRoots or modelNames problem")
			modelRoots = modelRoots.map{e -> e.trim()}
			modelNames = modelNames.map{e -> e.trim()}
			
			inputDir = properties.get("inputDir")!!
			inputFileNonLemmatized = properties.get("inputFileNonLemmatized")!!
			inputFileLemmatized = properties.get("inputFileLemmatized")!!
			
		}catch(e: NullPointerException){
			fail(e.getLocalizedMessage())
		}
	}
	
	private var modelList = mutableListOf<Model>()
	private var inputModel: Model? = null

	// TODO code duplicity... 
	fun runApp() {
		
		/**
 		test.howManyOutputItems = this.howManyOutputItems
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
		println("\n$TAG : Start lemmatize input")
		lemmatizeInput()
		println("\n$TAG : Time taken lemmatize input: " + (System.currentTimeMillis() - startTime))
		
		// build model of an input, not a class
		startTime = System.currentTimeMillis()
		println("\n$TAG : Start build input model")
		val inputModelName = "inputModel"
		val prunePunct = PrunePunctuation()
		val readerLemmasInputLemmatized = ModelDataReader(inputDir, true)
		val lemmatizedInput = prunePunct.prune(readerLemmasInputLemmatized.readFile(File(inputDir + PREFIX + inputFileLemmatized)))
		if (lemmatizedInput.size == 0) fail("lemmatizedInput.size == 0")
		val lemmaDataPerClass_nGramOf1 = LemmaDataPerClass(inputModelName, lemmatizedInput)// NGramProvider
		val nGramGraph = NgramGraph(lemmatizedInput)
		val nGramOf2 = Ngram(nGramGraph,"2-GRAM-$inputModelName",2)// NGramProvider
		val nGramOf3 = Ngram(nGramGraph,"3-GRAM-$inputModelName",3)// NGramProvider
		val nGrams = listOf<NgramProvider>(nGramOf2, nGramOf3)

		lemmaDataPerClass_nGramOf1.printDataStructures(howManyOutputItems)
		//nGramGraph.printDataStructures() // prints all!
		nGrams.forEach{it.printDataStructures(howManyOutputItems)}
		
		print("\n\n$TAG INSTANTIATING INPUT MODEL: $inputModelName")
		inputModel = getWrappedModel(
					lemmatizedInput,
					lemmaDataPerClass_nGramOf1,
					nGrams,
					nGramGraph,
					inputModelName
					)
		
		println("\n$TAG : Time taken build input model: " + (System.currentTimeMillis() - startTime))
		//
		
		// build models of classes
		startTime = System.currentTimeMillis()
		println("\n$TAG : Start build class models")
		buildModels()
		println("\n$TAG : Time taken build models: " + (System.currentTimeMillis() - startTime))
		//
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
			val nGramOf2 = Ngram(nGramGraph,"2-GRAM-$modelNames[i]",2)// NGramProvider
			val nGramOf3 = Ngram(nGramGraph,"3-GRAM-$modelNames[i]",3)// NGramProvider
			val nGrams = listOf<NgramProvider>(nGramOf2,nGramOf3)

			lemmaDataPerClass_nGramOf1.printDataStructures(howManyOutputItems)
			//nGramGraph.printDataStructures() // prints all!
			nGrams.forEach{it.printDataStructures(howManyOutputItems)}
			
			print("\n\n$TAG INSTANTIATING CLASS MODEL: ${modelNames[i]}")

			modelList.add(getWrappedModel(
					lemmaList,
					lemmaDataPerClass_nGramOf1,
					nGrams,
					nGramGraph,
					modelNames[i]
			))
		}
	}
	
	private fun getWrappedModel(
					lemmaList: List<String>,
					nGramOf1: LemmaDataPerClass,
					nGrams: List<NgramProvider>,
					nGramGraph: NgramGraph,
					modelName: String
			): Model{
		return Model(lemmaList, nGramOf1, nGrams, nGramGraph, modelName)
	}

	private fun lemmatizeInput() {
		val input: Lemmatizer = Lemmatizer(properties)
		input.lemmatize()
	}
}

fun fail(message: String): Nothing {
	throw Exception(message)
	//System.err.println(message)
}
