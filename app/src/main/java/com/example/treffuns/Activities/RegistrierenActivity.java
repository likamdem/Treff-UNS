package com.example.treffuns.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.treffuns.utils.FirebaseUtil;
import com.example.treffuns.R;
import com.example.treffuns.model.Usermodel;
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

    // Firebase-Authentifizierungsinstanz zur Verwaltung der Benutzer
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrieren);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialisiert die Firebase-Authentifizierungsinstanz.
        auth = FirebaseAuth.getInstance();

        // Initialisiert die UI-Elemente aus dem Layout.
        emailEingabeFeld = findViewById(R.id.email);
        passwortEingabeFeld = findViewById(R.id.passwort);
        passwortBestaetigungFeld = findViewById(R.id.confirmPasswort);
        registrierenButton = findViewById(R.id.registrirenBtn);

        // Setzt einen Klick-Listener auf den Button, der die Methode 'registrieren' aufruft.
        registrierenButton.setOnClickListener(this::registrieren);
    }

    // Navigiert zur Anmelde-Aktivität.
    public void anmelden(View view) {
        startActivity(new Intent(RegistrierenActivity.this, AnmeldenActivity.class));
    }

    // Überprüft die eingegebenen Daten und startet den Registrierungsprozess bei Firebase.
    public void registrieren(View view) {
        ProgressDialog ladeDialog = new ProgressDialog(this);
        ladeDialog.setMessage("Verbindung wird hergestellt.");

        // Validierungsprüfungen für alle Eingabefelder (E-Mail und Passwort).
        if (emailEingabeFeld.getText().toString().isBlank()) {
            emailEingabeFeld.setError("Bitte geben Sie Ihre E-Mail-Adresse ein.");
            emailEingabeFeld.requestFocus();
        } else if (!emailEingabeFeld.getText().toString().matches(E_MAIL_REGEX)) {
            emailEingabeFeld.setError("Bitte geben Sie eine gültige E-Mail-Adresse ein.");
        } else if (passwortEingabeFeld.getText().toString().isBlank()) {
            passwortEingabeFeld.setError("Bitte geben Sie Ihr Passwort ein.");
            passwortEingabeFeld.requestFocus();
        } else if (!passwortEingabeFeld.getText().toString().matches(PASSWORT_REGEX)) {
            passwortEingabeFeld.setError("Geben Sie bitte ein gültiges Passwort ein.");
            passwortBestaetigungFeld.requestFocus();
        } else if (passwortBestaetigungFeld.getText().toString().isBlank()) {
            passwortBestaetigungFeld.setError("Bitte bestätigen Sie Ihr Passwort.");
        } else if (!passwortEingabeFeld.getText().toString().equals(passwortBestaetigungFeld.getText().toString())) {
            passwortBestaetigungFeld.setError("Passwörter stimmen nicht überein.");
            passwortBestaetigungFeld.requestFocus();
        } else {
            // Wenn alle Validierungen erfolgreich sind, wird der Ladedialog angezeigt.
            ladeDialog.show();
            String gueltigeEmail = emailEingabeFeld.getText().toString();
            String gueltigesPasswort = passwortEingabeFeld.getText().toString();

            // Versucht, einen neuen Benutzer mit E-Mail und Passwort bei Firebase zu registrieren.
            auth.createUserWithEmailAndPassword(gueltigeEmail, gueltigesPasswort).addOnCompleteListener(results -> {
                if (results.isSuccessful()) {
                    // Die Registrierung war erfolgreich.
                    ladeDialog.cancel();
                    emailEingabeFeld.getText().clear();
                    passwortEingabeFeld.getText().clear();
                    passwortBestaetigungFeld.getText().clear();
                    Toast.makeText(this, "Anmeldung erfolgreich. Loggen Sie sich ein.", Toast.LENGTH_SHORT).show();

                    // Erstellt ein Usermodel-Objekt und speichert die Benutzerdetails in der Datenbank.
                    Usermodel userModel = new Usermodel(gueltigeEmail, "","","",-1,"");
                    FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                // Navigiert zur Anmelde-Aktivität und löscht den Back-Stack.
                                Intent intent = new Intent(RegistrierenActivity.this, AnmeldenActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                    });
                } else {
                    // Die Registrierung ist fehlgeschlagen.
                    ladeDialog.cancel();
                    try {
                        throw results.getException();
                    } catch (FirebaseAuthUserCollisionException fE) {
                        // Zeigt eine Fehlermeldung an, wenn die E-Mail-Adresse bereits registriert ist.
                        emailEingabeFeld.setError("Ihre E-Mail ist schon vergeben.");
                        emailEingabeFeld.requestFocus();
                    } catch (FirebaseNetworkException e) {
                        // Zeigt eine Fehlermeldung bei Netzwerkproblemen an.
                        Toast.makeText(this, "Sie sind nicht verbunden. Bitte stellen Sie eine Verbindung her.", Toast.LENGTH_LONG);
                    } catch (Exception e) {
                        // Zeigt eine generische Fehlermeldung für andere Fehler an.
                        Toast.makeText(this, "Anmeldung fehlgeschlagen. Bitte versuchen Sie es später erneut.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}