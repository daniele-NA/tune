# 🎵 Kotlin-Android Music Reproducer

Un riproduttore musicale semplice ma potente per Android, costruito con **Kotlin**. Supporta ricerca, riproduzione e notifiche interattive, tutto gestito in modo modulare.

## ✨ Funzionalità principali

* 🔍 **Ricerca e gestione playlist** – Cerca e visualizza le canzoni tramite RecyclerView.
* 🎶 **Riproduzione musicale** – Controllo completo tramite MediaPlayer, con gestione centralizzata dello stato.
* 🔄 **Switch tra pagine** – Mantiene traccia dell’attività corrente e gestisce la navigazione tra i fragment.
* 🔔 **Notifiche interattive** – Il servizio musicale crea notifiche con colori, Intents e gestione dei tasti di controllo.
* 🖼️ **Animazioni e interfaccia** – ProgressBar e animazioni sull’ImageView principale rendono l’esperienza più dinamica.

## 🛠️ Architettura

* **Controllers**

  * `SearchManagement.kt`: gestione della ricerca, animazioni e popolamento del RecyclerView.
  * `ObjectSwitcher`: gestione attività correnti, navigazione e creazione delle notifiche tramite `MusicService`.

* **Model**

  * `CurrentSongProperties`: database statico centrale per il MediaPlayer.
  * `Song`: incapsula titolo e autore; l’autore funge da chiave per le risorse RAW.

* **View**

  * `SearchFragment`: interfaccia per la ricerca e comunicazione con il controller.
  * `HomeFragment`: gestisce pulsanti, progressBar e animazioni principali.

## ⚡ Uso rapido

1. Inserisci le tue canzoni nella cartella `res/raw`.
2. Registra i file in `CurrentSongProperties.kt` modificando `songMap`.
3. Avvia l’app e goditi la riproduzione con notifiche integrate.
