

# アーキテクチャ
MVVMを使用
ざっくり各々の役割を記述

## Model
データクラスなどを主に担当。Sqliteの文や該当するクラスのデータの下処理などはここに記述。
### 備考
データの下処理はCompanion ObjectなどのStaticメソッドとして利用してよし。


## View
主にFragmentやActivity。画面遷移やボタンの動作、画面遷移後の値の受け渡しなどを担当。
MVCのController的な立ち位置が強い
### 備考
ボタンのクリックイベントや、特に画面遷移は必ずここに記述すること・

## ViewModel
データバインディングによって、バインディングされた値の描画などを担当
***重要なことであるが、こいつがViewを呼び出して何か変更するなどの処理は絶対にないようにする***

基本的にView要素がViewModelの描画メソッドを呼び出すなどして、変更するようにする


# Proxy対応

大学に行って暇なときに開発したいときは、Proxyの設定が必須である。

## GithubのProxy設定

```
sh set_proxy.sh
```

これでproxyの設定がなるが、`unset_proxy.sh`を行うと、proxyの設定を無にすることができる。

## Androidプロジェクト自体のProxy設定

### settings
![android](https://user-images.githubusercontent.com/43840168/98783607-31150c00-243d-11eb-9338-cf3d23e3c995.png)

### gradle.properties

以下を追記して、象さんマークでSyncする
```
systemProp.http.proxyHost=example.com
systemProp.http.proxyPort=8080
systemProp.https.proxyHost=example.com
systemProp.https.proxyPort=8080
```

上から順にやると、Proxy環境下で動く。



