# PrefixJoinQuitMsg-1.8.9
Hypixel のフレンド Join/Quit メッセージに Prefix を付けれる Mod です。

この Mod は <a href="https://www.chattriggers.com/">ChatTriggers<a/> の <a href="https://www.chattriggers.com/old/imports/RankJoinLeave">Rank Join Leave<a/> を参考にし、作成しました。

<img src="https://t.gyazo.com/teams/omn/02fb507562450d1ee1d4174c77aea0b5.png" alt="02fb507562450d1ee1d4174c77aea0b5" title="02fb507562450d1ee1d4174c77aea0b5">

# Download

ダウンロード: <a href="https://github.com/SimplyRin/PrefixJoinQuitMsg-1.8.9/releases/download/1.4/PrefixJoinQuitMsg-1.4.jar">v1.4<a/>

※ 使用は自己責任でお願いします...

# API

Prefix 取得したい場合以下の API を使用することで取得できます。

URL: `https://api.v2.simplyrin.net/Hypixel-API/prefix.php?name=SimplyRin`

Result:
```JSON
{
  "success": true,
  "player": "SimplyRin",
  "prefix": "&6[MVP&4++&6]"
}
```

# Libraries

この Mod は <a href="https://github.com/Sk1er/Sk1erHypixelPublicMod">Sk1erHypixelPublicMod<a/> の一部のコードを使用しています。

この Mod は <a href="https://github.com/SpigotMC/BungeeCord">BungeeCord<a/> の <a href="https://github.com/SpigotMC/BungeeCord/blob/master/chat/src/main/java/net/md_5/bungee/api/ChatColor.java">ChatColor.java<a/> コードを使用しています。

1.4 より、 <a href="https://github.com/HyperiumClient/Hyperium">Hyperium<a/> コードの使用に伴い、ライセンスを MIT から GPLv3 に変更しました
