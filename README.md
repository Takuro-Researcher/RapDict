

# アーキテクチャ
MVVMを使用
ざっくり各々の役割を記述

## Model
DataClassなどがある。database関連やDaoがModelになるイメージ

## View
FragmentやActivity。画面遷移やボタンの動作、Dialogなど。

LiveDataのObserveくらい

データの整形などは極力しない

## ViewModel
データバインディングによって、バインディングされた値の描画などを担当
***重要なことであるが、こいつがViewを呼び出して何か変更するなどの処理は絶対にないようにする***

基本的にView要素がViewModelの描画メソッドを呼び出すなどして、変更するようにする



## Repository
APIやSqliteなど、中断関数を経由するものはすべてDataRepositoryに記述するようにする。

一応今回の設計ではデータの整形はあまり行わず、そのままViewModelに渡すようにする。


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



