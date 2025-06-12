package com.example.focusnest;

import java.io.Serializable;

public class User implements Serializable {
    private int _id;

    private String name;
    //Settings
    private int studyTime,breakSmall,breakLarge;
    private int alarm,doNotDisturb;

    //Stats
    private int streak,pomodorosCompleted,pomodoroCyclesCompleted,totalStudySeconds,totalBreakSeconds;

    private String lastLoginDate; // format: "yyyy-MM-dd"

    public User(){}
    public User(String name){
        _id=0;//the Db will change it automatically
        this.name = name;
        //default values
        studyTime=15*60;
        breakSmall=5*60;
        breakLarge=15*60;
        alarm=1;//true
        doNotDisturb=0;//false

        streak=0;
        pomodorosCompleted=0;
        pomodoroCyclesCompleted=0;
        totalStudySeconds=0;
        totalBreakSeconds=0;
        lastLoginDate=null;
    }

//  set/get
    public int getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(int studyTime) {
        this.studyTime = studyTime;
    }

    public int getBreakSmall() {
        return breakSmall;
    }

    public void setBreakSmall(int breakSmall) {
        this.breakSmall = breakSmall;
    }

    public int getBreakLarge() {
        return breakLarge;
    }

    public void setBreakLarge(int breakLarge) {
        this.breakLarge = breakLarge;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public int getPomodorosCompleted() {
        return pomodorosCompleted;
    }

    public void setPomodorosCompleted(int pomodorosCompleted) {
        this.pomodorosCompleted = pomodorosCompleted;
    }

    public int getPomodoroCyclesCompleted() {
        return pomodoroCyclesCompleted;
    }

    public void setPomodoroCyclesCompleted(int pomodoroCyclesCompleted) {
        this.pomodoroCyclesCompleted = pomodoroCyclesCompleted;
    }

    public int getTotalStudySeconds() {
        return totalStudySeconds;
    }

    public void setTotalStudySeconds(int totalStudySeconds) {
        this.totalStudySeconds = totalStudySeconds;
    }

    public int getTotalBreakSeconds() {
        return totalBreakSeconds;
    }

    public void setTotalBreakSeconds(int totalBreakSeconds) {
        this.totalBreakSeconds = totalBreakSeconds;
    }

    public int get_id() {
        return _id;
    }

    void set_id(int id) {
        this._id=id;
    }

    public int getAlarm() {
        return alarm;
    }

    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }

    public int getDoNotDisturb() {
        return doNotDisturb;
    }

    public void setDoNotDisturb(int doNotDisturb) {
        this.doNotDisturb = doNotDisturb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){return name;}

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    private static final long serialVersionUID = 1L;
}






