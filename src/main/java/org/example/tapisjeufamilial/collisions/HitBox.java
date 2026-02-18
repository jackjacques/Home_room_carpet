package org.example.tapisjeufamilial.collisions;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class HitBox {

    // On change le type de Rectangle à Shape (plus générique)
    private Shape bounds;
    private boolean solid;
    private String type;

    // --- CONSTRUCTEUR 1 : Pour les objets rectangulaires (Tapis, Meubles, Murs droits) ---
    // (On garde celui-ci pour ne pas casser tout ton code existant)
    public HitBox(double x, double y, double width, double height, boolean solid, String type) {
        this.solid = solid;
        this.type = type;
        this.bounds = new Rectangle(x, y, width, height);
    }

    // --- CONSTRUCTEUR 2 (NOUVEAU) : Pour les formes complexes (Portes arrondies) ---
    public HitBox(Shape shape, boolean solid, String type) {
        this.solid = solid;
        this.type = type;
        this.bounds = shape; // On utilise la forme qu'on nous donne (ex: un Arc)
    }

    public boolean intersects(HitBox other) {
        Shape intersection = Shape.intersect(this.bounds, other.bounds);
        return intersection.getBoundsInLocal().getWidth() != -1;
    }

    // --- MISE A JOUR ---
    // Cette méthode est utilisée UNIQUEMENT par les Tapis qui bougent.
    // Comme les tapis sont toujours des Rectangles, on peut "caster" sans risque.
    public void update(double x, double y, double angle) {
        if (this.bounds instanceof Rectangle) {
            Rectangle r = (Rectangle) this.bounds;
            r.setX(x);
            r.setY(y);
            r.setRotate(angle);
        }
    }

    // Getters
    public double getX() { return bounds.getBoundsInLocal().getMinX(); } // Approximation pour le manager
    public double getY() { return bounds.getBoundsInLocal().getMinY(); }
    public boolean isSolid() { return solid; }
    public String getType() { return type; }
}