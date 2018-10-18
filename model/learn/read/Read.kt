package model.learn.read

import java.io.File
import java.io.*
import app.*

// TODO Boolean print not nice
class ModelDataReader(rootFolder: String, printMode: Boolean) {

	private val TAG = "MODEL_DATA_READER"
	private val root = rootFolder
	private val print = printMode

	fun getListOfFiles(): List<File> {
		var listOfFiles = mutableListOf<File>()

		File(root).walkTopDown().forEach {
			if (!it.isDirectory()) {
				if (print) println("$TAG : ${it.getName()} added to process")
				listOfFiles.add(it)
			} else if (print) println("$TAG : ${it.getName()} is directory, skipped")
		}

		println("\n\nfiles to process: ${listOfFiles.size}")

		if (listOfFiles.size == 0) {
			fail("listOfFiles.size == 0")
		}

		return listOfFiles
	}

	fun readFile(file: File): List<String> {
		var listOfLemmasPerFile = mutableListOf<String>()
		try {
			file.forEachLine {
				val lemmaListPerLine = it.split("\t")
				if (lemmaListPerLine.size > 1) {
					val lemmaRaw = lemmaListPerLine.get(1)
					val indexOfUnderscore = lemmaRaw.indexOf('_')
					if (indexOfUnderscore > 0) {
						listOfLemmasPerFile.add(lemmaRaw.substring(0, indexOfUnderscore))
					} else {
						listOfLemmasPerFile.add(lemmaRaw)
					}
				}
			}
		} catch (e: IOException) {
			fail(e.toString())
		}
		return listOfLemmasPerFile
	}

	fun getLemmatizedList(): List<String> {
		var lemmatizedList = arrayListOf<String>()//we want get(index) = O(k). Not sure about List<String>(). Are You?
		val publishedLemmatizedList: List<String> = lemmatizedList
		getListOfFiles().forEach {
			lemmatizedList.addAll(readFile(it))
		}

		if (lemmatizedList.size == 0) {
			fail("lemmatizedList.size == 0")
		}

		return publishedLemmatizedList
	}
}
