# RRO
RRO = Retrofit+RxJava+Okhttp3<br>
非常好用, 可靠的网络框架. 使用FastJson解析数据<br>

实现了使用FastJson的RetrofitConverter, 比GSON解析速度更快.<br>
保留Retrofit简洁的调用方式, 同时去掉过多繁琐的配置, 快速上手.<br>

# 快速使用(不配置RxJava)
```java
 public static String API_URL = "https://api.github.com"; //定义一个请求地址
 

 public interface GitHub { //和Retrofit一样, 定义一个本地接口
    @GET("/")
    Observable<HashMap<String, String>> index();
  }
  
  
  GitHub github = RRO.getApiService(GitHub.class, API_URL); //获取包装好的接口实例, 接下来就可以像调用本地接口方法一样做网络请求了.
  
  Call<HashMap<String, String>> call = github.index(); //与Retrofit用法一致, 调用本地方法
  
  call.enqueue(new Callback<HashMap<String, String>>() { //异步执行
    @Override
    public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
        // 这里的response即接口返回数据经FastJson解析后的结果.
    }

    @Override
    public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
        Log.d("RRO", "onFail: " + t.getMessage());
    }
  });
```
# 以RxJava回调的请求
```java
RRO.setApiUrl(API_URL); //设置请求地址

public interface RxGitHub { //返回值Call改为RxJava的Observalbe类型
  @GET("/")
  Observable<HashMap<String, String>> index();
}

RxGitHub apiService = RRO.getApiService(RxGitHub.class);
apiService.index()
          .subscribeOn(Schedulers.io()) //发送线程由RxJava管理
          .observeOn(AndroidSchedulers.mainThread()) //在主线程回调
          .subscribe(new Subscriber<HashMap<String, String>>() {
              @Override
              public void onCompleted() {

              }

              @Override
              public void onError(Throwable e) {

              }

              @Override
              public void onNext(HashMap<String, String> hashMap) {
                    // 这里返回FastJson的解析结果
              }
           });
 ```
