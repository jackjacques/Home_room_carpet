package org.example.tapisjeufamilial.objects;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color; // N'oublie pas cet import
import org.example.tapisjeufamilial.collisions.CollisionsManager;
import org.example.tapisjeufamilial.collisions.HitBox;

public class Meubles extends StackPane {

    // Pas besoin de x, y, angle0 en variables globales si c'est fixe,
    // les méthodes de StackPane suffisent.

    private HitBox Hb;

    public Meubles(double x0, double y0, double width, double height) {
        super(); // Appel au constructeur de StackPane

        // 1. Mise en place du Canvas
        Canvas canvas = new Canvas(width, height);
        this.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // 2. DESSIN PROVISOIRE (Pour voir le meuble)
        // On dessine un rectangle marron pour représenter un meuble en bois
        gc.setFill(Color.SADDLEBROWN);
        gc.fillRect(0, 0, width, height);
        // On ajoute un contour noir
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(0, 0, width, height);

        // 3. Positionnement
        this.setLayoutX(x0);
        this.setLayoutY(y0);

        // Fixer la taille du StackPane
        this.setPrefSize(width, height);
        // setMin/MaxSize évite que le layout parent ne redimensionne tes meubles
        this.setMinSize(width, height);
        this.setMaxSize(width, height);

        // 4. HitBox
        // On utilise width/height directement, pas de variables static
        // Attention : HitBox prend (x, y, h, w). Vérifie l'ordre dans ta classe HitBox !
        this.Hb = new HitBox(x0, y0, width, height, true, "MEUBLE");        CollisionsManager.addHitBox(Hb);

    }
}