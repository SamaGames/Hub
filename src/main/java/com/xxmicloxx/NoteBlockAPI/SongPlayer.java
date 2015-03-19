package com.xxmicloxx.NoteBlockAPI;

import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class SongPlayer {

    protected Song song;
    protected boolean playing = false;
    protected short tick = -1;
    protected CopyOnWriteArrayList<VirtualPlayer> playerList = new CopyOnWriteArrayList<>();
    protected boolean autoDestroy = false;
    protected boolean destroyed = false;
    protected Thread playerThread;
    protected byte fadeTarget = 100;
    protected byte volume = 100;
    protected byte fadeStart = volume;
    protected int fadeDuration = 60;
    protected int fadeDone = 0;
    protected FadeType fadeType = FadeType.FADE_LINEAR;

    public SongPlayer(Song song) {
        this.song = song;
        createThread();
    }

    public FadeType getFadeType() {
        return fadeType;
    }

    public void setFadeType(FadeType fadeType) {
        this.fadeType = fadeType;
    }

    public byte getFadeTarget() {
        return fadeTarget;
    }

    public void setFadeTarget(byte fadeTarget) {
        this.fadeTarget = fadeTarget;
    }

    public byte getFadeStart() {
        return fadeStart;
    }

    public void setFadeStart(byte fadeStart) {
        this.fadeStart = fadeStart;
    }

    public int getFadeDuration() {
        return fadeDuration;
    }

    public void setFadeDuration(int fadeDuration) {
        this.fadeDuration = fadeDuration;
    }

    public int getFadeDone() {
        return fadeDone;
    }

    public void setFadeDone(int fadeDone) {
        this.fadeDone = fadeDone;
    }

    protected void calculateFade() {
        if (fadeDone == fadeDuration) {
            return; // no fade today
        }
        double targetVolume = Interpolator.interpLinear(new double[]{0, fadeStart, fadeDuration, fadeTarget}, fadeDone);
        setVolume((byte) targetVolume);
        fadeDone++;
    }

    protected void createThread() {
        playerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!destroyed) {
                    long startTime = System.currentTimeMillis();

                        if (playing) {
                            calculateFade();
                            tick++;
                            if (tick > song.getLength()) {
                                playing = false;
                                tick = -1;
                                if (autoDestroy) {
                                    destroy();
                                    return;
                                }
                            }

                            for (VirtualPlayer p : playerList) {
                                if (p == null || p.getPlayer() == null) {
									playerList.remove(p);
                                    continue;
                                }
                                playTick(p.getPlayer(), tick);
                            }
                        }

                    long duration = System.currentTimeMillis() - startTime;
                    float delayMillis = song.getDelay() * 50;
                    if (duration < delayMillis) {
                        try {
                            Thread.sleep((long) (delayMillis - duration));
                        } catch (InterruptedException e) {
                            // do nothing
                        }
                    }
                }
            }
        });
        playerThread.setPriority(Thread.MAX_PRIORITY);
        playerThread.start();
    }

    public List<VirtualPlayer> getPlayerList() {
        return Collections.unmodifiableList(playerList);
    }

    public void addPlayer(Player p) {
        addPlayer(new VirtualPlayer(p));
    }

	public void addPlayer(VirtualPlayer p) {
			if (!playerList.contains(p)) {
				playerList.add(p);
			}
	}

    public boolean getAutoDestroy() {
        synchronized (this) {
            return autoDestroy;
        }
    }

    public void setAutoDestroy(boolean value) {
        synchronized (this) {
            autoDestroy = value;
        }
    }

    public abstract void playTick(Player p, int tick);

    public void destroy() {
            destroyed = true;
            playing = false;
            setTick((short) -1);
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public short getTick() {
        return tick;
    }

    public void setTick(short tick) {
        this.tick = tick;
    }

    public void removePlayer(Player p) {
        removePlayer(new VirtualPlayer(p));
    }

	public void removePlayer(VirtualPlayer player) {
		playerList.remove(player);
	}

    public byte getVolume() {
        return volume;
    }

    public void setVolume(byte volume) {
        this.volume = volume;
    }

    public Song getSong() {
        return song;
    }
}
