package com.example.new_campus_teamup.clean_code

import javax.inject.Inject

class PostHandlerFactory @Inject constructor(
    private val handlers : Map<PostType, @JvmSuppressWildcards BasePostHandler>
) {
    fun getHandler(postType: PostType) : BasePostHandler {
        return handlers[postType] ?: throw IllegalArgumentException("Handler not defined for $postType")
    }
}



enum class PostType{
    ROLE,
    VACANCY,
    PROJECT
}