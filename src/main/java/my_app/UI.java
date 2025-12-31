package my_app;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import megalodonte.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UI {
     State<String> folderDestination = new State<>("");
     State<String> currentFile = new State<>("");
     State<String> devices = new State<>("");
     State<String> ipPort = new State<>("");
     State<String> pairCode = new State<>("");
     State<Integer> pushProgress = new State<>(0);
    private volatile boolean pushFinished = false;

    public Component render() {
        final var showProgress =
                pushProgress.map(v -> v > 0 && v < 100);

        return new Column(new ColumnProps().paddingAll(15).spacingOf(20))
                .child(new Text("ADB Pusher", new TextProps().fontSize(25)))
                .child(
                        new Row(new RowProps().spacingOf(40))
                                .child(
                                        new Column(new ColumnProps().spacingOf(10))
                                                .child(ActionButton("Find devices", this::findDevices))
                                                .child(new Text(devices, new TextProps().fontSize(17)))
                                ).child(
                                        new Column(new ColumnProps().spacingOf(10))
                                                .child(ActionButton("Pair device", this::pairDevice))
                                                .child(Input_("IP:PORT",ipPort))
                                                .child(Input_("XXXXXX",pairCode))
                                ))
                .child(new SpacerVertical(20))
                .child(new LineHorizontal())
                .child(new Row(new RowProps().spacingOf(20))
                                .child(InputColumn("Destination folder", folderDestination))
                                .child(InputColumn("File path", currentFile))
                                .child(ActionButton("Push to device", this::push))
                )
                .child(Show.when(showProgress, ()-> new ProgressBar(pushProgress)));
    }

    private Input Input_(String placeholder, State<String> inputState) {
        return new Input(inputState, new InputProps().fontSize(17).placeHolder(placeholder));
    }

    private Column InputColumn(String label, State<String> inputState) {
        return new Column()
                .child(new Text(label, new TextProps().fontSize(18)))
                .child(Input_(label, inputState));
    }

    private Button ActionButton(String text, Runnable callback) {
        return new Button(text,
                new ButtonProps()
                        .onClick(callback::run)
                        .bgColor("#5D8A9D")
                        .textColor("white")
                        .fontSize(20));
    }

    private void pairDevice() {
        Thread.ofVirtual().start(() -> {
            try {
                ProcessBuilder pb =
                        new ProcessBuilder("adb", "pair", ipPort.get());

                Process process = pb.start();

                // 1️⃣ Envia o código de pareamento
                process.getOutputStream()
                        .write((pairCode.get() + "\n").getBytes());
                process.getOutputStream().flush();
                process.getOutputStream().close();

                // 2️⃣ Lê a resposta
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(process.getInputStream())
                        );

                String line;
                StringBuilder output = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }

                process.waitFor();

                Platform.runLater(() -> {
                    IO.println(output.toString());
                    if(output.toString().contains("Enter pairing code: ")){
                        devices.set("No device founded");
                    }else devices.set(output.toString());
                });

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void push() {
        pushProgress.set(0);
        pushFinished = false;

        // 🔄 Thread do fake progress
        Thread.ofVirtual().start(() -> {
            int value = 0;

            while (!pushFinished && value < 90) {
                value += 1 + (int)(Math.random() * 3); // avanço irregular
                int safeValue = Math.min(value, 90);

                Platform.runLater(() ->
                        pushProgress.set(safeValue)
                );

                try {
                    //Thread.sleep(120); // suavidade
                    Thread.sleep(220);
                } catch (InterruptedException ignored) {}
            }
        });

        // 🚀 Thread do adb push real
        Thread.ofVirtual().start(() -> {
            try {
                ProcessBuilder pb = new ProcessBuilder(
                        "adb",
                        "push",
                        currentFile.get(),
                        "/storage/emulated/0/" + folderDestination.get()
                );

                Process process = pb.start();
                process.waitFor();

                pushFinished = true;

                Platform.runLater(() -> {
                    pushProgress.set(100);
                    IO.println("Push finalizado");
                    Alert a = new Alert(Alert.AlertType.WARNING);
                                a.setContentText("✅ Push finished");
                                a.setTitle(null);
                                a.setHeaderText(null);
                                a.setGraphic(null);
                                a.show();
                });

            } catch (Exception e) {
                pushFinished = true;
                Platform.runLater(() ->
                        IO.println("❌ Erro no push: " + e.getMessage())
                );
            }
        });
    }

    private void findDevices(){
        devices.set("");

        Thread.ofVirtual().start(()->{
            ProcessBuilder pb = new ProcessBuilder("adb", "devices");
            try {
                var process = pb.start();

                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    if (line.isBlank()) continue; // 👈 aqui resolve
                    sb.append(line).append("\n");
                }

                process.waitFor();

                Platform.runLater(() ->{
                    IO.println(sb.toString());
                    devices.set(sb.toString());
                });

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
