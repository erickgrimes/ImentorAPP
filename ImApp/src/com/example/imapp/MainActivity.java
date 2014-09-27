package com.example.imapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.quickblox.core.QBCallback;
import com.quickblox.core.QBCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.core.result.Result;
import com.quickblox.module.auth.QBAuth;
import com.quickblox.module.auth.model.QBSession;
import com.quickblox.module.auth.result.QBSessionResult;
import com.quickblox.module.chat.QBChatRoom;
import com.quickblox.module.chat.QBChatService;
import com.quickblox.module.chat.listeners.ChatMessageListener;
import com.quickblox.module.chat.listeners.RoomListener;
import com.quickblox.module.chat.listeners.SessionCallback;
import com.quickblox.module.chat.smack.SmackAndroid;
import com.quickblox.module.users.QBUsers;
import com.quickblox.module.users.model.QBUser;

import org.jivesoftware.smack.XMPPException;

public class MainActivity extends Activity {
    SharedPreferences loginpreferences;
    SharedPreferences.Editor loginPrefsEditor;
    QBChatRoom room;
    QBUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        
        SmackAndroid.init(this);
        final QBUser user = new QBUser("username","password");    
        QBAuth.createSession(user, new QBCallbackImpl(){
        	@Override
        	public void onComplete(Result result){
        		if(result.isSuccess()){
        			QBSessionResult res = (QBSessionResult)result;
        			user.setId(res.getSession().getUserId());
        		}
        	}
        });
        QBChatService.getInstance().joinRoom("test", new RoomListener() {
			
			@Override
			public void onJoinedRoom(QBChatRoom arg0) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onError(String arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCreatedRoom(QBChatRoom arg0) {
				// TODO Auto-generated method stub
				
			}
		});
      //  setChat();
      //  loginpreferences = this.getSharedPreferences("ichat", this.MODE_PRIVATE);
       // autoSignIn();
       // signUp();
        //joinRoom();
        //try {
         //   room.sendMessage("THIS IS A TEST!");
        //} catch (XMPPException e) {
         //   e.printStackTrace();
        //}
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

    //unfinished, find appropriate replacement for this
    //doesnt work at all actually, fix much later
//    public void createRoom(){
//        QBChatService.getInstance().createRoom("testname", false/*what*/,true/*what*/, new RoomListener() {
//            @Override
//            public void onCreatedRoom(QBChatRoom qbChatRoom) {
//                qbChatRoom.addMessageListener(this);
//            }
//
//            @Override
//            public void onJoinedRoom(QBChatRoom qbChatRoom) {
//
//            }
//
//            @Override
//            public void onError(String s) {
//
//            }
//        });
//    }

    //Make this modular so it auto-joins appropriate room
    public void joinRoom(){
        room = QBChatService.getInstance().joinRoom("test", new RoomListener() {
            @Override
            public void onCreatedRoom(QBChatRoom qbChatRoom) {
                Log.d("TAG", "Room Created");
                room.setRoomListener(this);
            }

            @Override
            public void onJoinedRoom(QBChatRoom qbChatRoom) {
                room.setRoomListener(this);
            }

            @Override
            public void onError(String s) {

            }
        });
    }


    private void login(String username, String password) {
        //Create login boxes etc.
        QBUsers.signIn(username,password,new QBCallback() {
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
    	SmackAndroid.init(this);
        QBSettings.getInstance().fastConfigInit("14818","6zTZ8BF4RXVhpNK","SK4GwLAffgDSTd3");
        QBAuth.createSession(user, new QBCallback() {
            @Override
            public void onComplete(Result result) {
            	QBSessionResult ress = (QBSessionResult)result;
                if(result.isSuccess())
                    Toast.makeText(getApplicationContext(),"Session Created",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(),"Session Failed",Toast.LENGTH_SHORT).show();
                user.setId(ress.getSession().getUserId());
                QBChatService.getInstance().loginWithUser(user, new SessionCallback(){

					@Override
					public void onLoginError(String arg0) {
						// TODO Auto-generated method stub
						Log.d("LOGINERROR","Login Failed");
					}

					@Override
					public void onLoginSuccess() {
						// TODO Auto-generated method stub
						
					}
                	
                });
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
        getMenuInflater().inflate(R.menu.main, menu);
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
