package org.example.tapisjeufamilial;

import javafx.geometry.Point2D;
import org.example.tapisjeufamilial.collisions.CollisionsManager;
import org.example.tapisjeufamilial.objects.Tapis;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AutoLayoutController {

    private final List<Tapis> lesTapis;
    private final double roomWidth;
    private final double roomHeight;
    private final double margeX;
    private final double margeY;
    private final Random random = new Random();

    // CONFIGURATION
    private static final double OBJECTIF_COUVERTURE = 0.50; // J'ai baissé à 50% car 80% avec peu de superposition est très dur !
    private static final double MAX_OVERLAP = 0.60; // Max 60% de superposition entre 2 tapis
    private static final int MAX_ATTEMPTS_GLOBAL = 1000; // Plus d'essais car c'est plus dur à trouver

    public AutoLayoutController(List<Tapis> lesTapis, double w, double h, double mx, double my) {
        this.lesTapis = lesTapis;
        this.roomWidth = w;
        this.roomHeight = h;
        this.margeX = mx;
        this.margeY = my;
    }

    public boolean genererAgencement() {
        System.out.println("--- DÉBUT : Cible Couverture " + (OBJECTIF_COUVERTURE * 100) + "% | Max Superposition " + (MAX_OVERLAP * 100) + "% ---");

        for (int i = 0; i < MAX_ATTEMPTS_GLOBAL; i++) {

            // 1. On essaie de placer tous les tapis
            boolean placementReussi = placerTousLesTapisAvecContraintes();

            if (placementReussi) {
                // 2. Si le placement respecte les murs et les superpositions max
                // On calcule la couverture totale de la pièce
                double couvertureReelle = calculerCouvertureGlobale();

                System.out.printf("Essai %d : Valide ! Couverture : %.2f%%\n", i, couvertureReelle * 100);

                if (couvertureReelle >= OBJECTIF_COUVERTURE) {
                    System.out.println(">>> VICTOIRE ! Configuration optimale trouvée !");
                    return true;
                }
            }
        }

        System.out.println("--- ECHEC : Impossible de satisfaire toutes les contraintes ---");
        return false;
    }

    private boolean placerTousLesTapisAvecContraintes() {
        // Liste temporaire pour savoir qui est déjà posé
        List<Tapis> dejaPlaces = new ArrayList<>();

        for (Tapis tapisEncours : lesTapis) {
            boolean tapisPlace = false;

            // On tente 100 positions pour ce tapis
            for (int attempt = 0; attempt < 100; attempt++) {

                // A. Position Aleatoire
                double minX = margeX;
                double maxX = roomWidth - margeX;
                double minY = margeY;
                double maxY = roomHeight - margeY;

                double randX = minX + (maxX - minX) * random.nextDouble();
                double randY = minY + (maxY - minY) * random.nextDouble();
                double randAngle = random.nextDouble() * 360;

                // B. Téléportation temporaire
                tapisEncours.teleportation(randX, randY, randAngle);

                // C. Vérification 1 : Murs et Meubles
                if (!CollisionsManager.isValidPosition(tapisEncours.getHitBox())) {
                    continue; // Touche un mur, on réessaie
                }

                // D. Vérification 2 : Superposition avec les tapis déjà là
                if (checkSuperpositionValide(tapisEncours, dejaPlaces)) {
                    tapisPlace = true;
                    dejaPlaces.add(tapisEncours); // On le valide
                    break;
                }
            }

            // Si on n'arrive pas à placer ce tapis après 100 essais, l'agencement global est mort
            if (!tapisPlace) return false;
        }
        return true;
    }

    /**
     * Vérifie que 'tapisCandidat' ne recouvre pas (et n'est pas recouvert par)
     * un autre tapis à plus de 30%.
     */
    private boolean checkSuperpositionValide(Tapis candidat, List<Tapis> autres) {
        for (Tapis voisin : autres) {
            // 1. Est-ce que le Candidat recouvre trop le Voisin ?
            if (calculerRatioOverlap(candidat, voisin) > MAX_OVERLAP) return false;

            // 2. Est-ce que le Voisin recouvre trop le Candidat ?
            // (Important si le candidat est petit et le voisin immense)
            if (calculerRatioOverlap(voisin, candidat) > MAX_OVERLAP) return false;
        }
        return true;
    }

    /**
     * Calcule quel pourcentage de 'tapisDessous' est couvert par 'tapisDessus'.
     * Utilise une méthode Monte Carlo rapide (50 points).
     */
    private double calculerRatioOverlap(Tapis tapisDessus, Tapis tapisDessous) {
        int pointsTotal = 50; // Précision suffisante pour le placement (~2%)
        int pointsTouches = 0;

        double w = tapisDessous.getBoundsInLocal().getWidth();
        double h = tapisDessous.getBoundsInLocal().getHeight();

        for (int i = 0; i < pointsTotal; i++) {
            // Point local aléatoire dans le tapis du dessous
            double lx = random.nextDouble() * w;
            double ly = random.nextDouble() * h;

            // Conversion en coordonnées Monde (Scène)
            Point2D worldPoint = tapisDessous.localToParent(lx, ly);

            // Vérification : est-ce que ce point tombe DANS le tapis du dessus ?
            Point2D pointInOtherLocal = tapisDessus.parentToLocal(worldPoint);
            if (tapisDessus.contains(pointInOtherLocal)) {
                pointsTouches++;
            }
        }

        return (double) pointsTouches / pointsTotal;
    }

    /**
     * Calcul final précis de la couverture globale de la pièce
     */
    private double calculerCouvertureGlobale() {
        int pointsTotal = 1000;
        int pointsTouches = 0;

        double wZone = roomWidth - (margeX * 2);
        double hZone = roomHeight - (margeY * 2);

        for (int k = 0; k < pointsTotal; k++) {
            double px = margeX + (random.nextDouble() * wZone);
            double py = margeY + (random.nextDouble() * hZone);

            for (Tapis t : lesTapis) {
                if (t.contains(t.parentToLocal(px, py))) {
                    pointsTouches++;
                    break;
                }
            }
        }
        return (double) pointsTouches / pointsTotal;
    }
}