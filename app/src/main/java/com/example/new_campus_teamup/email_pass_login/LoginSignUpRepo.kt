package com.example.new_campus_teamup.email_pass_login

import javax.inject.Inject
import android.app.Activity
import android.util.Log
import com.example.new_campus_teamup.myactivities.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.lang.Error
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume


class LoginSignUpRepo @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    val tag = "SignUp_In_Repo"
    suspend fun signInUser(email: String, password: String): Result<Unit> =
        suspendCancellableCoroutine { continuation ->

            firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(tag, "sign-in success")
                        continuation.resume(Result.success(Unit))
                    } else {
                        val error = task.exception
                        when (error) {
                            is FirebaseAuthUserCollisionException -> {
                                Log.e(tag, "Email not registered")
                                continuation.resume(Result.failure(Exception("Email not registered")))
                            }

                            is FirebaseAuthInvalidCredentialsException -> {
                                Log.e(tag, "Wrong password")
                                continuation.resume(Result.failure(Exception("Email or password is incorrect")))
                            }

                            else -> {
                                Log.e(tag, "Something went wrong ${error?.localizedMessage}")

                                continuation.resume(Result.failure(Exception("Something went wrong. Please try again later.")))
                            }
                        }
                    }
                }
        }

    suspend fun signUpUser(email: String, password: String): Result<Unit> =
        suspendCancellableCoroutine { continuation ->
            firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(tag, "sign-up success")
                        continuation.resume(Result.success(Unit))
                    } else {
                        val error = task.exception
                        when (error) {
                            is FirebaseAuthUserCollisionException -> {
                                Log.d(tag, "email exists")
                                continuation.resume(Result.failure(Exception("Email already exists")))
                            }

                            else -> {
                                Log.d(tag, "sign-up error: ${error?.localizedMessage}")
                                continuation.resume(Result.failure(Exception("Something went wrong\n Please try again later")))
                            }
                        }
                    }
                }
        }


    // cheching if email exists for which user want to change password
    suspend fun isEmailRegistered(email: String, ) : Result<Unit> = suspendCancellableCoroutine{continuation->
        val documentRef = firebaseFirestore.collection("emails").document(email)

        documentRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val storedEmail = document.getString("email")

                    if(storedEmail == email){
                        continuation.resume(Result.success(Unit))
                    }
                } else {
                    continuation.resume(Result.failure(Exception("Email is not registered\n Please SignUp")))
                }
            }
            .addOnFailureListener {
                continuation.resume(Result.failure(Exception("Something went wrong.")))
            }
    }

    suspend fun sendPasswordVerificationEmail(email: String): Result<Unit> =
        suspendCancellableCoroutine { continuation ->
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.success(Unit))
                    } else {
                        continuation.resume(Result.failure(Exception()))
                        Log.d(tag, "Task is not success full in password reset")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(tag, exception.localizedMessage)
                    continuation.resume(Result.failure(Exception()))
                }

        }

    // when user do sign up save data to data store
    fun saveUserDataToDatabase(userData: UserData, onUserDataSaved: () -> Unit) {


        val batch = firebaseFirestore.batch()

        // save user email
        val saveEmail = firebaseFirestore.collection("emails").document(userData.email)

        // save user details


        val saveUserDetails =
            firebaseFirestore.collection("all_user_id").document(userData.userId)
                .collection("signup_details").document(userData.userId)

        batch.set(saveEmail, mapOf("email" to userData.email))
        batch.set(saveUserDetails, userData)
        batch.commit()
        onUserDataSaved()
    }


    // fetching data from firebase when user login
    suspend fun fetchDataFromDatabase(
        userId: String,
        onUserDataFetched: (DocumentSnapshot) -> Unit
    ) {
        Log.d(tag, "Going to fetch user data ")
        val userData = firebaseFirestore.collection("all_user_id").document(userId)
            .collection("signup_details").document(userId).get().await()
        onUserDataFetched(userData)
    }


}