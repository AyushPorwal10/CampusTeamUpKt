package com.example.new_campus_teamup.clean_code_1

import com.example.new_campus_teamup.clean_code.PostType
import javax.inject.Inject

class ViewPostHandlerFactory @Inject constructor(
    private val handlers : Map<PostType, @JvmSuppressWildcards ViewPostHandler>
) {
    fun getHandler(postType: PostType) : ViewPostHandler {
        return handlers[postType] ?: throw IllegalArgumentException("No Handler provided for $postType")
    }
}