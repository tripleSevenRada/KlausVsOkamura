package test

import model.learn.read.ModelDataReader
import model.learn.LemmaDataPerClass
import model.learn.Ngram
import model.learn.NgramGraph
import model.learn.NgramProvider
import model.Model
import app.fail

private val rootDir = "/home/radim/eclipse-workspace-kotlin/KlausVsOkamura/modelDataLemmatized/TestNgram/"
private val rootTestUnique = rootDir + "TestNgramAllUnique"
private val rootTestSame = rootDir + "TestNgramSame"
private val rootTestPair = rootDir + "TestNgramPair"
private val rootTest1 = rootDir + "TestNgram"

private val rootTestIsNgramOfLength0 = rootDir + "TestIsNgram0"
private val rootTestIsNgramOfLength1 = rootDir + "TestIsNgram1"
private val rootTestIsNgramOfLength2 = rootDir + "TestIsNgram2"
private val rootTestBagOfNgrams = rootDir + "TestBagOfNgramsEyeball"

private val TAG = "Tests.kt"

var howManyOutputItems = 12
	set(howMany: Int){
		field = howMany
	} 

fun getModelVerbose(dir: String, print: Boolean): Model {
	val model = getModel(dir, print)
	println("$TAG : lemmas " + model.listOfLemmas)
	val lemmaFrequencies = model.lemmaFrequenciesPerClass
	lemmaFrequencies.printDataStructures(howManyOutputItems)
	val nGramGraph = model.nGramGraph
	nGramGraph.printDataStructures()
	val nGrams = model.nGrams
	nGrams.forEach{
		it.printDataStructures(howManyOutputItems)
	}
	return model
}

// MODEL FACTORY
fun getModel(dir: String, print: Boolean): Model {
	val model: ModelDataReader = ModelDataReader(dir, print)
	val modelLemmaList: List<String> = model.getLemmatizedList()
	val modelName = "TestsNgram-modelFactory--$dir"
	val uniGram = LemmaDataPerClass(modelName, modelLemmaList)
	val nGramGraph = NgramGraph(modelLemmaList)
	val nGramOf2 = Ngram(nGramGraph,"2-GRAM",2)
	val nGramOf3 = Ngram(nGramGraph,"3-GRAM",3)
	val nGrams = listOf<NgramProvider>(nGramOf2,nGramOf3)
	return Model(modelLemmaList, uniGram, nGrams, nGramGraph, modelName)
}

fun testEyeBallNgramTree() {

	val dirs = arrayOf(rootTestUnique, rootTestSame, rootTestPair, rootTestIsNgramOfLength0, rootTest1)

	println("$TAG EYEBALL TESTS")

	dirs.forEach {
		println("\n------------------------------------------------------------------------------------------\n$it")
		getModelVerbose(it, true)
	}
}

fun testIsNgram0() {
	val model = getModelVerbose(rootTestIsNgramOfLength0, true)
	val lemmasToExamineList = mutableListOf<String>()
	val isNgramOfLength = mutableListOf<Int>()
	lemmasToExamineList.add("musime");
	isNgramOfLength.add(9)
	lemmasToExamineList.add("zlepsit");
	isNgramOfLength.add(8)
	lemmasToExamineList.add("komunikaci");
	isNgramOfLength.add(7)
	lemmasToExamineList.add("musime");
	isNgramOfLength.add(6)
	lemmasToExamineList.add("zlepsit");
	isNgramOfLength.add(5)
	lemmasToExamineList.add("interakci");
	isNgramOfLength.add(4)
	lemmasToExamineList.add("zlepsit");
	isNgramOfLength.add(3)
	lemmasToExamineList.add("vse");
	isNgramOfLength.add(2)
	lemmasToExamineList.add("pratele");
	isNgramOfLength.add(1)
	for (i in 0 until lemmasToExamineList.size) {
		println("$i  --  ${lemmasToExamineList.get(i)}")
		val nGram = model.nGramGraph.isNgramOfLength(lemmasToExamineList, i)
		println("is nGram: $nGram")
		if (isNgramOfLength.get(i) != nGram) fail("expected: " + isNgramOfLength.get(i) + " received: " + nGram)
	}
}

fun testIsNgram1() {
	val model = getModelVerbose(rootTestIsNgramOfLength1, true)
	val lemmasToExamineList = mutableListOf<String>()
	val isNgramOfLength = mutableListOf<Int>()
	lemmasToExamineList.add("musime");
	isNgramOfLength.add(4)
	lemmasToExamineList.add("se");
	isNgramOfLength.add(3)
	lemmasToExamineList.add("zlepsit");
	isNgramOfLength.add(2)
	lemmasToExamineList.add("v");
	isNgramOfLength.add(1)
	lemmasToExamineList.add("proste");
	isNgramOfLength.add(1)
	lemmasToExamineList.add("nejni tam");
	isNgramOfLength.add(0)
	lemmasToExamineList.add("ve");
	isNgramOfLength.add(1)
	lemmasToExamineList.add("komunikaci");
	isNgramOfLength.add(2)
	lemmasToExamineList.add("musime");
	isNgramOfLength.add(1)

	for (i in 0 until lemmasToExamineList.size) {
		println("$i  --  ${lemmasToExamineList.get(i)}")
		val nGram = model.nGramGraph.isNgramOfLength(lemmasToExamineList, i)
		println("is nGram: $nGram")
		if (isNgramOfLength.get(i) != nGram) fail("expected: " + isNgramOfLength.get(i) + " received: " + nGram)
	}
}

fun testIsNgram2() {
	val model = getModelVerbose(rootTestIsNgramOfLength2, true)
	val lemmasToExamineList = mutableListOf<String>()
	val isNgramOfLength = mutableListOf<Int>()
	lemmasToExamineList.add("musime");
	isNgramOfLength.add(4)
	lemmasToExamineList.add("se");
	isNgramOfLength.add(3)
	lemmasToExamineList.add("zlepsit");
	isNgramOfLength.add(2)
	lemmasToExamineList.add("v");
	isNgramOfLength.add(1)
	lemmasToExamineList.add("proste");
	isNgramOfLength.add(1)
	lemmasToExamineList.add("nejni tam");
	isNgramOfLength.add(0)
	lemmasToExamineList.add("ve");
	isNgramOfLength.add(1)
	lemmasToExamineList.add("komunikaci");
	isNgramOfLength.add(1)
	lemmasToExamineList.add("niente");
	isNgramOfLength.add(0)

	for (i in 0 until lemmasToExamineList.size) {
		println("$i  --  ${lemmasToExamineList.get(i)}")
		val nGram = model.nGramGraph.isNgramOfLength(lemmasToExamineList, i)
		println("is nGram: $nGram")
		if (isNgramOfLength.get(i) != nGram) fail("expected: " + isNgramOfLength.get(i) + " received: " + nGram)
	}
}

fun testIsNgram3() {
	val model = getModelVerbose(rootTestIsNgramOfLength2, true)
	val lemmasToExamineList = mutableListOf<String>()
	val isNgramOfLength = mutableListOf<Int>()
	lemmasToExamineList.add("proste");
	isNgramOfLength.add(3)
	lemmasToExamineList.add("ve");
	isNgramOfLength.add(2)
	lemmasToExamineList.add("vsem");
	isNgramOfLength.add(1)
	for (i in 0 until lemmasToExamineList.size) {
		println("$i  --  ${lemmasToExamineList.get(i)}")
		val nGram = model.nGramGraph.isNgramOfLength(lemmasToExamineList, i)
		println("is nGram: $nGram")
		if (isNgramOfLength.get(i) != nGram) fail("expected: " + isNgramOfLength.get(i) + " received: " + nGram)
	}
}

fun testIsNgram4() {
	val model = getModelVerbose(rootTestIsNgramOfLength2, true)
	val lemmasToExamineList = mutableListOf<String>()
	val isNgramOfLength = mutableListOf<Int>()
	lemmasToExamineList.add("musime");
	isNgramOfLength.add(13)
	lemmasToExamineList.add("se");
	isNgramOfLength.add(12)
	lemmasToExamineList.add("zlepsit");
	isNgramOfLength.add(11)
	lemmasToExamineList.add("v");
	isNgramOfLength.add(10)
	lemmasToExamineList.add("komunikaci");
	isNgramOfLength.add(9)
	lemmasToExamineList.add("musime");
	isNgramOfLength.add(8)
	lemmasToExamineList.add("se");
	isNgramOfLength.add(7)
	lemmasToExamineList.add("zlepsit");
	isNgramOfLength.add(6)
	lemmasToExamineList.add("v");
	isNgramOfLength.add(5)
	lemmasToExamineList.add("organizaci");
	isNgramOfLength.add(4)
	lemmasToExamineList.add("proste");
	isNgramOfLength.add(3)
	lemmasToExamineList.add("ve");
	isNgramOfLength.add(2)
	lemmasToExamineList.add("vsem");
	isNgramOfLength.add(1)
	for (i in 0 until lemmasToExamineList.size) {
		println("$i  --  ${lemmasToExamineList.get(i)}")
		val nGram = model.nGramGraph.isNgramOfLength(lemmasToExamineList, i)
		println("is nGram: $nGram")
		if (isNgramOfLength.get(i) != nGram) fail("expected: " + isNgramOfLength.get(i) + " received: " + nGram)
	}
	lemmasToExamineList.reverse()
	for (i in 0 until lemmasToExamineList.size) {
		println("$i  --  ${lemmasToExamineList.get(i)}")
		val nGram = model.nGramGraph.isNgramOfLength(lemmasToExamineList, i)
		println("is nGram: $nGram")
		if (1 != nGram) fail("expected: " + 1 + " received: " + nGram)
	}
}

fun testIsNgramEdgeCases() {
	// "musime", "zlepsit", "komunikaci", "musime", "zlepsit", "interakci", "zlepsit", "vse", "pratele" 
	val model = getModelVerbose(rootTestIsNgramOfLength0, true)
	val listOfListsOfLemmasToExamine = mutableListOf<List<String>>()
	val isNgramOfLength = mutableListOf<List<Int>>()

	val edgeALemm = listOf<String>("nenitam")
	val edgeANgram = listOf<Int>(0)
	listOfListsOfLemmasToExamine.add(edgeALemm)
	isNgramOfLength.add(edgeANgram)

	val edgeBLemm = listOf<String>("nenitam", "musime")
	val edgeBNgram = listOf<Int>(0, 1)
	listOfListsOfLemmasToExamine.add(edgeBLemm)
	isNgramOfLength.add(edgeBNgram)

	val edgeCLemm = listOf<String>("nenitam", "interakci", "")
	val edgeCNgram = listOf<Int>(0, 1, 0)
	listOfListsOfLemmasToExamine.add(edgeCLemm)
	isNgramOfLength.add(edgeCNgram)

	// "musime", "zlepsit", "komunikaci", "musime", "zlepsit", "interakci", "zlepsit", "vse", "pratele"

	val edgeDLemm = listOf<String>("nenitam", "komunikaci", "niente")
	val edgeDNgram = listOf<Int>(0, 1, 0)
	listOfListsOfLemmasToExamine.add(edgeDLemm)
	isNgramOfLength.add(edgeDNgram)

	val edgeELemm = listOf<String>("komunikaci", "zlepsit", "musime")
	val edgeENgram = listOf<Int>(1, 1, 1)
	listOfListsOfLemmasToExamine.add(edgeELemm)
	isNgramOfLength.add(edgeENgram)

	val edgeFLemm = listOf<String>("musime", "zlepsit", "komunikaci")
	val edgeFNgram = listOf<Int>(3, 2, 1)
	listOfListsOfLemmasToExamine.add(edgeFLemm)
	isNgramOfLength.add(edgeFNgram)

	val edgeGLemm = listOf<String>("zlepsit", "vse", "pratele")
	val edgeGNgram = listOf<Int>(3, 2, 1)
	listOfListsOfLemmasToExamine.add(edgeGLemm)
	isNgramOfLength.add(edgeGNgram)

	val edgeHLemm = listOf<String>("pratele", "vse", "zlepsit")
	val edgeHNgram = listOf<Int>(1, 1, 1)
	listOfListsOfLemmasToExamine.add(edgeHLemm)
	isNgramOfLength.add(edgeHNgram)

	// "musime", "zlepsit", "komunikaci", "musime", "zlepsit", "interakci", "zlepsit", "vse", "pratele"

	val edgeILemm = listOf<String>("pratele")
	val edgeINgram = listOf<Int>(1)
	listOfListsOfLemmasToExamine.add(edgeILemm)
	isNgramOfLength.add(edgeINgram)

	val edgeJLemm = listOf<String>("vse", "pratele")
	val edgeJNgram = listOf<Int>(2, 1)
	listOfListsOfLemmasToExamine.add(edgeJLemm)
	isNgramOfLength.add(edgeJNgram)


	val edge0Lemm = listOf<String>("vubectamneni", "faktne")
	val edge0Ngram = listOf<Int>(0, 0)
	listOfListsOfLemmasToExamine.add(edge0Lemm)
	isNgramOfLength.add(edge0Ngram)

	val edge1Lemm = listOf<String>("")
	val edge1Ngram = listOf<Int>(0)
	listOfListsOfLemmasToExamine.add(edge1Lemm)
	isNgramOfLength.add(edge1Ngram)

	// "musime", "zlepsit", "komunikaci", "musime", "zlepsit", "interakci", "zlepsit", "vse", "pratele"

	val edge2Lemm = listOf<String>("zlepsit")
	val edge2Ngram = listOf<Int>(1)
	listOfListsOfLemmasToExamine.add(edge2Lemm)
	isNgramOfLength.add(edge2Ngram)

	val edge3Lemm = listOf<String>("musime", "zlepsit", "komunikaci", "musime", "zlepsit", "interakci", "zlepsit", "vse", "pratele")
	val edge3Ngram = listOf<Int>(9, 8, 7, 6, 5, 4, 3, 2, 1)
	listOfListsOfLemmasToExamine.add(edge3Lemm)
	isNgramOfLength.add(edge3Ngram)

	val edge31Lemm = listOf<String>("musime", "zlepsit", "komunikaci", "", "musime", "zlepsit", "interakci", "zlepsit", "vse", "pratele")
	val edge31Ngram = listOf<Int>(3, 2, 1, 0, 6, 5, 4, 3, 2, 1)
	listOfListsOfLemmasToExamine.add(edge31Lemm)
	isNgramOfLength.add(edge31Ngram)

	val edge4Lemm = listOf<String>("komunikaci", "musime", "musime", "zlepsit", "zlepsit", "interakci", "zlepsit", "vse", "pratele")
	val edge4Ngram = listOf<Int>(2, 1, 2, 1, 5, 4, 3, 2, 1)
	listOfListsOfLemmasToExamine.add(edge4Lemm)
	isNgramOfLength.add(edge4Ngram)

	val edge5Lemm = listOf<String>("pratele")
	val edge5Ngram = listOf<Int>(1)
	listOfListsOfLemmasToExamine.add(edge5Lemm)
	isNgramOfLength.add(edge5Ngram)

	val edge6Lemm = listOf<String>("pratele", "vse", "zlepsit")
	val edge6Ngram = listOf<Int>(1, 1, 1)
	listOfListsOfLemmasToExamine.add(edge6Lemm)
	isNgramOfLength.add(edge6Ngram)

	val edge7Lemm = listOf<String>("komunikaci", "interakci", "zlepsit", "vse")
	val edge7Ngram = listOf<Int>(1, 3, 2, 1)
	listOfListsOfLemmasToExamine.add(edge7Lemm)
	isNgramOfLength.add(edge7Ngram)

	val edge8Lemm = listOf<String>("pratele", "")
	val edge8Ngram = listOf<Int>(1, 0)
	listOfListsOfLemmasToExamine.add(edge8Lemm)
	isNgramOfLength.add(edge8Ngram)

	// "musime", "zlepsit", "komunikaci", "musime", "zlepsit", "interakci", "zlepsit", "vse", "pratele"

	for (i in 0 until listOfListsOfLemmasToExamine.size) {
		println("\n\nEDGE CASE TEST===============================================================")
		val currList = listOfListsOfLemmasToExamine.get(i)
		val currisNgramOfLength = isNgramOfLength.get(i)
		assert(currList.size == currisNgramOfLength.size)
		println("$model\nanalyzing list of: $currList\nexpecting: $currisNgramOfLength")
		for (j in 0 until currList.size) {
			val expected = currisNgramOfLength.get(j)
			val got = model.nGramGraph.isNgramOfLength(currList, j)
			println("${currList.get(j)} : expected $expected got $got")
			if (expected != got) fail("see why")
		}

	}
}

fun testPrintNgrams(lemmas: List<String>, wrapper: Model) {
	println("\n\n------------------nGramsGraph, model = ${wrapper.modelName}\n")
	var acu = 0
	for (i in lemmas.indices) {
		val nGramNow = wrapper.nGramGraph.isNgramOfLength(lemmas, i)
		//println("${lemmas.get(i)}: is nGram of: $nGramNow")
		acu += nGramNow
	}
	println("acu: $acu")
}

// TODO
fun testBagOfNgramsEyeBall(){
	// "musime", "zlepsit", "komunikaci", "musime", "zlepsit", "interakci", "zlepsit", "vse", "pratele" 
	val model = getModelVerbose(rootTestBagOfNgrams, true)
	println("\n\n------------------testBagOfNgramsEyeBall, model = ${model.modelName}\n")
	println("full lemma list: ${model.listOfLemmas}")
	model.nGrams.forEach{
		println("------------------------------------------------------------------------------------------")
		it.printDataStructures(howManyOutputItems)
	}
}





