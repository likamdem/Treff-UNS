package com.example.treffuns;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class AnmeldenActivity extends AppCompatActivity {


// Reguläre Ausdrücke zur Validierung von E-Mail und Passwort
    private final String E_MAIL_REGEX = "^[a-zA-Z0-9._%+-]+@(studmail\\.w-hs\\.de|[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$";
    private final String PASSWORT_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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

            String gültigeEmail = emailFeld.getText().toString();
            String gültigesPasswort = passwortFeld.getText().toString();

            // Ausstehende Implementierung des Anmeldeverfahrens
            Log.i("Erfolgreich", "Die Anmeldedaten sollen später mit einer Datenbank überprüft werden.");
        }
    }
}