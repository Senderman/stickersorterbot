package com.senderman.stickersorterbot

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

/**
 * Download file from external resource
 * @param url url of resource to download
 * @param destination destination file (absolute or relative path)
 * @return file object
 *
 * @throws IOException if url/destination is not accessible
 * @throws MalformedURLException on bad url
 * @throws FileAlreadyExistsException if destination already exists
 */
fun downloadFile(url: String, destination: String): File {
    val connection = URL(url).openConnection()
    val file = File(destination)
    if (file.exists()) throw FileAlreadyExistsException(file)
    val input = connection.getInputStream()
    val out = FileOutputStream(file)
    val buffer = ByteArray(2048)
    var length: Int

    while (input.read(buffer).also { length = it } != -1)
        out.write(buffer, 0, length)

    input.close()
    out.close()
    return file
}

/**
 * Transforms List<T> to List<List<T>> where inner lists size <= limit
 * For example, [1,2,3,4,5,6,7,8,9,0].groupByLimit(3) will return [ [1,2,3], [4,5,6], [7,8,9], [0] ]
 * @param limit limit for each list
 * @return list of lists
 */
fun <T> Iterable<T>.groupByLimit(limit: Int): Iterable<Iterable<T>> {
    val result: MutableList<MutableList<T>> = mutableListOf()
    var innerList: MutableList<T> = mutableListOf<T>()
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