package metier;

import java.util.*;

public class PyRat {

    private List<Point> lstFromages;
    private Map<Point, List<Point>> lstPoints;
    private HashSet<Point> lstFromagesMieux;
    private HashMap<Point, HashSet<Point>> lstPointsMieux;
    private ArrayList<Point> chemin;

    /* Méthode appelée une seule fois permettant d'effectuer des traitements "lourds" afin d'augmenter la performace de la méthode turn. */
    public void preprocessing(Map<Point, List<Point>> laby, int labyWidth, int labyHeight, Point position, List<Point> fromages) {
        lstFromagesMieux = new HashSet<>();
        lstPointsMieux = new HashMap<>();
        for(Point p : fromages){
            lstFromagesMieux.add(p);
        }
        for(Point p : laby.keySet()){
            HashSet<Point> lstPassage = new HashSet<>();
            for(Point s : laby.get(p)){
                lstPassage.add(s);
            }
            lstPointsMieux.put(p, lstPassage);
        }
    }

    /* Méthode de test appelant les différentes fonctionnalités à développer.
        @param laby - Map<Point, List<Point>> contenant tout le labyrinthe, c'est-à-dire la liste des Points, et les Points en relation (passages existants)
        @param labyWidth, labyHeight - largeur et hauteur du labyrinthe
        @param position - Point contenant la position actuelle du joueur
        @param fromages - List<Point> contenant la liste de tous les Points contenant un fromage. */
    public void turn(Map<Point, List<Point>> laby, int labyWidth, int labyHeight, Point position, List<Point> fromages) {
        Point pt1 = new Point(2,1);
        Point pt2 = new Point(3,1);
        lstFromages = fromages;
        lstPoints = laby;
        System.out.println((fromageIci(pt1) ? "Il y a un" : "Il n'y a pas de") + " fromage ici, en position " + pt1);
        System.out.println((fromageIci_EnOrdreConstant(pt2) ? "Il y a un" : "Il n'y a pas de") + " fromage ici, en position " + pt2);
        System.out.println((passagePossible(pt1, pt2) ? "Il y a un" : "Il n'y a pas de") + " passage de " + pt1 + " vers " + pt2);
        System.out.println((passagePossible_EnOrdreConstant(pt1, pt2) ? "Il y a un" : "Il n'y a pas de") + " passage de " + pt1 + " vers " + pt2);
        System.out.println("Liste des points inatteignables depuis la position " + position + " : " + pointsInatteignables(position));
    }

    /* Regarde dans la liste des fromages s’il y a un fromage à la position pos.
        @return true s'il y a un fromage à la position pos, false sinon. */
    private boolean fromageIci(Point pos) {
        if (lstFromages.contains(pos)){return true;}
        return false;
    }

    /* Regarde de manière performante (accès en ordre constant) s’il y a un fromage à la position pos.
        @return true s'il y a un fromage à la position pos, false sinon. */
    private boolean fromageIci_EnOrdreConstant(Point pos) {
        if(lstFromagesMieux.contains(pos)){return true;}
        return false;
    }

    /* Indique si le joueur peut passer de la position (du Point) « de » au point « a ».
        @return true s'il y a un passage depuis  « de » vers « a ». */
    private boolean passagePossible(Point de, Point a) {
        if(lstPoints.containsKey(de)){
            if(lstPoints.get(de).contains(a)){
                return true;
            }
        }
        return false;
    }

    /* Indique si le joueur peut passer de la position (du Point) « de » au point « a »,
        mais sans devoir parcourir la liste des Points se trouvant dans la Map !
        @return true s'il y a un passage depuis  « de » vers « a ». */
    private boolean passagePossible_EnOrdreConstant(Point de, Point a) {
        if(lstPointsMieux.containsKey(de)){
            if(lstPointsMieux.get(de).contains(a)){
                return true;
            }
        }
        return false;
    }

    /* Retourne la liste des points qui ne peuvent pas être atteints depuis la position « pos ».
        @return la liste des points qui ne peuvent pas être atteints depuis la position « pos ». */
    private List<Point> pointsInatteignables(Point pos) {
        ArrayList<Point> lstPointsInatteignables = new ArrayList<>();
        chemin = new ArrayList<>();
        pointInatteignablesRecursif(pos);
        for(Point p : lstPoints.keySet()){
            if(!chemin.contains(p)){
                lstPointsInatteignables.add(p);
            }
        }
        return lstPointsInatteignables;
    }

    private void pointInatteignablesRecursif(Point pos){
        chemin.add(pos);
        HashSet<Point> passage = lstPointsMieux.get(pos);
        for(Point p : passage){
            if (!chemin.contains(p)){
                pointInatteignablesRecursif(p);
            }
        }
    }
}