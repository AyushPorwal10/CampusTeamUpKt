package com.example.new_campus_teamup.screens.profilescreens

import android.graphics.drawable.Icon
import androidx.compose.ui.res.stringResource
import com.example.new_campus_teamup.R

object CheckProfileLink {

    // this is to check which coding profile user added
    fun getPlatformNameAndIcon(url : String ) : CodingProfileIconAndUrl{
        val lowerCase = url.lowercase()

        if(lowerCase.contains("leetcode")) return CodingProfileIconAndUrl("Leetcode",R.drawable.leetcode)
        else if(lowerCase.contains("geeksforgeeks") || lowerCase.contains("gfg")) return CodingProfileIconAndUrl("Geeksforgeeks",R.drawable.gfg)
        else if(lowerCase.contains("codechef"))return CodingProfileIconAndUrl("Codechef",R.drawable.codechef)
        else if(lowerCase.contains("codeforces"))return CodingProfileIconAndUrl("Codeforces",R.drawable.codeforces)
        else if(lowerCase.contains("dummy")) return CodingProfileIconAndUrl("No Profile Found." , R.drawable.coding)
        else
            return CodingProfileIconAndUrl("Coding Profile",R.drawable.coding)
    }

}

data class CodingProfileIconAndUrl(val platformName : String ,val  platformIcon : Int)