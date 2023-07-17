package com.example.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class Welcome extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "GOOGLEAUTH";
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        SharedPreferences preferences = getSharedPreferences("ADMIN_LOGIN",MODE_PRIVATE);
        String isadmin = preferences.getString("IS_ADMIN_LOGGED_IN","");
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(isadmin.equals("true")){
            startActivity(new Intent(Welcome.this, Admin_home.class));
            finish();
        }
        else if (currentUser!=null){
            Intent i = new Intent(Welcome.this,Customer_Home.class);
            startActivity(i);
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        // Continue with google
        ImageButton signInbtn = findViewById(R.id.google);
        signInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        // Admin login button
        Button adminLoginBtn = findViewById(R.id.login_admin);
        adminLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminLogin();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            // Checking if the user is logging in for the first time and take user to register activit
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            FirebaseUser u = mAuth.getCurrentUser();
                            DatabaseReference usersRef = database.getReference("Users");
                            String emailIdToCheck = u.getEmail();
                            Log.d(TAG, "onComplete: "+emailIdToCheck);
                            usersRef.orderByChild("email").equalTo(emailIdToCheck).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        // Email ID found
                                        // Toast.makeText(Welcome.this, "Email id exists", Toast.LENGTH_SHORT).show();
                                        Log.d("Firebase", "Email ID is already present in the database.");

                                        // Go to user landing page
                                        Intent i =new Intent(Welcome.this, Customer_Home.class);
                                        startActivity(i);
                                        finish();


                                    } else {
                                        // Email ID not found
                                        // Toast.makeText(Welcome.this, "Email id doesn't exist", Toast.LENGTH_SHORT).show();
                                        Log.d("Firebase", "Email ID is not present in the database.");
                                        goToRegister();
                                        // Go to register activity

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // An error occurred while querying the database
                                    Log.e("Firebase", "Error querying the database: " + databaseError.getMessage());
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Welcome.this,"Login failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void adminLogin(){
        Intent i = new Intent(Welcome.this,Admin_Login.class);
        startActivity(i);
//        finish();
    }
    private void goToRegister(){
        Intent i = new Intent(Welcome.this,Register.class);
        startActivity(i);
        finish();
    }
}