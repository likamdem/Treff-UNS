package com.example.treffuns;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegistrierenActivity extends AppCompatActivity {

    // Reguläre Ausdrücke zur Validierung von E-Mail und Passwort
    private final String E_MAIL_REGEX = "^[a-zA-Z0-9._%+-]+@(studmail\\.w-hs\\.de|[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$";
    private final String PASSWORT_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$";


    // UI-Elemente für die Eingabefelder und den Registrierungsbutton
    private TextInputEditText emailEingabeFeld;
    private TextInputEditText passwortEingabeFeld;
    private TextInputEditText passwortBestaetigungFeld;
    private Button registrierenButton;


    private FirebaseAuth auth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_registrieren); // Layout wird auf activity_anmelde gesetzt.
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });


            auth = FirebaseAuth.getInstance();

            // Initialisiert die UI-Elemente aus dem Layout.
            emailEingabeFeld = findViewById(R.id.email);
            passwortEingabeFeld = findViewById(R.id.passwort);
            passwortBestaetigungFeld = findViewById(R.id.confirmPasswort);
            registrierenButton = findViewById(R.id.anmeldenBtn);

            // Definiert den Klick-Listener für den Button, der die Methode 'anmelden' aufruft.
            registrierenButton.setOnClickListener(this::anmelden);
        }

        // Navigiert zur Anmelde-Aktivität.
        public void anmelden(View view) {
            startActivity(new Intent(RegistrierenActivity.this, AnmeldenActivity.class));
        }


        // Überprüft die eingegebenen Daten und startet den Registrierungsprozess.
        public void registrieren(View view) {
            ProgressDialog ladeDialog = new ProgressDialog(this);
            ladeDialog.setMessage("Verbindung wird hergestellt.");

            // Überprüft, ob das E-Mail-Feld leer ist.
            if (emailEingabeFeld.getText().toString().isBlank()) {
                emailEingabeFeld.setError("Bitte geben Sie Ihre E-Mail-Adresse ein.");
                emailEingabeFeld.requestFocus();
                // Überprüft das Format der E-Mail-Adresse.
            } else if (!emailEingabeFeld.getText().toString().matches(E_MAIL_REGEX)) {
                emailEingabeFeld.setError("Bitte geben Sie eine gültige E-Mail-Adresse ein.");
                // Überprüft, ob das Passwort-Feld leer ist.
            } else if (passwortEingabeFeld.getText().toString().isBlank()) {
                passwortEingabeFeld.setError("Bitte geben Sie Ihr Passwort ein.");
                passwortEingabeFeld.requestFocus();
                // Überprüft das Format des Passworts.
            } else if (!passwortEingabeFeld.getText().toString().matches(PASSWORT_REGEX)) {
                passwortEingabeFeld.setError("Geben Sie bitte ein gültiges Passwort ein.");
                passwortBestaetigungFeld.requestFocus();
                // Überprüft, ob das Bestätigungs-Feld leer ist.
            } else if (passwortBestaetigungFeld.getText().toString().isBlank()) {
                passwortBestaetigungFeld.setError("Bitte bestätigen Sie Ihr Passwort.");
                // Überprüft, ob die Passwörter übereinstimmen.
            } else if (!passwortEingabeFeld.getText().toString().equals(passwortBestaetigungFeld.getText().toString())) {
                passwortBestaetigungFeld.setError("Passwörter stimmen nicht überein.");
                passwortBestaetigungFeld.requestFocus();
                // Zeigt den Ladedialog an, wenn alle Validierungen erfolgreich sind.
            } else {
                ladeDialog.show();
                String gueltigeEmail = emailEingabeFeld.getText().toString();
                String gueltigesPasswort = passwortEingabeFeld.getText().toString();
                // Die Daten können nun an eine Datenbank gesendet werden.

                auth.createUserWithEmailAndPassword(gueltigeEmail, gueltigesPasswort).addOnCompleteListener(results -> {
                    if (results.isSuccessful()) {
                        ladeDialog.cancel();
                        emailEingabeFeld.getText().clear();
                        passwortEingabeFeld.getText().clear();
                        passwortBestaetigungFeld.getText().clear();
                        Toast.makeText(this, "Anmelden erfolgreich. Loggen sie sich ein.", Toast.LENGTH_SHORT).show();
                        Usermodel userModel = new Usermodel(gueltigeEmail, "","","",-1,"");
                        FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Intent intent = new Intent(RegistrierenActivity.this, AnmeldenActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            }
                        });
                    } else {
                        ladeDialog.cancel();
                        try {
                            throw results.getException();
                        } catch (FirebaseAuthUserCollisionException fE) {
                            emailEingabeFeld.setError("Ihre Email ist schon vergeben.");
                            emailEingabeFeld.requestFocus();
                        } catch (FirebaseNetworkException e) {
                            Toast.makeText(this, "Sie sind nicht verbunden. Bitte Verbinden sie sich.", Toast.LENGTH_LONG);
                        } catch (Exception e) {
                            Toast.makeText(this, "Anmeldung fehlgeschlagen. Bitte versuchen Sie es später erneut.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }

}