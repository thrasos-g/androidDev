package com.example.focusnest;

public class User {
    private int _id;

    private String name;
    //Settings
    private int studyTime,breakSmall,breakLarge;
    private int alarm,doNotDisturb;

    //Stats
    private int streak,pomodorosCompleted,pomodoroCyclesCompleted,totalStudySeconds,totalBreakSeconds;

    public User(){}
    public User(int id, String name){
        _id=id;
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
}
