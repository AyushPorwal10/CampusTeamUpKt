package com.example.campus_teamup.helper

object  ChatRoomId{


    fun getChatRoomId(senderId : String , receiverId : String) : String{
        return if(senderId.hashCode() > receiverId.hashCode())
            senderId + "_" + receiverId
        else
            receiverId + "_" + senderId
    }
}