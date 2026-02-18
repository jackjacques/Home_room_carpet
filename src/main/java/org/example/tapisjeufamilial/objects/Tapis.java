package org.example.tapisjeufamilial.objects;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.example.tapisjeufamilial.collisions.CollisionsManager;
import org.example.tapisjeufamilial.collisions.HitBox;

public class Tapis extends StackPane {

    private double x, y;
    private Canvas canvas;
    private GraphicsContext gc;
    private HitBox Hb;
    private Color couleurTapis; // La couleur du tapis

    public Tapis(double x0, double y0, double width, double height, Color couleur) {
        super();

        this.x = x0;
        this.y = y0;
        this.couleurTapis = couleur;

        // 1. Création du Canvas
        this.canvas = new Canvas(width, height);
        this.getChildren().add(canvas);
        this.gc = canvas.getGraphicsContext2D();

        // 2. Dessin du rectangle
        dessinerTapis(width, height);

        // 3. Positionnement initial
        this.setLayoutX(x);
        this.setLayoutY(y);

        // 4. Paramétrage taille du StackPane (Node)
        this.setPrefSize(width, height);

        // 5. HitBox pour les collisions
        // Attention : width/height inversés dans HitBox ? (h, w) selon ta classe précédente
        this.Hb = new HitBox(x0, y0, width, height, true, "TAPIS");
        CollisionsManager.addHitBox(Hb);
    }

    /**
     * Dessine un rectangle plein avec contours
     */
    private void dessinerTapis(double w, double h) {
        // Fond du tapis
        gc.setFill(this.couleurTapis);
        gc.fillRect(0, 0, w, h);

        // Contour du tapis (Cadre noir)
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3); // Épaisseur du trait
        gc.strokeRect(0, 0, w, h);

        // Optionnel : Un petit motif ou croix au centre pour voir la rotation ?
        gc.strokeLine(0, 0, w, h);
    }

    // --- LOGIQUE DE MOUVEMENT ET ROTATION ---

    // Dans Tapis.java

    // ... constructeur et autres méthodes ...

    public void tourner(double speed) {
        double futurAngle = this.getRotate() + speed;

        // VERIFICATION DE LA ROTATION
        // On teste si en tournant sur place, on ne tape pas un meuble
        // (dx=0, dy=0, mais angle change)
        if (CollisionsManager.canMove(this.Hb, 0, 0, futurAngle)) {
            // Si c'est libre, on applique
            this.setRotate(futurAngle);
            // Et on met à jour la HitBox définitivement
            this.Hb.update(this.x, this.y, futurAngle);
        }
    }

    public void deplacer(double dx, double dy) {
        double currentAngle = this.getRotate();

        // VERIFICATION DU DEPLACEMENT
        // On teste si en bougeant (dx, dy) avec l'angle actuel, c'est bon
        if (CollisionsManager.canMove(this.Hb, dx, dy, currentAngle)) {

            // Calcul des nouvelles positions
            double newX = this.getLayoutX() + dx;
            double newY = this.getLayoutY() + dy;

            // Application visuelle
            this.setLayoutX(newX);
            this.setLayoutY(newY);
            this.x = newX;
            this.y = newY;

            // Application physique (HitBox)
            this.Hb.update(newX, newY, currentAngle);
        }
    }

    // On garde setPrefSize pour gérer le redimensionnement éventuel
    @Override
    public void setPrefSize(double w, double h) {
        super.setPrefSize(w, h);
        if (canvas != null) {
            canvas.setWidth(w);
            canvas.setHeight(h);
            dessinerTapis(w, h); // On redessine si la taille change
        }
    }

    public void teleportation(double newX, double newY, double newAngle){
        this.setLayoutX(newX);
        this.setLayoutY(newY);
        this.setRotate(newAngle);

        this.x=newX;
        this.y=newY;

        this.Hb.update(newX,newY,newAngle);
    }
    public double getSurface(){
        return canvas.getHeight()*canvas.getWidth();
    }
    public HitBox getHitBox() { return this.Hb; }
}