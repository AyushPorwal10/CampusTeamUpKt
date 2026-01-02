package com.example.new_campus_teamup.clean_code

import javax.inject.Inject

class ProjectHandler @Inject constructor(

): BasePostHandler {
    override suspend fun post(postDto: BasePostDto): Boolean {
        return true
    }

    override suspend fun delete(config: DeletePostConfig): Boolean {
        return true
    }

}