package com.example.how;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.coremedia.iso.boxes.Container;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;

import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ArrayList<String> Inputarray = new ArrayList<String>();
    String tosplit;
    //implements View.OnClickListener
    String[] splitter;
    //Button save;
    Button Shovo;
    PlayerView playerView;
    SimpleExoPlayer simpleExoPlayer;
    ImageView bgapp, clover,speak,save,list,play;
    Animation bganim, cloveranim,frombottom;
    LinearLayout textsplash,texthome,menus;

    HashMap<String, String> readyforvideo = new HashMap<>();
    int i = 0;
    String path;
    ArrayList<String> paths = new ArrayList<String>();

    ConcatenatingMediaSource concatenatedSource;
    DataSource.Factory datasourcefactory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       persmission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
       persmission(Manifest.permission.READ_EXTERNAL_STORAGE);
         speak=findViewById(R.id.speak);
         play=findViewById(R.id.play);
        // playerView=findViewById(R.id.player_view);
          save=findViewById(R.id.save);

        bgapp = (ImageView) findViewById(R.id.bgapp);
        clover = (ImageView) findViewById(R.id.clover);
        textsplash = (LinearLayout) findViewById(R.id.textsplash);
        texthome = (LinearLayout) findViewById(R.id.texthome);
        menus = (LinearLayout) findViewById(R.id.menus);



        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);


        bganim = AnimationUtils.loadAnimation(this, R.anim.bganim);
        // cloveranim = AnimationUtils.loadAnimation(this, R.anim.cloveranim);

        bgapp.animate().translationY(-3750).setDuration(900).setStartDelay(350);
        clover.animate().alpha(0).setDuration(800).setStartDelay(600);
        textsplash.animate().translationY(140).alpha(0).setDuration(800).setStartDelay(300);
        texthome.startAnimation(frombottom);
        menus.startAnimation(frombottom);

          speak.setOnClickListener(this);
         play.setOnClickListener(this);
         save.setOnClickListener(this);
         simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this);
         datasourcefactory=new DefaultDataSourceFactory(this, Util.getUserAgent(this, "completeproject"));

        persmission(Manifest.permission.CALL_PHONE);
    }





    void persmission (String  permissi)
    {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,permissi
        )
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    permissi)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{permissi},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    public void getspeechInput ()
    {
        Locale locale = new Locale("bn");

        Intent intent=new Intent (RecognizerIntent.ACTION_RECOGNIZE_SPEECH) ;
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,locale.getLanguage());
        if (intent.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(intent,2);

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 2: {
                if (resultCode == RESULT_OK && data!=null)
                {

                    // a=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Inputarray  =data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    tosplit=Inputarray.get(0);
                    splitter=tosplit.split(" ");
                    //show(""+splitter[1]);







                }



            }
        }
    }
    void readythehasmap()
    {
        readyforvideo.put("কে","v.mp4.mp4");
        readyforvideo.put("আমার","b.mp4.mp4");


    }
    void readythepath()

    {    paths.clear();
        path= Environment.getExternalStorageDirectory().getAbsolutePath();
        File pa;

        for (int k=0;k<splitter.length;k++) {
            //show(""+splitter.length);
            if (readyforvideo.get(splitter[k])!=null) {
                paths.add(path + "/" + readyforvideo.get(splitter[k]) + "/");
            }


        }


    }
    void checkforami()
    {
        show("splitter return"+splitter[0]);
        show("return 2"+splitter[1]);

    }


    void readymediasorce()
    {
        // String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"b.mp4.mp4"+"/";

        //  File pat=new File(path);


        MediaSource[] mediaSources;

        mediaSources = new MediaSource[splitter.length];


        for (int j = 0; j < paths.size(); j++) {

            MediaSource media = new ExtractorMediaSource.Factory(datasourcefactory).createMediaSource(Uri.parse(paths.get(j)));

            mediaSources[j] = media;
        }
        concatenatedSource = new ConcatenatingMediaSource(mediaSources);




    }


    void  show(String e)
    {
        AlertDialog.Builder a =new AlertDialog.Builder(this);
        a.setMessage(e);
        a.setCancelable(true);
        a.show();

    }
    void exoplayer()
    {
        simpleExoPlayer.prepare(concatenatedSource);
        playerView.setPlayer(simpleExoPlayer);
        simpleExoPlayer.setPlayWhenReady(true);
    }
    void merger ()  {
        int count=paths.size();
        Movie[] movie=new Movie[count];
        for(int i=0;i<count;i++)
        {
            File file =new File (paths.get(i));
            if(file.exists())
            {
                try {
                    movie[i]= MovieCreator.build(file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        LinkedList<Track> videotrack=new LinkedList<>();
        for(Movie m:movie) {
            for (Track t : m.getTracks()) {
                if (t.getHandler().equals("vide")) {
                    videotrack.add(t);

                }
            }
        }
        Movie result=new Movie();
        if(videotrack.size()>0)
        {

            try {
                result.addTrack(new AppendTrack(videotrack.toArray(new Track[videotrack.size()])));
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        Container out=new DefaultMp4Builder().build(result);
        String pat= Environment.getExternalStorageDirectory().getAbsolutePath();
        long timestamp=new Date().getTime();
        String b="sonamoni"+timestamp+".mp4";

        File storage=new File(pat,b);
        FileChannel filechannel =null;

        try {
            filechannel =new RandomAccessFile(storage,"rw").getChannel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            out.writeContainer(filechannel);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            filechannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.speak)
        {
            i++;
            getspeechInput ();


        }
        if(v.getId()==R.id.play)
        {
            //checkforami();
            readythehasmap();
            readythepath();
            readymediasorce();
            exoplayer();
            show(""+paths.get(0));



            //simpleExoPlayer= simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this);
            //  readymediasorce();
            //  exoplayer();





        }
        if(v.getId()==R.id.save)
        {
            try {
                merger();
            }catch(Exception e)
            {
                show(""+e);
            }
        }

    }

}