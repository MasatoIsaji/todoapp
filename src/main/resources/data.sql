INSERT INTO todos
(
   todo,
   username,
   status,
   detail,
   priority,
   created_at,
   updated_at
)
VALUES
(
   '買い物',
   'admin',
   false,
   'スーパーで食材を購入する',
   1,
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   '図書館に行く',
   'admin',
   false,
   '本を借りる',
   2,
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   '皇居ラン',
   'admin',
   false,
   '皇居の周りを1周30分目安で走る！',
   2,
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   'コンビニに行く',
   'admin',
   false,
   'ブラックサンダーを買う',
   1,
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   'カレーを作る',
   'admin',
   false,
   '食材を買うのを忘れずに・・・',
   3,
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   'パソコンを買う',
   'admin',
   false,
   'メモリ32GBは欲しい',
   0,
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   'アイスを買う',
   'admin',
   true,
   'おいしそうなアイスを２つ購入する',
   2,
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   'ジムに行く',
   'user',
   false,
   '運動する',
   1,
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   '車検に出す',
   'false',
   false,
   '○○ディーラーに事前にアポを取ること',
   2,
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   'お風呂を掃除する',
   'user',
   false,
   'ちゃんとカビキラーを使うこと',
   3,
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   'クリーニングに出す',
   'user',
   false,
   '出す物：スーツ/カッターシャツ/カーディガン',
   3,
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   '自動車税を払う',
   'user',
   false,
   'コンビニ支払い',
   2,
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   'サーバーを起動',
   'user',
   false,
   'サーバーを起動させる',
   0,
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   '空を飛ぶ',
   'user',
   true,
   '飛べたらいいなぁ',
   1,
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
);
-- 認証テーブルへのダミーデータの追加
-- password：adminpass
INSERT INTO authentications
(
   username,
   password,
   authority
)
VALUES
(
   'admin',
   '$2a$10$lNH4dLsCH4/g7aZZq14QG.PvnC7rkeN395ZWanW/hTOi5k6y009mm',
   'ADMIN'
);
-- password：userpass
INSERT INTO authentications
(
   username,
   password,
   authority
)
VALUES
(
   'user',
   '$2a$10$/jar9xXQ6lrnVjLvLGv5BepFkLnGIO49RrGx42p2i.1hQt1BZ/7E2',
   'USER'
);