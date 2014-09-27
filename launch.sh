#!/bin/bash

Port=2222
Server=jeux.cam.main.Server
ClientJeu=cam.ClientJeu
Joueur=cam.modele.JoueurCam
AddrServer=localhost

echo "Start the server"
#java -cp target/iia.projet-1.0.jar $Server $Port >server.log 2>&1 &
java -cp obfcam.jar cam.ServeurJeu $Port 1 >server.log 2>&1 &
serverPID=$!
echo "Server launched PID: $serverPID"

sleep 1

echo "Start the first client"
java -cp target/iia.projet-1.0.jar $ClientJeu $Joueur $AddrServer $Port &
fistClientPID=$!
echo "First client launched PID: $fistClientPID"

echo "Start the second client"
java -cp target/iia.projet-1.0.jar $ClientJeu $Joueur $AddrServer $Port &
secondClientPID=$!
echo "Second client launched PID: $secondClientPID"

wait $fistClientPID
echo "The first client ended."

wait $secondClientPID
echo "The second client ended."

echo "Press any key to exit..."
read end
kill -9 $serverPID
echo "Kill the server."

exit 0
