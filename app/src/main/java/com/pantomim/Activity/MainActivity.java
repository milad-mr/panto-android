package com.pantomim.Activity;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.pantomim.Adapter.ChatAdapter;
import com.pantomim.Adapter.UserAdapter;
import com.pantomim.DataManager;
import com.pantomim.Dialog.StartDialog;
import com.pantomim.Interface.ChooseInterface;
import com.pantomim.Interface.WinnerInterface;
import com.pantomim.Model.Chat;
import com.pantomim.Model.Player;
import com.pantomim.Model.User;
import com.pantomim.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoCapturerAndroid;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoRendererGui;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pantomim.ServerManager.getInterface;

//import io.socket.
//import java.net.Socket;


public class MainActivity extends AppCompatActivity implements ChooseInterface,WinnerInterface {
    private List<Chat> chats = new ArrayList<Chat>();
    private List<User> users = new ArrayList<User>();
    private User myUser;
    private  Player myPlayerType;
    private UserAdapter userAdapter;
    private ChatAdapter chatAdapter;
    private boolean imHost = false;
    private boolean isVoice = false;
    private ProgressBar progressBar;
    private int timerSec;
    private String ownerName;
    private String id;

    private Activity activity = this;
    private StartDialog dFragment = new StartDialog();

    private Socket socket;
    //web socket milad variable
    private PeerConnection peerConnection;
    VideoTrack remoteVideoTrack;
    MediaStream localVideoStream;
    MediaStream localAudioStream;

    MediaConstraints videoConst;
    MediaConstraints clientVideoConst;
    MediaConstraints sendOnlyConstraint;
    MediaConstraints recieveOnlyConstraint;

    VideoRenderer renderer ;


    private  boolean gameStarted = false;
    {
        try {
            socket = IO.socket(DataManager.getBaseUrl() + "/WSchannel");
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //java.net.Socket()
        RecyclerView rChat = (RecyclerView) findViewById(R.id.chats);
        RecyclerView rUser = (RecyclerView) findViewById(R.id.users);
        progressBar = (ProgressBar) findViewById(R.id.timer);
        ImageView send = (ImageView) findViewById(R.id.send);
        final ImageView microphone = (ImageView) findViewById(R.id.voice);
        final EditText edit = (EditText) findViewById(R.id.text);
        Intent intent = getIntent();
        //getRequest();
        ownerName = intent.getStringExtra("owner_name");
        socket.connect();
        Log.e("owneeeerrrrrrrrrrrrrrr", ownerName);
        id = intent.getStringExtra("id");
        if (ownerName.equals(DataManager.getUsername(this)))
            imHost = true;
        else{
            String pType = intent.getStringExtra("player_type");
            Log.e("player type", pType);
            if(pType.equals("client1")) myPlayerType = Player.client1;
            if(pType.equals("client2")) myPlayerType = Player.client2;
            if(pType.equals("host")) myPlayerType = Player.host;
        }
        userAdapter = new UserAdapter(users, imHost, this);
        chatAdapter = new ChatAdapter(chats);
        final LinearLayoutManager mManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rUser.setAdapter(userAdapter);
        rUser.setLayoutManager(mManager);
        rChat.setAdapter(chatAdapter);
        rChat.setLayoutManager(new LinearLayoutManager(this));
        //FOR ADDING MY USER
        myUser = new User(DataManager.getUsername(this), DataManager.getScore(this), chatAdapter,null);
        users.add(myUser);
        userAdapter.notifyDataSetChanged();
        //
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit.getText().length() != 0) {
                    broadcast("guess", edit.getText().toString());
                   // myUser.addMessage(edit.getText().toString(), chats, );
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

        //MILAD ***************************************************************************************
        //sockettttttttttt
        JSONObject data = new JSONObject();
        try {
            data.put("game id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.connect();

        socket.emit("join", data);

        //initialize valuesss


        //ice servers
        ArrayList<PeerConnection.IceServer> iceServers = new ArrayList<>();
        iceServers.add(new PeerConnection.IceServer("stun:stun.l.google.com:19302"));
        iceServers.add(new PeerConnection.IceServer("turn:turn.salar.click:3478?transport=udp", "salar", "KoalaTeam"));
            //iceServers = new List<PeerConnection.IceServer>();
        iceServers.add(new PeerConnection.IceServer("stun:stun01.sipphone.com"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.ekiga.net"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.fwdnet.net"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.ideasip.com"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.iptel.org"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.rixtelecom.se"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.schlund.de"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.l.google.com:19302"));
        iceServers.add(new PeerConnection.IceServer("stun:stun1.l.google.com:19302"));
        iceServers.add(new PeerConnection.IceServer("stun:stun2.l.google.com:19302"));
        iceServers.add(new PeerConnection.IceServer("stun:stun3.l.google.com:19302"));
        iceServers.add(new PeerConnection.IceServer("stun:stun4.l.google.com:19302"));
        iceServers.add(new PeerConnection.IceServer("stun:stunserver.org"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.softjoys.com"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.voiparound.com"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.voipbuster.com"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.voipstunt.com"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.voxgratia.org"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.xten.com"));

        //media constraints

        GLSurfaceView videoView = (GLSurfaceView) findViewById(R.id.video);
        videoView.setPreserveEGLContextOnPause(true);
        videoView.setKeepScreenOn(true);

        MediaConstraints videoConstraints = new MediaConstraints();
        videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair("maxHeight", "300"));
        videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair("maxWidth", "300"));
        videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair("maxFrameRate", "20"));
        videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair("minFrameRate", "5"));


        sendOnlyConstraint = new MediaConstraints();
        sendOnlyConstraint.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "false"));
        sendOnlyConstraint.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));

        recieveOnlyConstraint = new MediaConstraints();
        recieveOnlyConstraint.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
        recieveOnlyConstraint.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));

        videoConst = new MediaConstraints();
       // videoConst.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
        //videoConst.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
       // pcConstraints.optional.add(new MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"));
        //videoConst.mandatory.add(new MediaConstraints.KeyValuePair("video", "true"));


        PeerConnectionFactory.initializeAndroidGlobals(
                this,
                true,
                true,
                true,
                true);
        PeerConnectionFactory peerConnectionFactory = new PeerConnectionFactory();
        //
        //List<PeerConnection.IceServer> iceServers = List<PeerConnection.IceServer>();
      //  peerConnectionFactory.createPeerConnection()
       // peerConnectionFactory.

        //sakhte video sourco in dastana
        VideoRendererGui.setView(videoView, new VideoReadyRunnable());
        try {
            renderer = VideoRendererGui.createGui(0, 0, 100, 100, VideoRendererGui.ScalingType.SCALE_ASPECT_FIT, true);
        } catch (Exception e) {
            Log.e("renderer", "failed");
            e.printStackTrace();
        }
       // VideoRendererGui.

        if(imHost){
            VideoCapturer capturer = VideoCapturerAndroid.create(VideoCapturerAndroid.getNameOfFrontFacingDevice());
            VideoSource videoSource = peerConnectionFactory.createVideoSource(capturer, videoConstraints);
            VideoTrack localVideoTrack = peerConnectionFactory.createVideoTrack("hostTrack", videoSource);
            localVideoTrack.addRenderer(renderer);// todo: in bara teste ke bebinim mishe ba host mese client bar khord kard ya na hatman bayad avaz she
            localVideoStream = peerConnectionFactory.createLocalMediaStream("local_video_stream");
            localVideoStream.addTrack(localVideoTrack);


        }else{

            localAudioStream = peerConnectionFactory.createLocalMediaStream("local_video_stream");
            AudioSource audioSource = peerConnectionFactory.createAudioSource(new MediaConstraints());
            AudioTrack localAudioTrack = peerConnectionFactory.createAudioTrack("AUDIO_TRACK_ID", audioSource);
            Log.e("stream", "dar else e aval");
            localAudioStream.addTrack(localAudioTrack);
            //TODO agar host nist bayad video bezane
           // localVideoStream = peerConnectionFactory.createLocalMediaStream("local_video_stream");

        }


        //clientVideoConst.mandatory.add(new MediaConstraints.KeyValuePair)
        clientVideoConst = new MediaConstraints();
        clientVideoConst.mandatory.add(new MediaConstraints.KeyValuePair("offerToReceiveAudio", "false"));
        clientVideoConst.mandatory.add(new MediaConstraints.KeyValuePair("offerToReceiveVideo", "true"));
        clientVideoConst.optional.add(new MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"));
        MediaConstraints peerConst;
        if (!imHost){
            peerConst = recieveOnlyConstraint;
        }
        else {
            peerConst = sendOnlyConstraint;
        }
             peerConnection = peerConnectionFactory.createPeerConnection(iceServers, peerConst, new PeerConnection.Observer() { //todo :in client const bara teste
            @Override
            public void onSignalingChange(PeerConnection.SignalingState signalingState) {
               // Log.e("ice state change", iceConnectionState.toString());
            }

            @Override
            public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
                Log.e("ice state change", iceConnectionState.toString());
            }

            @Override
            public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
                Log.e("ice gathering change", iceGatheringState.toString());
            }

            @Override
            public void onIceCandidate(IceCandidate iceCandidate) {
                //todo condidate haye ice i ke amade misharo bayad befreste bara hame.
                Log.e("ice amade shod", iceCandidate.toString());
              //  peerConnection.a
                broadcast("ice", users.get(1).getName(), iceCandidate.sdpMid, iceCandidate.sdpMLineIndex, iceCandidate.sdp);
                Log.e("ice sent to ", users.get(1).getName());
             //Broadcast("ice", null, iceCandidate);
            }

            @Override
            public void onAddStream(MediaStream mediaStream) {
                Log.e("on add stream stream", mediaStream.toString());
               if (!imHost) {

                    //mediaStream.videoTracks.get(0).addRenderer(renderer);
                    remoteVideoTrack = mediaStream.videoTracks.get(0);
                    remoteVideoTrack.setEnabled(true);
                    remoteVideoTrack.addRenderer(renderer);
                }
               // mediaStream.dispose();
            }

            @Override
            public void onRemoveStream(MediaStream mediaStream) {


            }

            @Override
            public void onDataChannel(DataChannel dataChannel) {

            }

            @Override
            public void onRenegotiationNeeded() {

            }
        });
        /*if(!imHost){
            peerConnection.addStream(localAudioStream);
        }else{

        }*/

//        peerConnection.addStream(localVideoStream);
        //IceCandidate ice = new IceCandidate()
        // end of amozeshe 1
        //TODO socket bezane o condidate begire va addIceCondidate kone
        Log.e("state", "after host if user type is:" + myUser.getType());
        socket.on("broadcast", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                JSONObject data = (JSONObject) args[0];
                Log.e("broadcast", "new broadcast arrived data is :" + data);
                String type = null;
                String source = null;
                String dest = null;

                try {
                    type = data.getString("type");
                    source = data.getString("source");
                    dest = data.getString("dest");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (type.equals("start_game")) {
                    getRequest();
//                    if(imHost){ todo :shayad bug bokhore
//                        hostStart();
//                    }
                }else if (type.equals("guess")){
                  //  myUser.sendMessage(edit.getText().toString(), chats); todo :in kar nemikone ke
                     String msg = "";
                    try {
                        msg = data.getString("msg");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final String src = source;
                        final String msg2 = msg; //wtfffff this language is fuuuun:)
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myUser.sendMessage(msg2, chats, src);
                            }
                        });


                    Log.e("guess", "hads jadid umad");
                   // adapter.notifyDataSetChanged();
                }

                if (type.equals("sdp") && dest.equals(myUser.getName())){
                    SessionDescription sdp = null;
                    Log.e("sdp", "sdp found");
                    try {

                        sdp = new SessionDescription(SessionDescription.Type.fromCanonicalForm(data.getString("sdp_type")), data.getString("sdp_desc"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("sdp", "sdp ke umade :" + sdp);
                    peerConnection.setRemoteDescription(new SdpObserver() {
                        @Override
                        public void onCreateSuccess(SessionDescription sessionDescription) {

                        }

                        @Override
                        public void onSetSuccess() {
                            Log.e("sdp", "remote sdp set succeed");
                            if(!imHost) {
                                Log.e("sdp", "sdp commed in !imhost");
                                MediaConstraints answerConstraints = new MediaConstraints();
                                //answerConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "false"));
                                answerConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
                               // peerConnection.createAnswer(new AnswerObserver(), answerConstraints);
                                peerConnection.createAnswer(new SdpObserver() {
                                    @Override
                                    public void onCreateSuccess(SessionDescription sessionDescription) {
                                        Log.e("answer create success ",sessionDescription.type.toString() + sessionDescription.description);
                                        broadcast("sdp", ownerName, sessionDescription.type.canonicalForm(), sessionDescription.description);
                                        peerConnection.setLocalDescription(new SdpObserver() {
                                            @Override
                                            public void onCreateSuccess(SessionDescription sessionDescription) {

                                            }

                                            @Override
                                            public void onSetSuccess() {
                                                Log.e("sdp in client ", "set success");
                                            }

                                            @Override
                                            public void onCreateFailure(String s) {

                                            }

                                            @Override
                                            public void onSetFailure(String s) {
                                                Log.e("sdp in client failed ", s);

                                            }
                                        }, sessionDescription);
                                       // peerConnection.addStream(localAudioStream); //todo : for test
                                    }

                                    @Override
                                    public void onSetSuccess() {

                                    }

                                    @Override
                                    public void onCreateFailure(String s) {
                                            Log.e("answer create failed ",s);
                                    }

                                    @Override
                                    public void onSetFailure(String s) {

                                    }
                                }, recieveOnlyConstraint);//todo :inja fek nakonam ke bayad client const bashe
                            }
                        }


                        @Override
                        public void onCreateFailure(String s) {

                        }

                        @Override
                        public void onSetFailure(String s) {
                            Log.e("sdp", s);
                        }
                    }, sdp);

                    //todo sdp message comes
                }else if (type.equals("ice") && dest.equals(myUser.getName())){
                    Log.e("ice umade", data.toString());
                    IceCandidate newIce = null;
                    try {
                        newIce = new IceCandidate(data.getString("sdp_mid"), data.getInt("sdp_index"), data.getString("sdp_desc"));
                    } catch (JSONException e) {
                        Log.e("new ice creatin failed ", "in broad cast");
                        e.printStackTrace();
                    }

                    peerConnection.addIceCandidate(newIce);
                    Log.e("new ice gozashte shod", newIce.toString());
                   // Lo
                    //todo ice message comes
                }
            }
        });
      //  Log.e("start", myPlayerType.name());
        if(myPlayerType == Player.client1){
            Log.e("start", "man playere1 hasta,");
            broadcast("start_game", null, null, null);
        }
       // }
        //PeerConnection.Observer ob = new PeerConnection.Observer();


        //end webRTC mil ///////******************


        setStartMode();

        //addUser("t2",Player.client1);
        //addUser("t3",Player.client2);


    }
    //IceCandidate a = new IceCandidate()
    void broadcast(String type, String msg) {
        JSONObject json = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("type", type);
            data.put("msg", msg);
            data.put("source", myUser.getName());
            data.put("dest", "all");
            json.put("game id", id);
            json.put("data", data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("broadcast", json);
    }


    void broadcast(String type, String dest, String sdp_mid, int sdp_index, String sdp_desc)  { // for ice condidate
        JSONObject json = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("type", type);
            data.put("source", myUser.getName());
            data.put("dest", dest);
            data.put("sdp_mid", sdp_mid);
            data.put("sdp_index", sdp_index);
            data.put("sdp_desc", sdp_desc);
            json.put("game id", id);
            json.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("broadcast", "tu except");
        }
        Log.e("broadcast", "ghable emit");
        socket.emit("broadcast", json );
        Log.e("broadcast", "bade emit");
    }
    void broadcast(String type, String dest, String sdp_type,String sdp_desc)  {
        JSONObject json = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("type", type);
            data.put("source", myUser.getName());
            data.put("dest", dest);
            data.put("sdp_type", sdp_type);
            data.put("sdp_desc", sdp_desc);
            json.put("game id", id);
            json.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("broadcast", "tu except");
        }
        Log.e("broadcast", "ghable emit");
        socket.emit("broadcast", json );
        Log.e("broadcast", "bade emit");
    }

    void hostStart(){
        Log.e("peer", peerConnection.toString());
        Log.e("stream", localVideoStream.toString());
              peerConnection.addStream(localVideoStream); // video ro mikobone be peerconnection
            Log.e("Imhost", "host name is:" + ownerName +  "my name is: " + myUser.getName() );
           // peerConnection.c
            peerConnection.createOffer(new SdpObserver() {
                @Override
                public void onCreateSuccess(final SessionDescription sessionDescription) {
                    Log.e("offer success " , sessionDescription.type.toString() + sessionDescription.description);
                    broadcast("sdp", users.get(1).getName(), sessionDescription.type.canonicalForm() ,sessionDescription.description);
                    peerConnection.setLocalDescription(new SdpObserver() {
                        @Override
                        public void onCreateSuccess(SessionDescription sessionDescription) {
                        }

                        @Override
                        public void onSetSuccess() {
                           // Log.e("local offer", "offer set on host");
                            Log.e("hostset sending to ", users.get(1).getName());
                           // broadcast("sdp", users.get(1).getName(), sessionDescription.type.canonicalForm() ,sessionDescription.description);
                            //  SessionDescription mysdp = new SessionDescription();
                            //socket.emit("broadcast", data);
                            Log.e("socket", "my sdp offered");

                        }
                        //todo : shayad bayad inja jsono befrestim na bad az create .
                        //Broadcast("sdp", null, sessionDescription.type);
                        @Override
                        public void onCreateFailure(String s) {

                        }

                        @Override
                        public void onSetFailure(String s) {

                        }
                    },sessionDescription); //todo : inja shayad bayad offer ha  biad

                }

                @Override
                public void onSetSuccess() {

                }

                @Override
                public void onCreateFailure(String s) {
                    Log.e("offer", "create offered failed");
                }

                @Override
                public void onSetFailure(String s) {

                }
            }, sendOnlyConstraint);

        }




    class VideoReadyRunnable implements Runnable {

        @Override
        public void run() {
            Log.d("ready", VideoCapturerAndroid.getNameOfFrontFacingDevice());
        }

    }

    //for testing

    public void resetTimer() {
        timerSec = 0;
        CountDownTimer mCountDownTimer = new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timerSec++;
                progressBar.setProgress(timerSec);

            }

            @Override
            public void onFinish() {
                timerSec++;
                progressBar.setProgress(60);
                timerEnd();
            }
        };
        mCountDownTimer.start();
    }

    public void timerEnd() {
        //TODO: MILAD : vaghti timer tamom shod ino var boro
    }

    @Override
    public void choose(String text) {
        resetTimer();
        showWord(text);
    }

    public void addUser(String name,Player type){
        users.add(new User(name,0,chatAdapter,type));
        userAdapter.notifyDataSetChanged();
        if(users.size()==3)
            startGame();

    }

    public void startGame(){
        dFragment.start(this);
        resetTimer();
    }


    //show Word to another
    public void showWord(String text) {
        //TODO: neshan dadan e loghat
        if (imHost)
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    //For starting
    public void setStartMode() {
        dFragment = new StartDialog();
        dFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        dFragment.show(getSupportFragmentManager(), null);
    }


    //selecting winner
    @Override
    public void selectWinner(String username) {
        //add score to user
        //TODO : host fahmide k in username barandas
        Toast.makeText(activity,"user "+username+" wins",Toast.LENGTH_LONG).show();
        resetTimer();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setNewScore(myUser.getScore());
        deleteRequest();
        deleteBroadCast();
    }

    public void deleteBroadCast(){
        //TODO: be baghie begoo k man bastam
    }

    public void setNewScore(int score) {
        int mScore = DataManager.getScore(this);
        DataManager.setScore(this, mScore + score);

    }

    public void getRequest() {
        Call<JsonObject> call = getInterface().getGame(id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    String client = null;
                    if(imHost)
                        myUser.setType(Player.host);
                    else{
                        client = response.body().get("host").getAsString();
                        addUser(client, Player.client1);
                    }
                    if(response.body().has("client1")) {
                        client = response.body().get("client1").getAsString();
                        if(client.equals(DataManager.getUsername(activity)))
                            myUser.setType(Player.client1);
                        else
                           addUser(client, Player.client1);
                    }
                    if(response.body().has("client2")){
                        client = response.body().get("client2").getAsString();
                        if(client.equals(DataManager.getUsername(activity)))
                            myUser.setType(Player.client2);
                        else
                            addUser(client, Player.client2);
                    }
                    if(imHost){
                        hostStart();
                    }

                } else
                    Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
    public void deleteRequest(){
        Call<JsonObject> call = getInterface().deleteGame(id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
