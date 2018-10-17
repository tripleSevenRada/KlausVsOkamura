package test

import model.learn.read.ModelDataReader
import model.learn.LemmaDataPerClass
import model.learn.NgramGraph
import model.ModelWrapper
import app.*

private val rootDir = "/home/radim/eclipse-workspace-kotlin/KlausVsOkamura/modelDataLemmatized/TestNgram/"
private val rootTestUnique = rootDir + "TestNgramAllUnique"
private val rootTestSame = rootDir + "TestNgramSame"
private val rootTestPair = rootDir + "TestNgramPair"
private val rootTest1 = rootDir + "TestNgram"

private val rootTestIsNgram0 = rootDir + "TestIsNgram0"
private val rootTestIsNgram1 = rootDir + "TestIsNgram1"
private val rootTestIsNgram2 = rootDir + "TestIsNgram2"
private val HOW_MANY_PRINT = 10

private val TAG = "Tests.kt"

fun getModelVerbose(dir: String, print: Boolean): ModelWrapper{
	val model = getModel(dir, print)
	println("$TAG : " + model.listOfLemmas)
	val stats = model.lemmaFrequenciesPerClass
	printAllStats(stats, HOW_MANY_PRINT)
	val nGramGraph = model.nGramGraph
	printNgramGraph(nGramGraph)
	return model
}
fun printAllStats(stats: LemmaDataPerClass, howManySorted: Int){
	stats.printDataStructures(howManySorted)
}
fun printNgramGraph(graph: NgramGraph){
	graph.printDataStructures()
}
//MODEL FACTORY
fun getModel(dir: String, print: Boolean): ModelWrapper{
	val model: ModelDataReader = ModelDataReader(dir, print)
	val modelTestList: List<String> = model.getLemmatizedList()
	val modelName = "TestsNgram-modelFactory--$dir"
	val stats = LemmaDataPerClass(modelName, modelTestList)
	val nGramGraph = NgramGraph(modelTestList)
	nGramGraph.buildNgramGraph()
	return ModelWrapper(modelTestList, stats, nGramGraph, modelName)
}

fun testEyeBallNgramTree(){
	
	val dirs = arrayOf (rootTestUnique, rootTestSame, rootTestPair, rootTestIsNgram0, rootTest1)
	
	println("$TAG EYEBALL TESTS")
	
	dirs.forEach{
		println("\n------------------------------------------------------------------------------------------\n$it")
		getModelVerbose(it, true)
	}
}
fun testIsNgram0(){
		val model = getModelVerbose(rootTestIsNgram0, true)
		val lemmasToExamineList = mutableListOf<String>()
		val isNgram = mutableListOf<Int>()
		lemmasToExamineList.add("musime");
		isNgram.add(9)
		lemmasToExamineList.add("zlepsit");
		isNgram.add(8)
		lemmasToExamineList.add("komunikaci");
		isNgram.add(7)
		lemmasToExamineList.add("musime");
		isNgram.add(6)
		lemmasToExamineList.add("zlepsit");
		isNgram.add(5)
		lemmasToExamineList.add("interakci");
		isNgram.add(4)
		lemmasToExamineList.add("zlepsit");
		isNgram.add(3)
		lemmasToExamineList.add("vse");
		isNgram.add(2)
		lemmasToExamineList.add("pratele");
		isNgram.add(1)
		for(i in 0 .. lemmasToExamineList.size - 1){
			println("$i  --  ${lemmasToExamineList.get(i)}")
			val nGram = model.nGramGraph.isNgram(lemmasToExamineList, i)
			println("is nGram: $nGram")
			if(isNgram.get(i) != nGram) fail ("expected: " + isNgram.get(i) + " received: " + nGram)
		}
}
fun testIsNgram1(){
		val model = getModelVerbose(rootTestIsNgram1, true)
		val lemmasToExamineList = mutableListOf<String>()
		val isNgram = mutableListOf<Int>()
		lemmasToExamineList.add("musime");
		isNgram.add(4)
		lemmasToExamineList.add("se");
		isNgram.add(3)
		lemmasToExamineList.add("zlepsit");
		isNgram.add(2)
		lemmasToExamineList.add("v");
		isNgram.add(1)
		lemmasToExamineList.add("proste");
		isNgram.add(1)
		lemmasToExamineList.add("nejni tam");
		isNgram.add(0)
		lemmasToExamineList.add("ve");
		isNgram.add(1)
		lemmasToExamineList.add("komunikaci");
		isNgram.add(2)
		lemmasToExamineList.add("musime");
		isNgram.add(1)
	
		for(i in 0 .. lemmasToExamineList.size - 1){
			println("$i  --  ${lemmasToExamineList.get(i)}")
			val nGram = model.nGramGraph.isNgram(lemmasToExamineList, i)
			println("is nGram: $nGram")
			if(isNgram.get(i) != nGram) fail("expected: " + isNgram.get(i) + " received: " + nGram)
		}
}
fun testIsNgram2(){
		val model = getModelVerbose(rootTestIsNgram2, true)
		val lemmasToExamineList = mutableListOf<String>()
		val isNgram = mutableListOf<Int>()
		lemmasToExamineList.add("musime");
		isNgram.add(4)
		lemmasToExamineList.add("se");
		isNgram.add(3)
		lemmasToExamineList.add("zlepsit");
		isNgram.add(2)
		lemmasToExamineList.add("v");
		isNgram.add(1)
		lemmasToExamineList.add("proste");
		isNgram.add(1)
		lemmasToExamineList.add("nejni tam");
		isNgram.add(0)
		lemmasToExamineList.add("ve");
		isNgram.add(1)
		lemmasToExamineList.add("komunikaci");
		isNgram.add(1)
		lemmasToExamineList.add("niente");
		isNgram.add(0)
	
		for(i in 0 .. lemmasToExamineList.size - 1){
			println("$i  --  ${lemmasToExamineList.get(i)}")
			val nGram = model.nGramGraph.isNgram(lemmasToExamineList, i)
			println("is nGram: $nGram")
			if(isNgram.get(i) != nGram) fail("expected: " + isNgram.get(i) + " received: " + nGram)
		}
}
fun testIsNgram3(){
		val model = getModelVerbose(rootTestIsNgram2, true)
		val lemmasToExamineList = mutableListOf<String>()
		val isNgram = mutableListOf<Int>()
		lemmasToExamineList.add("proste");
		isNgram.add(3)
		lemmasToExamineList.add("ve");
		isNgram.add(2)
		lemmasToExamineList.add("vsem");
		isNgram.add(1)
		for(i in 0 .. lemmasToExamineList.size - 1){
			println("$i  --  ${lemmasToExamineList.get(i)}")
			val nGram = model.nGramGraph.isNgram(lemmasToExamineList, i)
			println("is nGram: $nGram")
			if(isNgram.get(i) != nGram) fail("expected: " + isNgram.get(i) + " received: " + nGram)
		}
}
fun testIsNgram4(){
		val model = getModelVerbose(rootTestIsNgram2, true)
		val lemmasToExamineList = mutableListOf<String>()
		val isNgram = mutableListOf<Int>()
		lemmasToExamineList.add("musime");
		isNgram.add(13)
		lemmasToExamineList.add("se");
		isNgram.add(12)
		lemmasToExamineList.add("zlepsit");
		isNgram.add(11)
		lemmasToExamineList.add("v");
		isNgram.add(10)
		lemmasToExamineList.add("komunikaci");
		isNgram.add(9)
		lemmasToExamineList.add("musime");
		isNgram.add(8)
		lemmasToExamineList.add("se");
		isNgram.add(7)
		lemmasToExamineList.add("zlepsit");
		isNgram.add(6)
		lemmasToExamineList.add("v");
		isNgram.add(5)
		lemmasToExamineList.add("organizaci");
		isNgram.add(4)
		lemmasToExamineList.add("proste");
		isNgram.add(3)
		lemmasToExamineList.add("ve");
		isNgram.add(2)
		lemmasToExamineList.add("vsem");
		isNgram.add(1)
		for(i in 0 .. lemmasToExamineList.size - 1){
			println("$i  --  ${lemmasToExamineList.get(i)}")
			val nGram = model.nGramGraph.isNgram(lemmasToExamineList, i)
			println("is nGram: $nGram")
			if(isNgram.get(i) != nGram) fail("expected: " + isNgram.get(i) + " received: " + nGram)
		}
		lemmasToExamineList.reverse()
		for(i in 0 .. lemmasToExamineList.size - 1){
			println("$i  --  ${lemmasToExamineList.get(i)}")
			val nGram = model.nGramGraph.isNgram(lemmasToExamineList, i)
			println("is nGram: $nGram")
			if(1 != nGram) fail("expected: " + 1 + " received: " + nGram)
		}
}
fun testIsNgramEdgeCases(){
	//"musime", "zlepsit", "komunikaci", "musime", "zlepsit", "interakci", "zlepsit", "vse", "pratele" 
		val model = getModelVerbose(rootTestIsNgram0, true)
		val listOfListsOfLemmasToExamine = mutableListOf<List<String>>()
		val isNgram = mutableListOf<List<Int>>()

		val edgeALemm = listOf<String>("nenitam")
		val edgeANgram = listOf<Int>(0)
		listOfListsOfLemmasToExamine.add(edgeALemm)
		isNgram.add(edgeANgram)
	
		val edgeBLemm = listOf<String>("nenitam", "musime")
		val edgeBNgram = listOf<Int>(0,1)
		listOfListsOfLemmasToExamine.add(edgeBLemm)
		isNgram.add(edgeBNgram)
	
		val edgeCLemm = listOf<String>("nenitam", "interakci", "")
		val edgeCNgram = listOf<Int>(0, 1, 0)
		listOfListsOfLemmasToExamine.add(edgeCLemm)
		isNgram.add(edgeCNgram)
	
	//"musime", "zlepsit", "komunikaci", "musime", "zlepsit", "interakci", "zlepsit", "vse", "pratele"
	
		val edgeDLemm = listOf<String>("nenitam","komunikaci", "niente")
		val edgeDNgram = listOf<Int>(0, 1, 0)
		listOfListsOfLemmasToExamine.add(edgeDLemm)
		isNgram.add(edgeDNgram)
	
		val edgeELemm = listOf<String>("komunikaci","zlepsit", "musime")
		val edgeENgram = listOf<Int>(1, 1, 1)
		listOfListsOfLemmasToExamine.add(edgeELemm)
		isNgram.add(edgeENgram)
	
		val edgeFLemm = listOf<String>("musime", "zlepsit", "komunikaci")
		val edgeFNgram = listOf<Int>(3, 2, 1)
		listOfListsOfLemmasToExamine.add(edgeFLemm)
		isNgram.add(edgeFNgram)
	
		val edgeGLemm = listOf<String>("zlepsit", "vse", "pratele")
		val edgeGNgram = listOf<Int>(3, 2, 1)
		listOfListsOfLemmasToExamine.add(edgeGLemm)
		isNgram.add(edgeGNgram)
	
		val edgeHLemm = listOf<String>("pratele", "vse", "zlepsit")
		val edgeHNgram = listOf<Int>(1, 1, 1)
		listOfListsOfLemmasToExamine.add(edgeHLemm)
		isNgram.add(edgeHNgram)

	//"musime", "zlepsit", "komunikaci", "musime", "zlepsit", "interakci", "zlepsit", "vse", "pratele"
	
		val edgeILemm = listOf<String>("pratele")
		val edgeINgram = listOf<Int>(1)
		listOfListsOfLemmasToExamine.add(edgeILemm)
		isNgram.add(edgeINgram)
	
		val edgeJLemm = listOf<String>("vse", "pratele")
		val edgeJNgram = listOf<Int>(2,1)
		listOfListsOfLemmasToExamine.add(edgeJLemm)
		isNgram.add(edgeJNgram)
	
	
		val edge0Lemm = listOf<String>("vubectamneni","faktne")
		val edge0Ngram = listOf<Int>(0,0)
		listOfListsOfLemmasToExamine.add(edge0Lemm)
		isNgram.add(edge0Ngram)
	
		val edge1Lemm = listOf<String>("")
		val edge1Ngram = listOf<Int>(0)
		listOfListsOfLemmasToExamine.add(edge1Lemm)
		isNgram.add(edge1Ngram)
	
	//"musime", "zlepsit", "komunikaci", "musime", "zlepsit", "interakci", "zlepsit", "vse", "pratele"
	
		val edge2Lemm = listOf<String>("zlepsit")
		val edge2Ngram = listOf<Int>(1)
		listOfListsOfLemmasToExamine.add(edge2Lemm)
		isNgram.add(edge2Ngram)
	
		val edge3Lemm = listOf<String>("musime", "zlepsit", "komunikaci", "musime", "zlepsit", "interakci", "zlepsit", "vse", "pratele")
		val edge3Ngram = listOf<Int>(9,8,7,6,5,4,3,2,1)
		listOfListsOfLemmasToExamine.add(edge3Lemm)
		isNgram.add(edge3Ngram)
	
		val edge31Lemm = listOf<String>("musime", "zlepsit", "komunikaci", "", "musime", "zlepsit", "interakci", "zlepsit", "vse", "pratele")
		val edge31Ngram = listOf<Int>(3,2,1,0,6,5,4,3,2,1)
		listOfListsOfLemmasToExamine.add(edge31Lemm)
		isNgram.add(edge31Ngram)
	
		val edge4Lemm = listOf<String>("komunikaci", "musime","musime", "zlepsit", "zlepsit", "interakci", "zlepsit", "vse", "pratele")
		val edge4Ngram = listOf<Int>(2,1,2,1,5,4,3,2,1)
		listOfListsOfLemmasToExamine.add(edge4Lemm)
		isNgram.add(edge4Ngram)
	
		val edge5Lemm = listOf<String>("pratele")
		val edge5Ngram = listOf<Int>(1)
		listOfListsOfLemmasToExamine.add(edge5Lemm)
		isNgram.add(edge5Ngram)
	
		val edge6Lemm = listOf<String>("pratele", "vse", "zlepsit")
		val edge6Ngram = listOf<Int>(1, 1, 1)
		listOfListsOfLemmasToExamine.add(edge6Lemm)
		isNgram.add(edge6Ngram)
	
		val edge7Lemm = listOf<String>("komunikaci", "interakci", "zlepsit", "vse")
		val edge7Ngram = listOf<Int>(1, 3, 2, 1)
		listOfListsOfLemmasToExamine.add(edge7Lemm)
		isNgram.add(edge7Ngram)
	
		val edge8Lemm = listOf<String>("pratele", "")
		val edge8Ngram = listOf<Int>(1, 0)
		listOfListsOfLemmasToExamine.add(edge8Lemm)
		isNgram.add(edge8Ngram)
	
	//"musime", "zlepsit", "komunikaci", "musime", "zlepsit", "interakci", "zlepsit", "vse", "pratele"

			for(i in 0 .. listOfListsOfLemmasToExamine.size - 1){
			println("\n\nEDGE CASE TEST===============================================================")
			val currList = listOfListsOfLemmasToExamine.get(i)
			val currIsNgram = isNgram.get(i)
			assert(currList.size == currIsNgram.size)
			println("$model\nanalyzing list of: $currList\nexpecting: $currIsNgram")
			for(j in 0 .. currList.size - 1){
				val expected = currIsNgram.get(j)
				val got = model.nGramGraph.isNgram(currList, j)
				println("${currList.get(j)} : expected $expected got $got")
				if (expected != got) fail("see why")
			}
			 
		}
}
fun testPrintNgrams(lemmas: List<String>, wrapper: ModelWrapper){
	println("\n\n------------------nGrams, model = ${wrapper.modelName}\n")
	var acu = 0
	for(i in lemmas.indices){
		val nGramNow = wrapper.nGramGraph.isNgram(lemmas,i)
		//println("${lemmas.get(i)}: is nGram of: $nGramNow")
		acu += nGramNow
	}
	println("acu: $acu")
}





