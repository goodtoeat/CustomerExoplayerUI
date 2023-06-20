package util.hc.exoplayer.ui.demo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.ui.StyledPlayerView;

public class PlayerActivity extends AppCompatActivity {

    Context mContext;
    ExoPlayer player;
    DefaultLoadControl dlc;
    StyledPlayerView playerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        mContext = this;
        init();
        String url = "https://storage.googleapis.com/wvmedia/clear/h264/tears/tears.mpd";
        setPlayView(url);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void init() {
        dlc = new DefaultLoadControl.Builder()
                .setBufferDurationsMs(
                        30000,
                        90000,
                        30000,
                        30000
                ).build();
        playerView = findViewById(R.id.player_view);
        setViews();
    }

    public void setViews() {
        player = new ExoPlayer.Builder(mContext)
                .setLoadControl(dlc)
                .setMediaSourceFactory(new DefaultMediaSourceFactory(mContext))
                .build();
        playerView.setPlayer(player);
        playerView.setControllerShowTimeoutMs(10000);

        //Video title field
        playerView.setTitleText("Demo_Video_Title");
        //Customized buttons that can define whether to display and their corresponding
        //click actions, with the default action being "last seen"
        playerView.setLastSeen("Last seen at 00:05:00",
                () -> {
                    player.seekTo(5*60*1000);
                    playerView.setLastWatchShowing(false);
                });
        playerView.setLastWatchShowing(true);

        /**
         * This listener is used to customize and define the behavior of Dpad buttons, under the condition that the controller is hidden.
         * Return values of CustomerDpadKeyListener method:
         * return true: The method execution ends immediately, for example dpadUp().
         * return false: After executing the method, ExoPlayer will continue with its default behavior.
         */
        playerView.setDpadKeyListener(new StyledPlayerView.CustomerDpadKeyListener() {
            @Override
            public boolean dpadUp() {
                //When the controller is not displayed, clicking dpadDown triggers an action,
                //for example, it can increase the volume.
                Toast.makeText(PlayerActivity.this, "volume increase", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean dpadUpRight() {
                return false;
            }

            @Override
            public boolean dpadRight() {
                return false;
            }

            @Override
            public boolean dpadDownRight() {
                return false;
            }

            @Override
            public boolean dpadDown() {
                //When the controller is not displayed, clicking dpadUp triggers an action,
                //for example, it can decrease the volume.
                Toast.makeText(PlayerActivity.this, "volume decrease", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean dpadDownLeft() {
                return false;
            }

            @Override
            public boolean dpadLeft() {
                return false;
            }

            @Override
            public boolean dpadUpLeft() {
                return false;
            }

            @Override
            public boolean dpadCenter() {
                //When the controller is not displayed,
                //clicking dpadCenter directly executes the pause action.
                player.pause();
                return false;
            }
        });

        //Display rewind or fast-forward buttons (default hidden).
        playerView.showRewindAndFastForwardButton();

        //Display next or previous buttons (default hidden).
        playerView.showPreviousAndNextButton();

        //Adjust the time each Dpad (direction key) controls the progress bar.
        playerView.setTimeSkipPerDpadClick(5000);

        ////Automatic start of the video.
        player.setPlayWhenReady(true);

    }

    @Override
    public void onBackPressed() {
        //When the controller is displayed, clicking onBackPressed only hides the controller.
        if (playerView.isControllerFullyVisible()){
            playerView.hideController();
        }else {
            super.onBackPressed();
        }
    }

    public void setPlayView(String url) {
        MediaItem mediaItem = MediaItem.fromUri(url);
        player.seekTo(0);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}