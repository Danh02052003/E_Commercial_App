package vlu.mobileproject.globalfuction;

import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageHandler {


    private static String cutEmail(String emailAccountLogin){
        return emailAccountLogin.replace("@gmail.com", "");
    }

    // Need id Image and Email Account
    public static void setImageFromFirebaseStorage(CircleImageView avatarAccount, String emailAccount) {
        String avatarUrl = "https://firebasestorage.googleapis.com/v0/b/e-commerce-73482.appspot.com/o/avatars%2Favatar_" + cutEmail(emailAccount) +".png?alt=media";

        // Get the reference to the Firebase Storage file using the image URL
        StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(avatarUrl);

        // Download the image to a temporary file
        try {
            File localFile = File.createTempFile("avatar", ".png");
            imageRef.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image downloaded successfully, set it to the CircleImageView
                        avatarAccount.setImageURI(Uri.fromFile(localFile));
                    })
                    .addOnFailureListener(exception -> {
                        // Handle any errors that occur during the download
                        Log.e("FirebaseError", "Failed to download image: " + exception.getMessage());
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
