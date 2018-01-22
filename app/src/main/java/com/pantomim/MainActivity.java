package com.pantomim;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import org.webrtc.MediaConstraints;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoCapturerAndroid;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoRendererGui;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private List<Chat> chats = new ArrayList<Chat>();
    private List<User> users = new ArrayList<User>();
    private User myUser;
    private UserAdapter userAdapter;
    private ChatAdapter chatAdapter;
    private boolean imHost = true;
    private boolean isVoice = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GLSurfaceView videoView = (GLSurfaceView) findViewById(R.id.video);
        RecyclerView rChat = (RecyclerView) findViewById(R.id.chats);
        RecyclerView rUser = (RecyclerView) findViewById(R.id.users);
        ImageView send = (ImageView) findViewById(R.id.send);
        final ImageView microphone = (ImageView) findViewById(R.id.voice);
        final EditText edit = (EditText) findViewById(R.id.text);


        userAdapter = new UserAdapter(users,imHost);
        chatAdapter = new ChatAdapter(chats);
        final LinearLayoutManager mManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        rUser.setAdapter(userAdapter);
        rUser.setLayoutManager( mManager);
        rChat.setAdapter(chatAdapter);
        rChat.setLayoutManager(new LinearLayoutManager(this));

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit.getText().length()!=0) {
                    myUser.sendMessage(edit.getText().toString(), chats);
                    edit.setText("");
                }
            }
        });
        microphone.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                myUser.microOn();
                isVoice = true;
                return false;
            }
        });
        microphone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.onTouchEvent(event);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    myUser.microOff();
                    if (isVoice)
                        isVoice = false;
                }
                return false;
            }
        });

        PeerConnectionFactory.initializeAndroidGlobals(
                this,
                true,
                true,
                true,
                true);
        PeerConnectionFactory peerConnectionFactory = new PeerConnectionFactory();

        VideoCapturer capturer =VideoCapturerAndroid.create(VideoCapturerAndroid.getNameOfFrontFacingDevice());

        VideoSource videoSource =
                peerConnectionFactory.createVideoSource(capturer, new MediaConstraints());
        VideoTrack localVideoTrack =
                peerConnectionFactory.createVideoTrack("test", videoSource);

        videoView.setPreserveEGLContextOnPause(true);
        videoView.setKeepScreenOn(true);

        VideoRendererGui.setView(videoView, new VideoReadyRunnable());
        try {
            if(imHost) {
                VideoRenderer renderer = VideoRendererGui.createGui(0, 0, 100, 100, VideoRendererGui.ScalingType.SCALE_ASPECT_FIT, true);
                localVideoTrack.addRenderer(renderer);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        //FOR ADDING MY USER
        myUser = new User("ADMIN",1,12,chatAdapter);
        users.add(myUser);
        userAdapter.notifyDataSetChanged();
        //
        generateUser();

    }

    class VideoReadyRunnable implements Runnable {

        @Override
        public void run() {
            Log.d("ready",VideoCapturerAndroid.getNameOfFrontFacingDevice());
        }

    }

    private void addUser(User user){
        users.add(user);
        userAdapter.notifyDataSetChanged();
    }
    //for testing
    public void generateUser(){
        User user = myUser = new User("MILAD",1,15,chatAdapter);
        addUser(user);
        user = myUser = new User("ARYA",1,12,chatAdapter);
        addUser(user);
        user = myUser = new User("KIA",1,16,chatAdapter);
        addUser(user);
    }
}
