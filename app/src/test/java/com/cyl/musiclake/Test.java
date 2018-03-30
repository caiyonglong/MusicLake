package com.cyl.musiclake;

import com.cyl.musiclake.utils.FormatUtil;

/**
 * Author   : D22434
 * version  : 2018/3/20
 * function :
 */

public class Test {
    @org.junit.Test
    public void tt() {
//        for (; ; ) {
        System.out.println("请输入下一个数据（直接回车结束输入）：");
        long totalTime = 60;
        long time = totalTime * 1000 * 60;
        System.out.println(FormatUtil.formatTime(time));

        totalTime = 60;
        time = totalTime * 1000 * 60;
        System.out.println(FormatUtil.formatTime(time));


        totalTime = 1400;
        time = totalTime * 1000 * 60;
        System.out.println(FormatUtil.formatTime(time));

        totalTime = 1440;
        time = totalTime * 1000 * 60;
        System.out.println(FormatUtil.formatTime(time));
    }

    /*
      A class to encapsulate information about an audio focus request.
      An {@code AudioFocusRequest} instance is built by {@link Builder}, and is used to
      request and abandon audio focus, respectively
      with {@link AudioManager#requestAudioFocus(AudioFocusRequest)} and
      {@link AudioManager#abandonAudioFocusRequest(AudioFocusRequest)}.

      <h3>What is audio focus?</h3>
      <p>Audio focus is a concept introduced in API 8. It is used to convey the fact that a user can
      only focus on a single audio stream at a time, e.g. listening to music or a podcast, but not
      both at the same time. In some cases, multiple audio streams can be playing at the same time,
      but there is only one the user would really listen to (focus on), while the other plays in
      the background. An example of this is driving directions being spoken while music plays at
      a reduced volume (a.k.a. ducking).
      <p>When an application requests audio focus, it expresses its intention to “own” audio focus to
      play audio. Let’s review the different types of focus requests, the return value after a request,
      and the responses to a loss.
      <p class="note">Note: applications should not play anything until granted focus.</p>

      <h3>The different types of focus requests</h3>
      <p>There are four focus request types. A successful focus request with each will yield different
      behaviors by the system and the other application that previously held audio focus.
      <ul>
      <li>{@link AudioManager#AUDIOFOCUS_GAIN} expresses the fact that your application is now the
      sole source of audio that the user is listening to. The duration of the audio playback is
      unknown, and is possibly very long: after the user finishes interacting with your application,
      (s)he doesn’t expect another audio stream to resume. Examples of uses of this focus gain are
      for music playback, for a game or a video player.</li>

      <li>{@link AudioManager#AUDIOFOCUS_GAIN_TRANSIENT} is for a situation when you know your
      application is temporarily grabbing focus from the current owner, but the user expects playback
      to go back to where it was once your application no longer requires audio focus. An example is
      for playing an alarm, or during a VoIP call. The playback is known to be finite: the alarm will
      time-out or be dismissed, the VoIP call has a beginning and an end. When any of those events
      ends, and if the user was listening to music when it started, the user expects music to resume,
      but didn’t wish to listen to both at the same time.</li>

      <li>{@link AudioManager#AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK}: this focus request type is similar
      to {@code AUDIOFOCUS_GAIN_TRANSIENT} for the temporary aspect of the focus request, but it also
      expresses the fact during the time you own focus, you allow another application to keep playing
      at a reduced volume, “ducked”. Examples are when playing driving directions or notifications,
      it’s ok for music to keep playing, but not loud enough that it would prevent the directions to
      be hard to understand. A typical attenuation by the “ducked” application is a factor of 0.2f
      (or -14dB), that can for instance be applied with {@code MediaPlayer.setVolume(0.2f)} when
      using this class for playback.</li>

      <li>{@link AudioManager#AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE} is also for a temporary request,
      but also expresses that your application expects the device to not play anything else. This is
      typically used if you are doing audio recording or speech recognition, and don’t want for
      examples notifications to be played by the system during that time.</li>
      </ul>

      <p>An {@code AudioFocusRequest} instance always contains one of the four types of requests
      explained above. It is passed when building an {@code AudioFocusRequest} instance with its
      builder in the {@link Builder} constructor
      {@link AudioFocusRequest.Builder#AudioFocusRequest.Builder(int)}, or
      with {@link AudioFocusRequest.Builder#setFocusGain(int)} after copying an existing instance with
      {@link AudioFocusRequest.Builder#AudioFocusRequest.Builder(AudioFocusRequest)}.

      <h3>Qualifying your focus request</h3>
      <h4>Use case requiring a focus request</h4>
      <p>Any focus request is qualified by the {@link AudioAttributes}
      (see {@link Builder#setAudioAttributes(AudioAttributes)}) that describe the audio use case that
      will follow the request (once it's successful or granted). It is recommended to use the
      same {@code AudioAttributes} for the request as the attributes you are using for audio/media
      playback.
      <br>If no attributes are set, default attributes of {@link AudioAttributes#USAGE_MEDIA} are used.

      <h4>Delayed focus</h4>
      <p>Audio focus can be "locked" by the system for a number of reasons: during a phone call, when
      the car to which the device is connected plays an emergency message... To support these
      situations, the application can request to be notified when its request is fulfilled, by flagging
      its request as accepting delayed focus, with {@link Builder#setAcceptsDelayedFocusGain(boolean)}.
      <br>If focus is requested while being locked by the system,
      {@link AudioManager#requestAudioFocus(AudioFocusRequest)} will return
      {@link AudioManager#AUDIOFOCUS_REQUEST_DELAYED}. When focus isn't locked anymore, the focus
      listener set with {@link Builder#setOnAudioFocusChangeListener(OnAudioFocusChangeListener)}
      or with {@link Builder#setOnAudioFocusChangeListener(OnAudioFocusChangeListener, Handler)} will
      be called to notify the application it now owns audio focus.

      <h4>Pausing vs ducking</h4>
      <p>When an application requested audio focus with
      {@link AudioManager#AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK}, the system will duck the current focus
      owner.
      <p class="note">Note: this behavior is <b>new for Android O</b>, whereas applications targeting
      SDK level up to API 25 had to implement the ducking themselves when they received a focus
      loss of {@link AudioManager#AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK}.
      <p>But ducking is not always the behavior expected by the user. A typical example is when the
      device plays driving directions while the user is listening to an audio book or podcast, and
      expects the audio playback to pause, instead of duck, as it is hard to understand a navigation
      prompt and spoken content at the same time. Therefore the system will not automatically duck
      when it detects it would be ducking spoken content: such content is detected when the
      {@code AudioAttributes} of the player are qualified by
      {@link AudioAttributes#CONTENT_TYPE_SPEECH}. Refer for instance to
      {@link AudioAttributes.Builder#setContentType(int)} and
      {@link MediaPlayer#setAudioAttributes(AudioAttributes)} if you are writing a media playback
      application for audio book, podcasts... Since the system will not automatically duck applications
      that play speech, it calls their focus listener instead to notify them of
      {@link AudioManager#AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK}, so they can pause instead. Note that
      this behavior is independent of the use of {@code AudioFocusRequest}, but tied to the use
      of {@code AudioAttributes}.
      <p>If your application requires pausing instead of ducking for any other reason than playing
      speech, you can also declare so with {@link Builder#setWillPauseWhenDucked(boolean)}, which will
      cause the system to call your focus listener instead of automatically ducking.

      <h4>Example</h4>
      <p>The example below covers the following steps to be found in any application that would play
      audio, and use audio focus. Here we play an audio book, and our application is intended to pause
      rather than duck when it loses focus. These steps consist in:
      <ul>
      <li>Creating {@code AudioAttributes} to be used for the playback and the focus request.</li>
      <li>Configuring and creating the {@code AudioFocusRequest} instance that defines the intended
          focus behaviors.</li>
      <li>Requesting audio focus and checking the return code to see if playback can happen right
          away, or is delayed.</li>
      <li>Implementing a focus change listener to respond to focus gains and losses.</li>
      </ul>
      <p>
      <pre class="prettyprint">
      // initialization of the audio attributes and focus request
      mAudioManager = (AudioManager) Context.getSystemService(Context.AUDIO_SERVICE);
      mPlaybackAttributes = new AudioAttributes.Builder()
              .setUsage(AudioAttributes.USAGE_MEDIA)
              .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
              .build();
      mFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
              .setAudioAttributes(mPlaybackAttributes)
              .setAcceptsDelayedFocusGain(true)
              .setWillPauseWhenDucked(true)
              .setOnAudioFocusChangeListener(this, mMyHandler)
              .build();
      mMediaPlayer = new MediaPlayer();
      mMediaPlayer.setAudioAttributes(mPlaybackAttributes);
      final Object mFocusLock = new Object();

      boolean mPlaybackDelayed = false;

      // requesting audio focus
      int res = mAudioManager.requestAudioFocus(mFocusRequest);
      synchronized (mFocusLock) {
          if (res == AUDIOFOCUS_REQUEST_FAILED) {
              mPlaybackDelayed = false;
          } else if (res == AUDIOFOCUS_REQUEST_GRANTED) {
              mPlaybackDelayed = false;
              playbackNow();
          } else if (res == AUDIOFOCUS_REQUEST_DELAYED) {
             mPlaybackDelayed = true;
          }
      }

      // implementation of the OnAudioFocusChangeListener
      &#64;Override
      public void onAudioFocusChange(int focusChange) {
          switch (focusChange) {
              case AudioManager.AUDIOFOCUS_GAIN:
                  if (mPlaybackDelayed || mResumeOnFocusGain) {
                      synchronized (mFocusLock) {
                          mPlaybackDelayed = false;
                          mResumeOnFocusGain = false;
                      }
                      playbackNow();
                  }
                  break;
              case AudioManager.AUDIOFOCUS_LOSS:
                  synchronized (mFocusLock) {
                      // this is not a transient loss, we shouldn't automatically resume for now
                      mResumeOnFocusGain = false;
                      mPlaybackDelayed = false;
                  }
                  pausePlayback();
                  break;
              case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
              case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                  // we handle all transient losses the same way because we never duck audio books
                  synchronized (mFocusLock) {
                      // we should only resume if playback was interrupted
                      mResumeOnFocusGain = mMediaPlayer.isPlaying();
                      mPlaybackDelayed = false;
                  }
                  pausePlayback();
                  break;
          }
      }

      // Important:
      // Also set "mResumeOnFocusGain" to false when the user pauses or stops playback: this way your
      // application doesn't automatically restart when it gains focus, even though the user had
      // stopped it.
      </pre>
     */

}
