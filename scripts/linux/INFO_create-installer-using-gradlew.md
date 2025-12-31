

##Forma manual

./gradlew clean jar

$ ls build/libs/

./scripts/linux/create-installer-using-gradlew.sh

## Forma automatizada utilizando a task criada lá no gradle
./gradlew createInstaller

--------------------------------
Como instalar aplicativos .deb

sudo dpkg -i dist/myapp_1.2_amd64.deb

projeto atual: adb-file-pusher_1.0.0_amd64.deb

sudo dpkg -i dist/adb-file-pusher_1.0.0_amd64.deb

-> RODANDO

o nome vem de: APP_NAME="MyApp"
/opt/myapp/bin/MyApp 

-> NESSE CASO O NOME É: adb_file_pusher
/opt/adb-file-pusher/bin/adb_file_pusher


-> desinstalando versão antiga
  sudo dpkg -r myapp # (Seu APP_NAME em minúsculas)

NESSE CASO
sudo dpkg -r adb-file-pusher  

-> VER TAMANHO DA APLICAÇÃO
dpkg-deb -I dist/adb-file-pusher_1.0.0_amd64.deb | grep Installed-Size

Installed-Size: 207698 KB
que vais ser: 207698/1024 = 202,83 MB -> 203MB

sem modulos fxml, web e media:  Installed-Size: 206005
que vira: 201,176757812 MB -> 201MB


# Após o jpackage terminar
cp "$APP_ICON" "/opt/adb-file-pusher/lib/${APP_NAME}.png"

sudo cp src/main/resources/logo_256x256.png /opt/adb-file-pusher/bin/adb_file_pusher.png