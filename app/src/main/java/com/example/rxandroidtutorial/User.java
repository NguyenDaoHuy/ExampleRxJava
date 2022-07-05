package com.example.rxandroidtutorial;

import java.io.Serializable;

import io.reactivex.rxjava3.core.Observable;

public class User implements Serializable {
    private int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public Observable<String> getNameObserbable(){
        return Observable.just(name);
    }

    public Observable<String> getNameDeferObserbable(){
        return Observable.defer(() -> Observable.just(name));
    }
}
