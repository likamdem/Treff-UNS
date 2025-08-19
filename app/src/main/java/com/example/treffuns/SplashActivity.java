package com.example.treffuns;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- Kernlogik für den Timer ---

        // Erstellt einen neuen Handler, um eine Aktion mit einer Verzögerung auszuführen.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Startet eine neue Aktivität (hier die LoginActivity). Dies ist der
                // Übergang vom Splash-Screen zum nächsten Bildschirm.
                startActivity(new Intent(SplashActivity.this, AnmeldenActivity.class));

                // Beendet die aktuelle Aktivität (SplashActivity). Das ist wichtig,
                // damit der Benutzer nicht über die Zurück-Taste zum Splash-Screen zurückkehren kann.
                finish();
            }
        }, 3000); // Legt die Verzögerung auf 3000 Millisekunden (3 Sekunden) fest.
    }
}