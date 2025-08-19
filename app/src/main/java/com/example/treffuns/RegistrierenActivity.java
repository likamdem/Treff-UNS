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

import com.google.android.material.textfield.TextInputEditText;

public class RegistrierenActivity extends AppCompatActivity {

    // Reguläre Ausdrücke zur Validierung von E-Mail und Passwort
    private final String E_MAIL_REGEX = "^[a-zA-Z0-9._%+-]+@(studmail\\.w-hs\\.de|[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$";
    private final String PASSWORT_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$";


        // UI-Elemente für die Eingabefelder und den Registrierungsbutton
        private TextInputEditText emailEingabeFeld;
        private TextInputEditText passwortEingabeFeld;
        private TextInputEditText passwortBestätigungFeld;
        private Button registrierenButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_anmelde); // Layout wird auf activity_anmelde gesetzt.
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // Initialisiert die UI-Elemente aus dem Layout.
            emailEingabeFeld = findViewById(R.id.email);
            passwortEingabeFeld = findViewById(R.id.passwort);
            passwortBestätigungFeld = findViewById(R.id.confirmPasswort);
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
                passwortBestätigungFeld.requestFocus();
                // Überprüft, ob das Bestätigungs-Feld leer ist.
            } else if (passwortBestätigungFeld.getText().toString().isBlank()) {
                passwortBestätigungFeld.setError("Bitte bestätigen Sie Ihr Passwort.");
                // Überprüft, ob die Passwörter übereinstimmen.
            } else if (!passwortEingabeFeld.getText().toString().equals(passwortBestätigungFeld.getText().toString())) {
                passwortBestätigungFeld.setError("Passwörter stimmen nicht überein.");
                passwortBestätigungFeld.requestFocus();
                // Zeigt den Ladedialog an, wenn alle Validierungen erfolgreich sind.
            } else {
                ladeDialog.show();
                String gültigeEmail = emailEingabeFeld.getText().toString();
                String gültigesPasswort = passwortEingabeFeld.getText().toString();
                // Die Daten können nun an eine Datenbank gesendet werden.

                // Ausstehende Implementierung des Anmeldeverfahrens
                Log.i("Erfolgreich", "Die Registrierungdaten sollen an die Datenbank gesendet werden.");
            }
        }

}