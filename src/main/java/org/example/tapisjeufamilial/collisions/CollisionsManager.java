package org.example.tapisjeufamilial.collisions;

import java.util.ArrayList;

public class CollisionsManager {

    private static final ArrayList<HitBox> hitBoxes = new ArrayList<>();

    public static void addHitBox(HitBox hb) {
        hitBoxes.add(hb);
    }

    /**
     * Teste si le mouvement complet (dx, dy) est possible pour une HitBox donnée
     * en prenant en compte sa rotation actuelle (ou future si tu voulais gérer la rotation prédictive).
     */
    public static boolean canMove(HitBox mover, double dx, double dy, double currentAngle) {

        // 1. On sauvegarde l'état actuel pour le remettre après le test
        double oldX = mover.getX();
        double oldY = mover.getY();

        // 2. On SIMULE le mouvement sur la HitBox "fantôme"
        mover.update(oldX + dx, oldY + dy, currentAngle);

        // 3. On vérifie les collisions
        boolean collisionDetected = false;

        for (HitBox other : hitBoxes) {
            if (other == mover) continue; // Pas d'auto-collision
            if (!other.isSolid()) continue;

            // RÈGLE : Tapis traverse Tapis
            if (mover.getType().equals("TAPIS") && other.getType().equals("TAPIS")) {
                continue;
            }

            // Test d'intersection précis (Rotated Rectangle vs Rotated Rectangle)
            if (mover.intersects(other)) {
                collisionDetected = true;
                break; // On a trouvé un obstacle, pas la peine de continuer
            }
        }

        // 4. TRES IMPORTANT : On remet la HitBox à sa place d'origine !
        // (C'est le Tapis.java qui validera le mouvement final si true est renvoyé)
        mover.update(oldX, oldY, currentAngle);

        return !collisionDetected; // Si pas de collision, return true (on peut bouger)
    }
    /**
     * Vérifie si une HitBox est dans une position valide (ne touche pas de MUR ou MEUBLE).
     * Ignore les autres TAPIS.
     */
    public static boolean isValidPosition(HitBox toCheck) {
        for (HitBox other : hitBoxes) {
            if (other == toCheck) continue; // Pas d'auto-collision
            if (!other.isSolid()) continue; // Ignore les objets non solides (si y'en a)

            // REGLE D'OR : Les tapis s'ignorent
            if (toCheck.getType().equals("TAPIS") && other.getType().equals("TAPIS")) {
                continue;
            }

            // Si ça touche un MEUBLE ou un MUR, c'est invalide
            if (toCheck.intersects(other)) {
                return false;
            }
        }
        return true; // Aucune collision interdite trouvée
    }
}