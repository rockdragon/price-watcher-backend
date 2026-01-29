# Price Watcher（プライス・ウォッチャー）のバックエンド

## 事前準備

1. <code>src/main/resources/application.properties</code>で、メール転送を設定
```properties
spring.mail.host=
spring.mail.port=
spring.mail.username=
spring.mail.password=
```

2. Maven パッケージをインストールする
```bash
./dependency_install.sh
```

## 起動する方法
```bash
./run.sh
```