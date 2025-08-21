package com.example.treffuns.Activities;

import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.treffuns.R;
import com.example.treffuns.fragments.HomeFragment;
import com.example.treffuns.fragments.NeueFragment;
import com.example.treffuns.fragments.ProfilFragment;
import com.example.treffuns.fragments.SucheFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    // UI-Element für die untere Navigationsleiste
    BottomNavigationView bottomNavigationView;

    // Konstanten zur Identifizierung der Fragmente
    public static final int FRAGMENT_HOME = 0;
    public static final int FRAGMENT_SEARCH = 1;
    public static final int FRAGMENT_ADD = 2;
    public static final int FRAGMENT_PROFILE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Setzt das Layout für diese Aktivität auf "activity_home".
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Methode für den Wechsel zu einem Fragment.
    private void moveToFragment(Fragment fragment) {
        // Startet eine Transaktion, um das aktuelle Fragment zu ersetzen.
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    // Wird aufgerufen, wenn ein Element in der unteren Navigationsleiste ausgewählt wird.
    private boolean onNavigationItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_home) {
            replaceFragment(new HomeFragment());
            return true;
        } else if (itemId == R.id.nav_suche) {
            replaceFragment(new SucheFragment());
            return true;
        } else if (itemId == R.id.nav_neues) {
            replaceFragment(new NeueFragment());
            return true;
        }else if (itemId == R.id.nav_profil) {
            replaceFragment(new ProfilFragment());
            return true;
        }
        // Füge weitere Fälle für deine Navigations-Items hinzu
        return false;
    }

    // Eine Hilfsmethode, um ein Fragment im Container zu ersetzen.
    // (Beachte: Diese Methode ist fast identisch mit moveToFragment.)
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        // Optional: Füge die Transaktion zum Back-Stack hinzu, damit der Zurück-Button Fragments wechselt
        // fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // Zeigt ein Fragment basierend auf einem übergebenen Code an.
    private void showFragmentBasedOnIntent(int fragmentCode) {
        switch (fragmentCode) {
            case FRAGMENT_HOME:
                replaceFragment(new HomeFragment());
                // Setzt das ausgewählte Item in der Navigationsleiste.
                bottomNavigationView.setSelectedItemId(R.id.nav_home);
                break;
            case FRAGMENT_SEARCH:
                replaceFragment(new SucheFragment());
                bottomNavigationView.setSelectedItemId(R.id.nav_suche);
                break;
            case FRAGMENT_ADD:
                replaceFragment(new NeueFragment());
                bottomNavigationView.setSelectedItemId(R.id.nav_neues);
                break;
            case FRAGMENT_PROFILE:
                replaceFragment(new ProfilFragment());
                bottomNavigationView.setSelectedItemId(R.id.nav_profil);
                break;
            default:
                // Standardmäßig wird das Home-Fragment angezeigt.
                replaceFragment(new HomeFragment());
                bottomNavigationView.setSelectedItemId(R.id.nav_home);
                break;
        }
    }
}