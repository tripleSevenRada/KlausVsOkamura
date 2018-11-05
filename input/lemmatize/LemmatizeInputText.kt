package input.lemmatize

import java.io.File
import java.lang.ProcessBuilder.Redirect
import java.util.concurrent.TimeUnit

import app.fail


public val PREFIX = "LEMMATIZED_"

class Lemmatizer{

	private val TAG = this.toString()
	
	private var inputFilesDirPath: String
	private var filename: String
	private var filenameLemmatized: String
	
	private var morphoDiTaModel: String
	private var morphoDiTaBinDir: String
	private var useGuesser: String
	
	constructor(properties: Map<String,String>){
		try{
			inputFilesDirPath = properties.get("inputDir")!!
			filename = properties.get("inputFileNonLemmatized")!!
			filenameLemmatized = properties.get("inputFileLemmatized")!!
	
			morphoDiTaModel = properties.get("morphoDiTaModel")!!
			morphoDiTaBinDir = properties.get("morphoDiTaBinDir")!!
			useGuesser = properties.get("useGuesser")!!
		}catch(e: NullPointerException){
			fail("$TAG - NullPointerException")
		}	
	}
	
	fun lemmatize() {
		
		val baseMorphoDiTaCommand = morphoDiTaBinDir + "run_morpho_analyze --output=vertical --from_tagger " + morphoDiTaModel + " " + useGuesser + " "

		val inputFile: File = File(inputFilesDirPath + filename)
		val fullCommand = baseMorphoDiTaCommand + inputFile.getPath() + ":" + inputFilesDirPath + PREFIX + filenameLemmatized
		println("$TAG: LEMMATIZE FULL COMMAND:")
		println(fullCommand)
		fullCommand.runCommand(File(inputFilesDirPath))
	}

	private fun String.runCommand(workingDir: File) {
		ProcessBuilder(*split(" ").toTypedArray())
				.directory(workingDir)
				.redirectOutput(Redirect.INHERIT)
				.redirectError(Redirect.INHERIT)
				.start()
				.waitFor(2, TimeUnit.MINUTES)
	}
}