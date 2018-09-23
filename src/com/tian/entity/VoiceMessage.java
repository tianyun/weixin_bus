package com.tian.entity;

public class VoiceMessage extends BaseMessage {
	private Voice Voice;

	public Voice getVoice() {
		return Voice;
	}

	public void setVoice(Voice voice) {
		Voice = voice;
	}

	public static Voice voiceMenu() {
		Voice voice = new Voice();
		voice.setMediaId("_w86dK8mgnZ-joUYI_2vBigwLA7df7afPJZcha5diwNIWHi6ZZ6vbwfcrC1yjuye");
		return voice;
	}
}
