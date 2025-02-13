-- 1件目のデータ登録
INSERT INTO todos
(
   todo,
   username,
   detail,
   created_at,
   updated_at
)
VALUES
(
   '買い物',
   'admin',
   'スーパーで食材を購入する',
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   '図書館に行く',
   'admin',
   '本を借りる',
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   '皇居ラン',
   'admin',
   '皇居の周りを1周30分目安で走る！',
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   'コンビニに行く',
   'admin',
   'ブラックサンダーを買う',
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   'カレーを作る',
   'admin',
   '食材を買うのを忘れずに・・・',
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   'パソコンを買う',
   'admin',
   'メモリ32GBは欲しい',
   CURRENT_TIMESTAMP,
   CURRENT_TIMESTAMP
),

(
   'ジムに行く',
   'user',
   '運動する',
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