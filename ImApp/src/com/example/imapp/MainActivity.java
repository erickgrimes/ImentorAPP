package cfg.example.org.cfg;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.quickblox.core.QBCallback;
import com.quickblox.core.QBCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.core.result.Result;
import com.quickblox.module.auth.QBAuth;
import com.quickblox.module.users.QBUsers;
import com.quickblox.module.users.model.QBUser;


public class MyActivity extends Activity {

    SharedPreferences loginpreferences;
    SharedPreferences.Editor loginPrefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        setChat();
        loginpreferences = this.getSharedPreferences("ichat", this.MODE_PRIVATE);
        autoSignIn();
        signUp();
    }

    public void signUp(){
        loginPrefsEditor=loginpreferences.edit();
        //PROMPT USER FOR USERNAME AND PASSWORD wiTH DiALOG
        //THIS SHOULD OPEN A DIALOG THAT HANDLES THE REST OF THE LOGIC
        loginPrefsEditor.putString("password","password");
        loginPrefsEditor.putString("username","username");
    }

    public void autoSignIn(){
        if(loginpreferences.getBoolean("haslogin",false)){
            login(loginpreferences.getString("username",""),loginpreferences.getString("password",""));
        }
    }



    private void login(String username, String password) {
        //Create login boxes etc.
        QBUsers.signIn("test1","test1pass",new QBCallback() {
            @Override
            public void onComplete(Result result) {
                if(result.isSuccess())
                    Toast.makeText(getApplicationContext(),"Signin Success",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(),"Signin Failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(Result result, Object o) {
                if(result.isSuccess())
                    Toast.makeText(getApplicationContext(),"Sigin Success",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(),"Signin Failed",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setChat(){
        QBSettings.getInstance().fastConfigInit("14818","6zTZ8BF4RXVhpNK","SK4GwLAffgDSTd3");
        QBAuth.createSession(new QBCallback() {
            @Override
            public void onComplete(Result result) {
                if(result.isSuccess())
                    Toast.makeText(getApplicationContext(),"Session Created",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(),"Session Failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(Result result, Object o) {
                if(result.isSuccess())
                    Toast.makeText(getApplicationContext(),"Session Created",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(),"Session Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
