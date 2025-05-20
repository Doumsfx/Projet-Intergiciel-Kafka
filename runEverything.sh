sudo ./1-runKafka.sh
sudo ./2-runPostgreSQL.sh


# On lance ensuite dans une fenêtre individuelle chacunes des 5 commandes suivantes
sudo gnome-terminal -- bash -c "./3-runLibreTranslate.sh; exec bash"
sudo gnome-terminal -- bash -c "./4-runConsDBClient.sh; exec bash"
sudo gnome-terminal -- bash -c "./5-runTranslateClient.sh; exec bash"

# Attente de 15 sec pour être sûr que tout est lancé correctement
sleep 15

sudo gnome-terminal -- bash -c "./6-runShellClientA.sh; exec bash"
sudo gnome-terminal -- bash -c "./7-runShellClientB.sh; exec bash"

