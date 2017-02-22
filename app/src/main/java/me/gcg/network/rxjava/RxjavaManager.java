package me.gcg.network.rxjava;

/**
 * Created by gechuanguang on 2017/2/20.
 * 邮箱：1944633835@qq.com
 */
public class RxjavaManager<T>{

    private static  RxjavaManager instance=null;

    private RxjavaManager(){
    }
    public static  RxjavaManager newInstance(){

        if(instance==null){
            synchronized (RxjavaManager.class){
                if(instance==null){
                    instance=new RxjavaManager();
                }
            }
        }
        return  instance ;
    }



    /**
     *  获取 被观察者对象
     */
//    public  Observable<T> getObservable(){
//        Observable<T> observable=Observable.create(new Observable.OnSubscribe<T>() {
//            @Override
//            public void call(Subscriber<? super T> subscriber) {
//                subscriber.onNext(T);
//            }
//        });
//        observable.subscribe(new Subscriber<T>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(T t) {
//
//            }
//        });
//        return  observable;
//    }
}
