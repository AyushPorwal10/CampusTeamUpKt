const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();


const db = admin.firestore();


// when teamMember is interested in users profile they can send request

exports.sendNotification = functions.https.onRequest(async (req, res) => {
  console.log("Raw Request Body:", JSON.stringify(req.body, null, 2));

  const message = req.body.message;

  if (!message || !message.token) {
    console.error("Role FCM Token is missing!");
    return res.status(400).send("FCM Token is required");
  }

  const {senderId, senderName, time, phoneNumber} = message.data || {};
  const {title, body} = message.notification || {};
  const fcmToken = message.token;

  const fcmMessage = {
    token: fcmToken,
    notification: {title, body},
    data: {
      senderId: senderId || "",
      senderName: senderName || "",
      time: time || "",
      phoneNumber: phoneNumber || "",
    },
  };

  try {
    console.log("Sending FCM Message:", JSON.stringify(fcmMessage, null, 2));

    const response = await admin.messaging().send(fcmMessage);
    console.log("FCM Response:", response);

    return res.status(200).send("Notification sent successfully");
  } catch (error) {
    console.error("Error sending notification:", error);
    return res.status(500).send("Error sending notification");
  }
});

// This is when a user requests to join a team
exports.sendTeamJointNotification = functions.https.onRequest(
    async (req, res) => {
      console.log(
          "Raw Request Body:",
          JSON.stringify(req.body, null, 2),
      );

      const message = req.body.message;

      if (!message || !message.token) {
        console.error("Vacancy FCM Token is missing!");
        return res.status(400).send("FCM Token is required");
      }

      const {
        senderId,
        senderName,
        time,
        phoneNumber,
      } = message.data || {};

      const {title, body} = message.notification || {};
      const fcmToken = message.token;

      const fcmMessage = {
        token: fcmToken,
        notification: {title, body},
        data: {
          senderId: senderId || "",
          senderName: senderName || "",
          time: time || "",
          phoneNumber: phoneNumber || "",
        },
      };

      try {
        console.log(
            "Sending FCM Message:",
            JSON.stringify(fcmMessage, null, 2),
        );

        const response = await admin.messaging().send(fcmMessage);
        console.log("FCM Response:", response);

        return res.status(200).send("Notification sent successfully");
      } catch (error) {
        console.error("Error sending notification:", error);
        return res.status(500).send("Error sending notification");
      }
    },
);


/**
 * Callable function to delete a user and their data.
 * @param {Object} data - Data from client.
 * @param {string} data.email - The email of the user to delete.
 */
exports.deleteUserData = functions.https.onCall(async (data, context) => {
  try {
    console.log("AppCheck:", context.app ?
    context.app.appId : "No AppCheck object");
    console.log("Raw request data:", data);

    const email = data.data.email;

    if (!email) {
      throw new Error("Email is required");
    }

    // Extract userId from email (string before @)
    const userId = email.split("@")[0];

    console.log("User id is :", userId);

    // Delete Firestore docs
    const userIdRef = db.collection("all_user_id").doc(userId);
    const emailRef = db.collection("emails").doc(email);

    await Promise.all([
      userIdRef.delete(),
      emailRef.delete(),
    ]);

    // Find Firebase Auth user by email
    const userRecord = await admin.auth().getUserByEmail(email);

    // Delete user in Firebase Auth
    await admin.auth().deleteUser(userRecord.uid);

    await db.collection("deleted_account").doc(userId).set({
      userId: userId,
      email: email,
      deletedAt: admin.firestore.FieldValue.serverTimestamp(),
    });


    return {success: true, message: "User deleted successfully"};
  } catch (error) {
    console.error("Error deleting user:", error);
    return {success: false, message: error.message};
  }
});


