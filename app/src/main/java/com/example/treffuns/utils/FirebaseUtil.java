package com.example.treffuns.utils;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtil {
    private FirebaseAuth auth;

    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }
    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }


    public static DocumentReference currentUserTreffen(){
        return FirebaseFirestore.getInstance().collection("treffens").document(currentUserId());
    }
    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("users");
    }


    public static DocumentReference getChatRoomReference(String chatRoomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatRoomId);
    }

    public static CollectionReference getChatRoomTreffenReference(String chatRoomId){
        return getChatRoomReference(chatRoomId).collection("chats");
    }


    public static CollectionReference allTreffenCollectionReference(){
        return FirebaseFirestore.getInstance().collection("treffens");
    }



}
