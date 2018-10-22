package input.lemmatize

import java.io.File
import java.lang.ProcessBuilder.Redirect
import java.util.concurrent.TimeUnit


public val PREFIX = "LEMMATIZED_"

class Lemmatizer(inputDirPath: String, filename: String, filenameLemmatized: String) {

	private val inputFilesDirPath = inputDirPath
	private val filename = filename
	private val filenameLemmatized = filenameLemmatized
	private val TAG = this.toString()

	// TODO di?
	val morphoDiTaModel = "/home/radim/morphoDiTaModels/czech-morfflex-pdt-161115/czech-morfflex-pdt-161115.tagger" //czech-morfflex-161115.dict
	val morphoDiTaBinDir = "/home/radim/morphoDiTa/githubRepo/morphodita/src/"
	//val morphoDiTaBinDir = "/home/radim/morphoDiTa/morphodita/src/"// laptop
	val useGuesser = 1
	
	val baseMorphoDiTaCommand = morphoDiTaBinDir + "run_morpho_analyze --output=vertical --from_tagger " + morphoDiTaModel + " " + useGuesser + " "

	fun lemmatize() {
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