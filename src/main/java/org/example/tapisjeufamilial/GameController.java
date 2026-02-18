package org.example.tapisjeufamilial;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import org.example.tapisjeufamilial.objects.Tapis;

public class GameController {

    private Scene scene;
    private Tapis selectedTapis;
    private AutoLayoutController autoLayout;

    // VARIABLE D'ÉTAT : Est-ce que la touche R est appuyée ?
    private boolean isRotatingRight = false;
    private boolean isRotatingLeft = false;

    public GameController(Scene scene, AutoLayoutController autoLayout) {
        this.scene = scene;
        this.autoLayout = autoLayout;
        initListeners();
    }

    public void setSelectedTapis(Tapis tapis) {
        if (this.selectedTapis != null) {
            this.selectedTapis.setStyle("-fx-border-color: black; -fx-border-width: 1;");
        }
        this.selectedTapis = tapis;
        if (this.selectedTapis != null) {
            this.selectedTapis.setStyle("-fx-border-color: blue; -fx-border-width: 3;");
        }
    }

    private void initListeners() {
        // 1. QUAND ON APPUIE
        scene.setOnKeyPressed(event -> {
            // 1. Gestion GLOBALE (Pas besoin de tapis sélectionné)
            if (event.getCode() == KeyCode.G) {
                System.out.println("Touche G : Génération auto...");
                if (autoLayout != null) {
                    boolean success = autoLayout.genererAgencement();
                    if (!success) System.out.println("Echec de la génération (trop serré ?)");
                }
                return; // On arrête là pour G
            }

            // 2. Gestion CIBLÉE (Besoin d'un tapis sélectionné)
            if (selectedTapis == null) return;

            // Gestion des déplacements (inchangé pour l'instant)
            double speed = 10.0;
            switch (event.getCode()) {
                case UP:    case Z: selectedTapis.deplacer(0, -speed); break;
                case DOWN:  case S: selectedTapis.deplacer(0, speed); break;
                case LEFT:  case Q: selectedTapis.deplacer(-speed, 0); break;
                case RIGHT: case D: selectedTapis.deplacer(speed, 0); break;

                // Pour la rotation, on active juste le drapeau
                case R:
                    isRotatingRight = true;
                    break;
                // Rotation inverse, idem avec T
                case T:
                    isRotatingLeft = true;
                    break;
            }
        });

        // 2. QUAND ON RELÂCHE (TRES IMPORTANT)
        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case R:
                    isRotatingRight = false; // On arrête de tourner
                    break;
                case T:
                    isRotatingLeft = false;
                    break;
            }
        });
    }

    /**
     * Cette méthode sera appelée à chaque image par l'AnimationTimer
     */
    public void update() {
        if (selectedTapis != null) {

            if (isRotatingRight){
                selectedTapis.tourner(1.0);
            }
            if(isRotatingLeft) {
                selectedTapis.tourner(-1.0);
            }
        }
    }
}