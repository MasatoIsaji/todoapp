-- 1件目のデータ登録
INSERT INTO todos
(
   todo,
   username,
   status,
   detail,
   created_at,
   updated_at
)
VALUES
(
   '買い物',
   'admin',
   false,
   'スーパーで食材を購入する',
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   '図書館に行く',
   'admin',
   false,
   '本を借りる',
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   '皇居ラン',
   'admin',
   false,
   '皇居の周りを1周30分目安で走る！',
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   'コンビニに行く',
   'admin',
   false,
   'ブラックサンダーを買う',
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   'カレーを作る',
   'admin',
   false,
   '食材を買うのを忘れずに・・・',
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   'パソコンを買う',
   'admin',
   false,
   'メモリ32GBは欲しい',
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   '終わったやつ',
   'admin',
   true,
   '本当に終わった・・・？',
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   'ジムに行く',
   'user',
   false,
   '運動する',
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   '空を飛ぶ',
   'user',
   true,
   '飛べたらいいなぁ',
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