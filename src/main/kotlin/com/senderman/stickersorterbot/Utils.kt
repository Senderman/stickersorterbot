package com.senderman.stickersorterbot

/**
 * Transforms List<T> to List<List<T>> where inner lists size <= limit
 * For example, [1,2,3,4,5,6,7,8,9,0].groupByLimit(3) will return [ [1,2,3], [4,5,6], [7,8,9], [0] ]
 * @param limit limit for each list
 * @return list of lists
 */
fun <T> Iterable<T>.groupByLimit(limit: Int): Iterable<Iterable<T>> {
    val result: MutableList<MutableList<T>> = mutableListOf()
    var innerList: MutableList<T> = mutableListOf()
    for (el in this) {
        innerList.add(el)
        if (innerList.size == limit) {
            result.add(innerList)
            innerList = mutableListOf()
        }
    }
    // if the last innerList if not empty but were not added to result
    if (innerList.size in 1..limit) result.add(innerList)
    return result
}