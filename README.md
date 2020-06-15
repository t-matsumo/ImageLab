# How to use
1. add image to android device.
2. open this app.
3. tap menu button you like.

# note
This app only read images on device (do not write and delete images).

# 作成中に失敗したことｗｗｗ
- 画像のlazy loadを実装しないせいで、スクロールして一行読み込むごとに固まる。
    Glideで解決
- 重い処理(openCVの処理をしてからのインデックス作成)をすべてアプリ起動時にやろうとして、画面が表示されるまでに時間がかかった。
    バルクインサートではなく、openCVの処理が終わったものから順にインサート
    RoomにLiveDataを使用しているので、逐次表示される。
- 重い処理を実行中にプログレスバーを表示していなかったため、フリーズと勘違いするくらい固まっていた。
    プログレスバーを実装
- LiveDataのsetValueをバックグラウンドで実行しようとして落ちてた。
    postValueを使って解決
- 非同期処理のキャンセルを実装しないせいで、読み込み→データ削除を順に実行すると読み込みが続く
    キャンセルを実行??今の作りだと難しくない？？？