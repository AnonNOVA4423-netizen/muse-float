# Muse Float（上に浮くタイプ）

他のアプリの上に Muse の丸いアバターが浮いて、画面の端で跳ね返りながら動き回る Android アプリのプロジェクトです。
ドラッグで移動でき、タップすると言葉が変わって進む方向も変わります。

## スマホに入れるまで（PCが必要です）

1. PCに Android Studio（無料）をインストールする
2. このフォルダを Android Studio で開く
3. スマホを USB で PC につなぐ（または Android Studio のエミュレータではなく実機を選ぶ）
   - スマホ側で「開発者向けオプション → USBデバッグ」をオンにしておく
4. Android Studio の Run（▶）を押すと、スマホにインストールされます
5. アプリ「Muse Float」を開く
6. 「表示の許可を設定する」→ Muse Float の「他のアプリの上に表示」をオンにする
7. アプリに戻って「アバターを出す」を押す

止めたいときは、アプリで「アバターを消す」を押してください。通知も残ります。

## 注意

- この環境（Muse のコンピューター）には Android のビルド道具がないため、完成した APK ファイルではなく、ビルドできるプロジェクト一式をお渡ししています。
- Google Play には出していません。あなたのスマホに入れる個人用アプリです。
- 機種によっては「バッテリー最適化」からこのアプリを除外しないと、しばらくして止まることがあります。

## ファイル構成

- `app/src/main/java/com/example/musefloat/MainActivity.java` — 許可と開始／停止の画面
- `app/src/main/java/com/example/musefloat/FloatingService.java` — 浮いて動き回る本体
- `app/src/main/AndroidManifest.xml` — 許可（SYSTEM_ALERT_WINDOW など）の宣言
