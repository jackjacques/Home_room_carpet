package org.example.tapisjeufamilial;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.example.tapisjeufamilial.collisions.CollisionsManager;
import org.example.tapisjeufamilial.collisions.HitBox;
import org.example.tapisjeufamilial.objects.Meubles;
import org.example.tapisjeufamilial.objects.Tapis;

import java.io.IOException;

public class TapisOrganisation extends Application {

    // Enum pour identifier facilement sur quel mur on pose la porte
    public enum PositionMur { HAUT, BAS, GAUCHE, DROITE }

    // ECHELLE
    public static final double SCALE = 6.0 / 15.0;

    private static final int MARGE_X = (int) (50 * SCALE);
    private static final int MARGE_Y = (int) (50 * SCALE);
    private static final int WIDTH = (int) (2993 * SCALE);
    private static final int HEIGHT = (int) (1531 * SCALE);
    // Facteur de conversion : Si votre pièce fait 6m de large (600cm) et votre canvas 2993px
    // Alors 1 cm = 2993 / 600 = env 5 pixels.
    // A AJUSTER selon la taille réelle de votre pièce représentée par le background !
    // J'utilise 1.0 par défaut pour que vous voyiez les objets, augmentez-le si c'est trop petit.
    double CM_TO_PX = 3.779040404040404;

    @Override
    public void start(Stage stage) throws IOException {

        stage.setTitle("Tapis dans le salon");
        stage.setResizable(false);

        Group root = new Group();
        Scene scene = new Scene(root);

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // 1. DESSIN DU FOND
        dessinerPiece(gc);

        // 2. COLLISIONS (MURS)
        creerMursDeLaPiece();

        // 3. AJOUT DES PORTES (Au choix sur les murs)
        // Note : "position" est la distance depuis le coin haut-gauche
        // "tailleBattant" est la longueur d'une seule porte

        // Exemple 1 : Porte en BAS (au centre X environ)
        ajouterDoublePorte(gc, PositionMur.BAS, WIDTH- 214*CM_TO_PX * SCALE, 290 * SCALE);

        // Exemple 2 : Porte à DROITE (au milieu Y environ)
        ajouterDoublePorte(gc, PositionMur.DROITE, HEIGHT / 2.0, 260 * SCALE);

        // Exemple 3 : Porte à GAUCHE (Plus haut)
        // ajouterDoublePorte(gc, PositionMur.GAUCHE, 300 * SCALE, 70 * SCALE);


        // --- 4. CREATION DES TAPIS ---



        // Tapis 1 : Vierzon (Médaillon escalier) - 207x107 [cite: 3, 4, 5]
        Tapis vierzon = new Tapis(
                500 * SCALE, 350 * SCALE, // Position X, Y (A CHANGER)
                207 * CM_TO_PX * SCALE, 107 * CM_TO_PX * SCALE, // Largeur, Hauteur
                Color.SANDYBROWN // Couleur arbitraire
        );

        // Tapis 2 : Boukhara (Beloutch) - 183x102 [cite: 6, 7, 8]
        Tapis boukhara = new Tapis(
                500 * SCALE, 400 * SCALE,
                183 * CM_TO_PX * SCALE, 102 * CM_TO_PX * SCALE,
                Color.DARKRED
        );

        // Tapis 3 : Riyadh (Laine+soie) - 162x94 [cite: 9, 10, 11]
        Tapis riyadhLaine = new Tapis(
                500 * SCALE, 500 * SCALE,
                162 * CM_TO_PX * SCALE, 94 * CM_TO_PX * SCALE,
                Color.BEIGE
        );

        // Tapis 4 : Hamadan (Bleu+ivoire à médaillon rouge) - 290x170 [cite: 12, 13, 14]
        // Note : C'est le plus grand tapis
        Tapis hamadan = new Tapis(
                500 * SCALE, 300 * SCALE,
                290 * CM_TO_PX * SCALE, 170 * CM_TO_PX * SCALE,
                Color.DARKBLUE
        );

        // Tapis 5 : Riyadh ‘50 (Médaillon framboise) - 206x152 [cite: 15, 16, 17]
        Tapis riyadh50 = new Tapis(
                500 * SCALE, 300 * SCALE,
                206 * CM_TO_PX * SCALE, 152 * CM_TO_PX * SCALE,
                Color.DEEPPINK // Pour "Framboise"
        );

        // Tapis 6 : Riyadh (4 saisons) - 259x151 [cite: 18, 19, 20]
        Tapis riyadh4Saisons = new Tapis(
                400 * SCALE, 350 * SCALE,
                259 * CM_TO_PX * SCALE, 151 * CM_TO_PX * SCALE,
                Color.FORESTGREEN
        );

        // Ajout des meubles (Si besoin, à ajuster)
        Meubles Sofa = new Meubles(20, HEIGHT*0.175 * CM_TO_PX * SCALE, 87 * CM_TO_PX * SCALE, 202*CM_TO_PX * SCALE);
        Meubles Banquette = new Meubles((WIDTH-450), 20,202*CM_TO_PX*SCALE, 70*CM_TO_PX*SCALE);
        Meubles Commode = new Meubles(360*CM_TO_PX*SCALE,20,103*CM_TO_PX*SCALE, 57*CM_TO_PX*SCALE);
        Meubles Vitrine =new Meubles (WIDTH /2-40, HEIGHT - MARGE_Y-53,142*CM_TO_PX*SCALE, 35*CM_TO_PX*SCALE);
        Meubles Secretaire = new Meubles (WIDTH - 150*CM_TO_PX*SCALE,HEIGHT-58*CM_TO_PX*SCALE,87*CM_TO_PX*SCALE,44*CM_TO_PX*SCALE);
        Meubles Cheminee = new Meubles (200 *CM_TO_PX*SCALE,HEIGHT-MARGE_Y-91,172*CM_TO_PX*SCALE,60*CM_TO_PX*SCALE);
        Meubles Mur = new Meubles(316.5*CM_TO_PX*SCALE,HEIGHT-MARGE_Y-110*CM_TO_PX*SCALE,56*CM_TO_PX*SCALE,50*CM_TO_PX*SCALE);

        // AJOUT AU ROOT (Ordre important : Meubles dessous ou dessus)
        root.getChildren().addAll(Sofa, Banquette, Commode, Vitrine,Secretaire, Cheminee, Mur);
        root.getChildren().addAll(vierzon, boukhara, hamadan,riyadhLaine, riyadh50, riyadh4Saisons);
        // --- PREPARATION AUTO-LAYOUT ---
        // 1. On regroupe tous les tapis dans une liste
        java.util.List<Tapis> listeTapis = java.util.Arrays.asList(
                vierzon, boukhara, riyadhLaine, hamadan, riyadh50, riyadh4Saisons
        );

        // 2. On instancie l'algorithme (C'est la SEULE instantiation)
        AutoLayoutController autoLayoutAlgo = new AutoLayoutController(
                listeTapis, WIDTH, HEIGHT, MARGE_X, MARGE_Y
        );
        // --- 5. CONTROLLER ---
        GameController controller = new GameController(scene, autoLayoutAlgo);

        // Rendre TOUS les tapis cliquables
        controller.setSelectedTapis(hamadan); // Sélection par défaut

        vierzon.setOnMouseClicked(e -> controller.setSelectedTapis(vierzon));
        boukhara.setOnMouseClicked(e -> controller.setSelectedTapis(boukhara));
        riyadhLaine.setOnMouseClicked(e -> controller.setSelectedTapis(riyadhLaine));
        hamadan.setOnMouseClicked(e -> controller.setSelectedTapis(hamadan));
        riyadh50.setOnMouseClicked(e -> controller.setSelectedTapis(riyadh50));
        riyadh4Saisons.setOnMouseClicked(e -> controller.setSelectedTapis(riyadh4Saisons));



        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                controller.update();
            }
        };
        gameLoop.start();

        stage.setScene(scene);
        stage.show();
    }

    private void dessinerPiece(GraphicsContext gc) {
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        gc.setFill(Color.BISQUE);
        gc.fillRect(MARGE_X, MARGE_Y, WIDTH - (MARGE_X * 2), HEIGHT - (MARGE_Y * 2));
        gc.setStroke(Color.RED);
        gc.setLineWidth(3);
        gc.strokeRect(MARGE_X, MARGE_Y, WIDTH - (MARGE_X * 2), HEIGHT - (MARGE_Y * 2));

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
        gc.strokeText("Salon Familial", MARGE_X + (20 * SCALE), HEIGHT - (20 * SCALE));
    }

    private void creerMursDeLaPiece() {
        int thickness = (int) (200 * SCALE);
        CollisionsManager.addHitBox(new HitBox(0, -thickness + MARGE_Y, WIDTH, thickness, true, "MUR"));
        CollisionsManager.addHitBox(new HitBox(0, HEIGHT - MARGE_Y, WIDTH, thickness, true, "MUR"));
        CollisionsManager.addHitBox(new HitBox(-thickness + MARGE_X, 0, thickness, HEIGHT, true, "MUR"));
        CollisionsManager.addHitBox(new HitBox(WIDTH - MARGE_X, 0, thickness, HEIGHT, true, "MUR"));
    }

    /**
     * Méthode principale pour ajouter une double porte n'importe où.
     * @param positionSurMur : Si Mur HAUT/BAS -> Coordonnée X. Si Mur GAUCHE/DROITE -> Coordonnée Y.
     */
    private void ajouterDoublePorte(GraphicsContext gc, PositionMur mur, double positionSurMur, double tailleBattant) {

        // 1. Effacer le mur rouge (Trait couleur sol)
        gc.setStroke(Color.BISQUE);
        gc.setLineWidth(4 * SCALE);

        double xGond1 = 0, yGond1 = 0; // Premier battant
        double xGond2 = 0, yGond2 = 0; // Deuxième battant

        // Angles de départ pour les hitboxes (Demi-cercles)
        double startAngle1 = 0, startAngle2 = 0;

        // Configuration selon le mur
        switch (mur) {
            case HAUT:
                // On efface horizontalement
                gc.strokeLine(positionSurMur - tailleBattant, MARGE_Y, positionSurMur + tailleBattant, MARGE_Y);

                // Gond Gauche
                xGond1 = positionSurMur - tailleBattant; yGond1 = MARGE_Y;
                startAngle1 = -90; // Demi-cercle vers la droite (Est)

                // Gond Droit
                xGond2 = positionSurMur + tailleBattant; yGond2 = MARGE_Y;
                startAngle2 = 90; // Demi-cercle vers la gauche (Ouest)
                break;

            case BAS:
                double yBas = HEIGHT - MARGE_Y;
                gc.strokeLine(positionSurMur - tailleBattant, yBas, positionSurMur + tailleBattant, yBas);

                xGond1 = positionSurMur - tailleBattant; yGond1 = yBas;
                startAngle1 = -90; // Vers Droite

                xGond2 = positionSurMur + tailleBattant; yGond2 = yBas;
                startAngle2 = 90; // Vers Gauche
                break;

            case GAUCHE:
                // On efface verticalement
                gc.strokeLine(MARGE_X, positionSurMur - tailleBattant, MARGE_X, positionSurMur + tailleBattant);

                // Gond Haut
                xGond1 = MARGE_X; yGond1 = positionSurMur - tailleBattant;
                startAngle1 = 0; // Vers le Bas (Sud)

                // Gond Bas
                xGond2 = MARGE_X; yGond2 = positionSurMur + tailleBattant;
                startAngle2 = 180; // Vers le Haut (Nord)
                break;

            case DROITE:
                double xDro = WIDTH - MARGE_X;
                gc.strokeLine(xDro, positionSurMur - tailleBattant, xDro, positionSurMur + tailleBattant);

                xGond1 = xDro; yGond1 = positionSurMur - tailleBattant;
                startAngle1 = 0; // Vers le Bas

                xGond2 = xDro; yGond2 = positionSurMur + tailleBattant;
                startAngle2 = 180; // Vers le Haut
                break;
        }

        // 2. Création des deux battants (Visuel + Hitbox)
        creerBattant(gc, xGond1, yGond1, tailleBattant, startAngle1);
        creerBattant(gc, xGond2, yGond2, tailleBattant, startAngle2);
    }

    /**
     * Helper pour créer un seul battant de porte va-et-vient
     * Dessine les lignes, l'arc visuel et crée la HitBox demi-cercle
     */
    private void creerBattant(GraphicsContext gc, double x, double y, double taille, double startAngleHitbox) {

        // --- VISUEL ---
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2 * SCALE);

        // On dessine le trait de la porte (Optionnel pour va-et-vient car elle bouge,
        // mais on peut dessiner la position "ouverte à 90 deg" pour le style)
        // Ici je simplifie : je dessine juste l'arc de balayage complet

        // L'astuce : startAngleHitbox correspond à la direction du demi-cercle.
        // On dessine l'arc visuel correspondant.
        // En JavaFX Shape : 0=Est, 90=Sud, 180=Ouest, 270=Nord
        // En JavaFX Canvas : 0=Est, Positif=Anti-horaire (Maths standard)
        // C'est souvent inversé entre les deux systemes, donc on adapte :

        // Conversion approximative pour le dessin visuel (Canvas)
        double drawStart = 0;
        if(Math.abs(startAngleHitbox - (-90)) < 1) drawStart = 270; // Est -> Start angle Nord draw sweep right
        else if(Math.abs(startAngleHitbox - 90) < 1) drawStart = 90; // Ouest -> Start Sud draw sweep left
        else if(Math.abs(startAngleHitbox - 0) < 1) drawStart = 180;   // Sud -> Start Est
        else if(Math.abs(startAngleHitbox - 180) < 1) drawStart = 0; // Nord -> Start Ouest

        // Dessin Arc (Demi-cercle va et vient)
        // Boite centrée sur le gond (x, y)
        gc.strokeArc(x - taille, y - taille, taille * 2, taille * 2, drawStart, 180, ArcType.ROUND);


        // --- HITBOX ---
        javafx.scene.shape.Arc arcPhysique = new javafx.scene.shape.Arc(x, y, taille, taille, startAngleHitbox, 180);
        arcPhysique.setType(ArcType.ROUND);

        CollisionsManager.addHitBox(new HitBox(arcPhysique, true, "MUR"));

        // Debug (pour voir la hitbox si besoin)
        // gc.setStroke(Color.BLUE);
        // gc.strokeArc(x - taille, y - taille, taille*2, taille*2, drawStart, 180, ArcType.OPEN);
    }

    public static void main(String[] args) {
        launch();
    }
}