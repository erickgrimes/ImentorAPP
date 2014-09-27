package com.example.imapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.quickblox.core.QBCallback;
import com.quickblox.core.QBCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.core.result.Result;
import com.quickblox.internal.core.request.QBPagedRequestBuilder;
import com.quickblox.module.auth.QBAuth;
import com.quickblox.module.auth.result.QBSessionResult;
import com.quickblox.module.chat.QBChatRoom;
import com.quickblox.module.chat.QBChatService;
import com.quickblox.module.chat.listeners.ChatMessageListener;
import com.quickblox.module.chat.listeners.RoomListener;
import com.quickblox.module.chat.listeners.SessionCallback;
import com.quickblox.module.chat.smack.SmackAndroid;
import com.quickblox.module.chat.xmpp.QBPrivateChat;
import com.quickblox.module.content.QBContent;
import com.quickblox.module.content.result.QBFilePagedResult;
import com.quickblox.module.users.QBUsers;
import com.quickblox.module.users.model.QBUser;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;


public class MainActivity extends Activity {

    SharedPreferences loginpreferences;
    SharedPreferences.Editor loginPrefsEditor;
    QBChatRoom room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);



        SmackAndroid.init(this);
        QBSettings.getInstance().fastConfigInit("14818","6zTZ8BF4RXVhpNK","SK4GwLAffgDSTd3");
        QBUser user = new QBUser();
        user.setId(1629963);
        user.setPassword("password");
        final QBUser user1 = user;
        QBAuth.createSession(user,new QBCallbackImpl(){
            @Override
            public void onComplete(Result result) {
                if (result.isSuccess()) {
                    QBSessionResult res = (QBSessionResult)result;
                    user1.setId(res.getSession().getUserId());
                    //
                    QBChatService.getInstance().loginWithUser(user1, new SessionCallback() {
                        @Override
                        public void onLoginSuccess() {
                            Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onLoginError(String s) {
                            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                }
            }
        });
        // Create 1-1 chat
        final QBPrivateChat chat = QBChatService.getInstance().createChat();
        chat.addChatMessageListener(new ChatMessageListener() {
            @Override
            public void processMessage(Message message) {
            }

            @Override
            public boolean accept(Message.Type type) {
                switch (1) {
                    case 1:
                        return true; // process 1-1 chat messages
                    default:
                        return false;
                }
            }
        });

// send message
        try {
            chat.sendMessage(1630100, "Hi mate!");
        } catch (XMPPException e) {
            e.printStackTrace();
        }

//        setChat();
//        loginpreferences = this.getSharedPreferences("ichat", this.MODE_PRIVATE);
//        autoSignIn();
//        signUp();
//        joinRoom();
//        try {
//            room.sendMessage("THIS IS A TEST!");
//        } catch (XMPPException e) {
//            e.printStackTrace();
//        }
    }

    private void authorizeApp() {
        QBUser qbUser = new QBUser("username", "password");
        // authorize app with default user
        QBAuth.createSession(qbUser, new QBCallback() {
            @Override
            public void onComplete(Result result) {
                if (result.isSuccess()) {
                    // return result from QBAuth.authorizeApp() query
                    QBSessionResult qbSessionResult = (QBSessionResult) result;
                    DataHolder.getDataHolder().setSignInUserId(qbSessionResult.getSession().getUserId());
                    // retrieve user's files
                    getFileList();
                } else {

                }
            }

            @Override
            public void onComplete(Result result, Object o) {
            }
        });
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

    private void getFileList() {
        // ================= QuickBlox ===== Step 2 =================
        // Gey all user's files
        QBPagedRequestBuilder builder = new QBPagedRequestBuilder();
        builder.setPerPage(30);
        builder.setPage(1);
        QBContent.getFiles(builder, new QBCallback() {
            @Override
            public void onComplete(Result result) {
                QBFilePagedResult qbFilePagedResult = (QBFilePagedResult) result;
                DataHolder.getDataHolder().setQbFileList(qbFilePagedResult.getFiles());
                // show activity_gallery
                Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_SHORT).show();
            //    startGalleryActivity();
            }

            @Override
            public void onComplete(Result result, Object o) {
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
