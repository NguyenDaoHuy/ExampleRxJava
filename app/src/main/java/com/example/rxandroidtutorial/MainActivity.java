package com.example.rxandroidtutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //khởi tạo
        Observable<User> observable = getObservableUsers();
        Observer<User> observer = getObserverUser();

//        Observable<Serializable> observable = getObservableUsers();
//        Observer<Serializable> observer = getObserverUser();

        //kết nối
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

   /*     //defer
        User user = new User(1,"HuyTu");
        user.setName("HuyTu2");
        Observable<String> observableDefer = user.getNameDeferObserbable();
        Observer<String> observerDefer = getObserverUser();   */
    }

    private Observer<User> getObserverUser(){
        return new Observer<User>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.e("HuyTu","onSubscribe");
                mDisposable = d;
            }

            @Override
            public void onNext(@NonNull User user) {
                Log.e("HuyTu","onNext" + user.toString());
                Log.e("HuyTu","Observer onNext thread :"+ Thread.currentThread().getName());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("HuyTu","onError");
            }

            @Override
            public void onComplete() {
                Log.e("HuyTu","onComplete");
                Log.e("HuyTu","Observer onComplete thread :"+ Thread.currentThread().getName());
            }
        };
    }

/*  private Observer<Serializable> getObserverUser(){
      return new Observer<Serializable>() {
          @Override
          public void onSubscribe(@NonNull Disposable d) {
              Log.e("HuyTu","onSubscribe");
              mDisposable = d;
          }

          @Override
          public void onNext(@NonNull Serializable serializable) {
              Log.e("HuyTu","onNext" + serializable.toString());
              if(serializable instanceof User[]){
                  User[] users = (User[]) serializable;
                  for(User user: users){
                      Log.e("HuyTu","User Infor onNext :" + user.toString());
                  }
              }
          }

          @Override
          public void onError(@NonNull Throwable e) {
              Log.e("HuyTu","onError");
          }

          @Override
          public void onComplete() {
              Log.e("HuyTu","onComplete");
          }
      };
  }  */

/*    private Observable<Serializable> getObservableUsers(){
//        List<User> listUser = getListUser();
        User user1 = new User(1,"Huy 1");
        User user2 = new User(2,"Huy 2");

       //just
        User[] userArray = new User[]{user1, user2};
        return Observable.just(userArray);

     //   return Observable.interval(3,5, TimeUnit.SECONDS);

     //  return Observable.timer(3,TimeUnit.SECONDS);

     //    range and repeat
     //    return Observable.range(1,10).repeat(2);

    } */

    private Observable<User> getObservableUsers(){
        List<User> listUser = getListUser();
    /*    User user1 = new User(1,"Huy 1");
        User user2 = new User(2,"Huy 2");

         // fromArray
        User[] userArray = new User[]{user1, user2};
        return Observable.fromArray(userArray);  */

        // create
        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<User> emitter) throws Throwable {
                //in ra thread
                Log.e("HuyTu","Observable thread :"+ Thread.currentThread().getName());
                if(listUser == null || listUser.isEmpty()){
                    if(!emitter.isDisposed()){
                        emitter.onError(new Exception());
                    }
                }

                 for(User user : listUser){
                     if(!emitter.isDisposed()){
                         emitter.onNext(user);
                     }
                 }
                 if(!emitter.isDisposed()){
                     emitter.onComplete();
                 }
            }
        });
    }

    private List<User> getListUser(){
        List<User> list = new ArrayList<>();
        list.add(new User(1,"Huy 1"));
        list.add(new User(2,"Huy 2"));
        list.add(new User(3,"Huy 3"));
        list.add(new User(4,"Huy 4"));
        list.add(new User(5,"Huy 5"));
        list.add(new User(6,"Huy 6"));
        return list;
    }

    //ngắt kết nối
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDisposable != null){
            mDisposable.dispose();
        }
    }
}