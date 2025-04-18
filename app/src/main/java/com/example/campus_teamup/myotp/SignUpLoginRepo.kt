package com.example.campus_teamup.myotp

import android.app.Activity
import android.util.Log
import com.example.campus_teamup.myactivities.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SignUpLoginRepo @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {
    fun startPhoneNumberVerification(
        phoneNumber: String,
        activity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks,
        onVerificationSuccess: () -> Unit,
        onVerificationFailed: () -> Unit
    ) {

        try {
            val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
            onVerificationSuccess()
        } catch (e: Exception) {
            onVerificationFailed()
        }


    }

    fun verifyOTP(verificationId: String, code: String): PhoneAuthCredential {
        return PhoneAuthProvider.getCredential(verificationId, code)
    }

    fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "Verification failed")
                }
            }
    }


    // function to check if email is already registered or not

    suspend fun isEmailOrPhoneNumberRegistered(email: String, phoneNumber: String): Boolean {
        val isEmail = firebaseFirestore.collection("emails").document(email).get().await()
        val isPhoneNumber =
            firebaseFirestore.collection("phone_number").document(phoneNumber).get().await()
        //either is registered toast a message to user
        return isEmail.exists() || isPhoneNumber.exists()
    }


    // when user do sign up save data to data store

    fun saveUserDataToDatabase(userData: UserData  , onUserDataSaved : () -> Unit ) {


        val batch = firebaseFirestore.batch()

        // save user email
        val saveEmail = firebaseFirestore.collection("emails").document(userData.email)

        // save phone number
        val savePhoneNumber =
            firebaseFirestore.collection("phone_number").document(userData.phoneNumber)

        // save user details


        val saveUserDetails =
            firebaseFirestore.collection("all_user_id").document(userData.phoneNumber)
                .collection("signup_details").document(userData.phoneNumber)




        batch.set(saveEmail, mapOf("email" to userData.email))

        batch.set(savePhoneNumber, mapOf("phone_number" to userData.phoneNumber))

        batch.set(saveUserDetails, userData)

        batch.commit()

        onUserDataSaved()
        Log.d("OneTimePass", "User all saving operation done ")
    }


    // fetching data from firebase when user login

    suspend fun fetchDataFromDatabase(
        phoneNumber: String,
        onUserDataFetched: (DocumentSnapshot) -> Unit
    ) {
        Log.d("OneTimePass","Going to fetch user data ")
        val userData = firebaseFirestore.collection("all_user_id").document(phoneNumber)
            .collection("signup_details").document(phoneNumber).get().await()
        onUserDataFetched(userData)
    }


}