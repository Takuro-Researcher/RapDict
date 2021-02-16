

# アーキテクチャ
Ver2.0現在
ViewModel＋Repositoryを使用

## データ側
DataClassなどがある。database関連やDaoクラス。
MVCのModel的な役割を想定している。

## View側
FragmentやActivity。画面遷移やボタンの動作、Dialogなど。
LiveDataのObserveくらい
データの整形などは極力しない。（してしまっているところは今後治す）

## ViewModel
LiveDataなどを保持。監視しているLiveDataの値を変更するメソッドを基本的に書いていく。
また、Repositoryクラスを呼び出し、データの取得もViewModelの仕事

## Repository
APIやSqliteなど、中断関数を経由するものはすべてDataRepositoryに記述するようにする。
一応今回の設計ではデータの整形はあまり行わず、そのままViewModelに渡すようにする。

この設計に従っていないコードは基本的に直していく方針を取る。

# Proxy対応

大学に行って暇で開発したいときは、Proxyの設定が必須である。

https://qiita.com/Takuro-Researcher/items/4778d8d0e292af6a5350

忘れた時は↑を確認。


