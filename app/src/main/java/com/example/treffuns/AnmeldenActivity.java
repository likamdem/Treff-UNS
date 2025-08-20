package com.example.treffuns;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

import com.google.android.material.textfield.TextInputEditText;

public class AnmeldenActivity extends AppCompatActivity {


// Reguläre Ausdrücke zur Validierung von E-Mail und Passwort
    private final String E_MAIL_REGEX = "^[a-zA-Z0-9._%+-]+@(studmail\\.w-hs\\.de|[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$";
    private final String PASSWORT_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$";


    private Button anmeldeButton;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_anmelden);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        anmeldeButton = findViewById(R.id.einloggenBtn);
        anmeldeButton.setOnClickListener(this::anmelden);
        auth = FirebaseAuth.getInstance();
    }

    //Navigiert zur registrieren-Aktivität.
    public void registrieren(View view){
        startActivity(new Intent(AnmeldenActivity.this, RegistrierenActivity.class));
    }

    // Methode, die den Anmeldevorgang startet. Sie wird bei einem Klick auf den Login-Button aufgerufen.
    public void anmelden(View view) {
        TextInputEditText emailFeld = findViewById(R.id.email);
        TextInputEditText passwortFeld = findViewById(R.id.passwort);
        ProgressDialog ladeDialog = new ProgressDialog(this);

        // Konfigurieren des Ladedialogs
        ladeDialog.setMessage("Verbindung wird hergestellt...");
        ladeDialog.setCanceledOnTouchOutside(false);

        // Überprüfungen, ob die Eingaben den Anforderungen entsprechen
        if (emailFeld.getText().toString().isBlank()) {
            emailFeld.setError("Bitte geben Sie Ihre E-Mail-Adresse ein.");
            emailFeld.requestFocus();
        } else if (!emailFeld.getText().toString().matches(E_MAIL_REGEX)) {
            emailFeld.setError("Bitte geben Sie eine gültige E-Mail-Adresse ein.");
        } else if (passwortFeld.getText().toString().isBlank()) {
            passwortFeld.setError("Bitte geben Sie Ihr Passwort ein.");
            passwortFeld.requestFocus();
        } else if (!passwortFeld.getText().toString().matches(PASSWORT_REGEX)) {
            passwortFeld.setError("Bitte geben Sie ein gültiges Passwort ein.");
        } else {
            // Zeigt den Ladedialog an, wenn alle Überprüfungen erfolgreich waren
            ladeDialog.show();

            String gueltigeEmail = emailFeld.getText().toString();
            String gueltigesPasswort = passwortFeld.getText().toString();

            auth.signInWithEmailAndPassword(gueltigeEmail, gueltigesPasswort).addOnCompleteListener(results ->{
                if(results.isSuccessful()){
                    ladeDialog.cancel();
                    emailFeld.getText().clear();
                    passwortFeld.getText().clear();
                    Toast.makeText(this,"Einloggen erfolgreich.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AnmeldenActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else {
                    ladeDialog.cancel();
                    try{
                        throw results.getException();
                    }catch (FirebaseAuthInvalidCredentialsException fE){
                        emailFeld.setError("Ihre Email oder Passwort ist falsch.");
                        emailFeld.requestFocus();
                    }catch (FirebaseNetworkException e){
                        Toast.makeText(this, "Sie haben keine Internet-Verbindung. Bitte verbinden sie sich.", Toast.LENGTH_LONG).show();
                    }
                    catch (Exception e) {
                        Toast.makeText(this, "Einloggen fehlgeschlagen. bitte versuchen sie es später.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}