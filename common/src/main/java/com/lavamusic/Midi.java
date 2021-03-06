package com.lavamusic;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Midi {
    private static Midi common;
    private static final Object LockThis = new Object();
    public synchronized static Midi get() {
        synchronized (LockThis) {
            if (null == common) {
                common = new Midi();
            }
        }
        return common;
    }
    public int modeState(){
        return 1;
    }
    public boolean back(){
        return true;
    }
    private byte[] shortToByteArray(short data) {
        return new byte[]{(byte) (data & 0xff), (byte) ((data >>> 8) & 0xff)};
    }
    public void midiFinish(String fileToWrite,long filesize,boolean mono,DataOutputStream outFile){
        try {
            outFile.flush();
            outFile.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        long myDataSize = filesize;
        int myBitsPerSample = 16;
        long myChannels = ((mono) ? 1 : 2);
        long myChunk2Size = myDataSize * myChannels * myBitsPerSample / 8;
        long myChunkSize = 36 + myChunk2Size;
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(fileToWrite, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        try {
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int[] getRate(){return new int[]{8000, 11025, 16000, 22050, 44100, 48000, 88200, 96000};}
    public String getSoundfontsSet(String s){return (s.startsWith("#") ? "#" : "") + "soundfont \"" + s + "\"\n"; }
    public String getFontsPath(){
        return "/soundfonts/8Rock11e.sf2";
    }
    public String getCfgPath(){
        return "/config/lavamidi.cfg";
    }
    public byte[] dealDatas(byte[] mono,byte[] data){
        for (int i = 0; i < mono.length / 2; ++i) {
            int HI = 1;
            int LO = 0;
            int left = (data[i * 4 + HI] << 8) | (data[i * 4 + LO] & 0xff);
            int right = (data[i * 4 + 2 + HI] << 8) | (data[i * 4 + 2 + LO] & 0xff);
            int avg = (left + right) / 2;
            mono[i * 2 + HI] = (byte) ((avg >> 8) & 0xff);
            mono[i * 2 + LO] = (byte) (avg & 0xff);
        }
        return mono;
    }
    public String[] control(){
        String[] s = {
            "PM_REQ_MIDI",
                    "PM_REQ_INST_NAME",
                    "PM_REQ_DISCARD",
                    "PM_REQ_FLUSH",
                    "PM_REQ_GETQSIZ",
                    "PM_REQ_SETQSIZ",
                    "PM_REQ_GETFRAGSIZ",
                    "PM_REQ_RATE",
                    "PM_REQ_GETSAMPLES",
                    "PM_REQ_PLAY_START",
                    "PM_REQ_PLAY_END",
                    "PM_REQ_GETFILLABLE",
                    "PM_REQ_GETFILLED",
                    "PM_REQ_OUTPUT_FINISH",
                    "PM_REQ_DIVISIONS"
                    };
        return s;
    }
}
