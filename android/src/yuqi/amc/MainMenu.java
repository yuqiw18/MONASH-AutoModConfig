package yuqi.amc;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity implements OnNavigationItemSelectedListener, OnClickListener {

    private TextView navUsername;
    private TextView navUserEmail;
    private ImageView navUserImg;
    private SharedPreferences sharedPreferences;
    private NavigationView navigationView;
    private Menu navigationMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navHeader = navigationView.getHeaderView(0);
        navUsername = (TextView)navHeader.findViewById(R.id.navTextName);
        navUserEmail = (TextView)navHeader.findViewById(R.id.navTextEmail);
        navUserImg = (ImageView)navHeader.findViewById(R.id.navImgUser);

        navigationView.setNavigationItemSelectedListener(this);
        navigationMenu = navigationView.getMenu();
        // Set the Explorer as checked
        navigationMenu.findItem(R.id.nav_explorer).setChecked(true);



        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        loadPreference();

        getSupportActionBar().setTitle(getString(R.string.app_name));

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new ExplorerFragment()).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //moveTaskToBack(true);
            int count = getFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                super.onBackPressed();
            } else {
                getFragmentManager().popBackStack();
            }
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment nextFragment = null;
        if (id == R.id.nav_explorer) {
            nextFragment = new ExplorerFragment();

        } else if (id == R.id.nav_configurator) {
            startActivity(new Intent(this, ConfiguratorBrand.class));

        } else if (id == R.id.nav_account) {
            nextFragment = new AccountFragment();

        } else if (id == R.id.nav_orders) {
            nextFragment = new OrderListFragment();

        } else if (id == R.id.nav_signout){
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_title_confirmation))
                    .setMessage(getString(R.string.dialog_msg_logout))
                    .setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int selection) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isSignedIn", false);
                            editor.commit();
                            Intent intent = getIntent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            finish();
                            startActivity(intent);
                            overridePendingTransition(0,0);
                        }

                    }).setNegativeButton(getString(R.string.dialog_no), null).show();
        }

        if (nextFragment != null){
            navigationView.setCheckedItem(id);
            getFragmentManager().beginTransaction().replace(R.id.content_frame, nextFragment).addToBackStack(null).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.navTextName){
            if (!sharedPreferences.getBoolean("isSignedIn", false)){
                startActivity(new Intent(this, Login.class));
            }
        }
    }

    private void loadPreference(){
        //Load the values using SharedPreference
        if (sharedPreferences.getBoolean("isSignedIn", false)){
            navUsername.setText(sharedPreferences.getString("name", null));
            navUserEmail.setText(sharedPreferences.getString("email", null));
            navigationMenu.findItem(R.id.nav_account).setVisible(true);
        }else {
            //If failed loading the value, which means no saved user information, set default String value: "TAP TO SIGN IN"
            navUsername.setText(getString(R.string.ui_main_tap_to_login));
            navUserEmail.setText(null);
            navigationMenu.findItem(R.id.nav_account).setVisible(false);
        }
    }
}
