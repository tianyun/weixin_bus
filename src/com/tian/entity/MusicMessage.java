package com.tian.entity;

public class MusicMessage extends BaseMessage {
	private Music Music;

	public Music getMusic() {
		return Music;
	}

	public void setMusic(Music music) {
		Music = music;
	}

	@Override
	public String toString() {
		return "MusicMessage [Music=" + Music + "]";
	}
	
	
	public static Music musicMenu() {
		Music music = new Music();
		music.setThumbMediaId("B3jaT90nkbhflF5wi68mUITSpk2nVyMcTxhh8fbOnXBC-e9TdOQHK4aQ9Cb6C3Ak");
		music.setTitle("see you");
		music.setDescription("您好");
		music.setMusicUrl("http://bangding123-weixin.stor.sinaapp.com/images%2FIt.mp3");
		music.setHQMusicUrl("http://bangding123-weixin.stor.sinaapp.com/images%2FIt.mp3");
		
		return music;
	}
}
