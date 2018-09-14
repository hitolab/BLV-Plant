#BLV-Plant
#####シリアル通信の設定について
基本的に、gitに上がっているBLVの最新バージョンの構成で動くはず。
* rxtxSerial.dllは使っているjavaのbinに置く（C:\Eclipce\pleiades\java\8\bin\rxtxSerial.dll）
（64bit版の入手先：<http://fizzed.com/oss/rxtx-for-java>　→　<https://github.com/CMU-CREATE-Lab/commons-java/blob/master/java/lib/rxtx/rxtxSerial64.dll>）
* rxtxSerial64.dllの"64"をリネームで削除して使用する。
* RXTXcomm.jarは使っているjavaのlibの下に置く（C:\Eclipce\pleiades\java\8\lib）

#####エラーが出た時にまず確認すること
* どんなエラーが出ているか、どこで出ているかを把握して、ググる。
* コンソールのログをとるなどして、状況を報告できるようにする（後に解決したら、備忘録としてまとめる）
* 接続機器のコンセントは挿さっているか、プラスマイナスはあっているか、スイッチは入っているか確認（SSRのスイッチなどはエラー時にオフされずに終了する場合があるので、SSRの状態も確認）
* 電線が切れていないか。（テスターで回路を確認するとよい）
* 接続ポートの割り当てがあっているか

#####謎エラー(再現性のないエラー)が出た時の対処
* 再起動してみる。セーフブートなどの軽量化も試す。
* 開発環境（eclipce）をクリーン＆リフレッシュして再ビルドする
* 接続ポートの割り当てを変えてみる。（いくつか通信に向かないポート番号があるっぽい）
* 装置の回路が生きているか、テスターで確認する。
* 実行環境（OSの違いやスペック不足）を疑う。再現性のないエラーは大体これに行き着く・・・

#####ブルースクリーンエラー（強制終了）の対処
######MEMORY_MANEGEMENT ブルースクリーンエラーの対処

* 主な原因
PCのメモリが圧迫されている？
* 対策
eclipceの割り当てメモリを増量する（自動で調整してくれる機能がデフォルトで付いているので、しなくても良いかも）
やり方参考：<http://xirasaya.com/?m=detail&hid=222>

バックグラウンドで動いている他のアプリを止める（Microsoft Diffenderなどのセキュリティソフトは、特に容量をくってる場合が多いので、止めれば効果的）
######DRIVER_POWER_STATE_FAILURE ブルースクリーンエラーの対処
* 主な原因
接続している機器との接続に問題がある
* 対策
Microsoft Diffenderなどのセキュリティソフトが、接続機器を勝手に脅威として扱っている場合があるので、機能を止める。
やり方参考1：<https://boxil.jp/mag/a13/>
やり方参考2:<https://www.fmworld.net/cs/azbyclub/qanavi/jsp/qacontents.jsp?PID=6511-1105>
やり方参考3:<https://freesoft.tvbok.com/tips/windows_tips/regedit_administrators.html>

機器の正しいドライバをインストールする。PCがその機器をサポートしているか調べる。

######Windous Auto Updateの対処
win10はデフォルトでオートアップデート機能がオンになっており、放置して置くと実験中に勝手に更新＆再起動などをする可能性があるので、エラーではないが、機能停止しておく方が良い。
やり方参考：<https://togeonet.co.jp/post-7032>

#####その他、win10の設定周りでやっておいた方が良さそうなこと
* 高速スタートアップ機能の切り替え
やり方：<https://togeonet.co.jp/post-14079>
#####ブルースクリーンエラー（強制終了）の対処
#####ブルースクリーンエラー（強制終了）の対処
#####eclipceでのjarファイルの書き出し
プロジェクト名で右クリックから、
エクスポート＞java＞実行可能jarファイル＞書き出し設定画面

* エクスポート先はworkspace内の任意のプロジェクトの直下でok
* 「生成するjarに必須ライブラリをパッケージ」にチェックを入れれば、楽に扱える
参考：<https://www.ilovex.co.jp/blog/system/projectandsystemdevelopment/jar>

#####jarの実行環境の設定
現在使っているjavaの環境でプログラムが動くように、環境変数を設定しなおす必要がある場合がある。
WinのOSのバージョンによって画面構成が異なる場合があるが、
コントロールパネル＞「環境変数の編集」で検索すると設定画面に移る。

* 「システム環境変数」に「JAVA＿HOME」を新規追加し、変数値は「使っているjavaのjdkのbin(java\jdk~.~.~\bin)」を指定する。

* 同様にして「ユーザー環境変数」にある「PATH」を編集し、変数値は「使っているjavaのjdkのbin(java\jdk~.~.~\bin)」を指定する。（すでにeclipceソフト内のjavaに設定されている場合など、プログラムの実行環境によってはこっちの手順はしなくてもよいかも？）

わかりやすい参考：<http://eiua-memo.tumblr.com/post/142196867378/windows10%E3%81%A7jar%E3%83%95%E3%82%A1%E3%82%A4%E3%83%AB%E3%82%92%E8%B5%B7%E5%8B%95%E3%81%99%E3%82%8B%E6%96%B9%E6%B3%95>

