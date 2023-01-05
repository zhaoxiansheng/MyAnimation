package com.example.leetcode.arithmetic

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.nio.CharBuffer
import java.util.*
import kotlin.collections.LinkedHashMap

class Array {

    companion object {
        @RequiresApi(Build.VERSION_CODES.N)
        fun firstUniqChar(s: String): Char {
            if (s.isEmpty()) {
                return ' '
            }
            val hashMap: LinkedHashMap<Char, Int> = LinkedHashMap()
            for(a in s.toCharArray()) {
                hashMap[a] = hashMap.getOrDefault(a, 0) + 1
            }

            for ((k,v) in hashMap) {
                Log.d("TAG", "firstUniqChar: $k , $v")
                if (v==1) {
                    return k
                }
            }

            return ' '
        }
    }

}