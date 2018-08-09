package com.example.welington.locedu.Controller;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by welington on 09/08/18.
 */

public class ReferencesHelper {
    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference databaseReference;

    public static FirebaseDatabase getFirebaseDatabase() {
        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
        }
        return firebaseDatabase;
    }

    public static DatabaseReference getDatabaseReference() {
        if (databaseReference == null)
            databaseReference = getFirebaseDatabase().getReference();
        return databaseReference;
    }
}
