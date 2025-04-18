const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();


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
