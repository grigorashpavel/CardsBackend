package com.pasha.util

class SearchUtil {
    companion object {
        fun calculateNameSimilarity(name1: String, name2: String): Int {
            var similarity = 0
            for (i in name1.indices) {
                if (i < name2.length && name1[i] == name2[i]) {
                    similarity++
                } else {
                    break
                }
            }
            return similarity
        }
    }
}