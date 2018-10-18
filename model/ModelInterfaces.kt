package model.learn

interface NgramProvider {
	fun getDescription(): String
	fun getPopularityTuplesSize(): Int
	fun getNgramSize(): Int
	fun getPopularityHeadOfSize(topRank: Int): List<PopularityTuple>
}