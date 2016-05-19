package kuutiopc;

import java.awt.Color;
import java.util.ArrayList;

// Rubikin 4x4x4 kuution mallinnus.
// Kuution koostuu kuudesta sivusta (sisäluokka).
// Tehtävää:
// - sivujen mallinnus
// - sivujen mahdolliset käännökset
// - kuutio osaa kääntyä ratkaisusarjan mukaan
// - käännökset palauttavat siirtokomennon, joka on transmittable EDIT: jokainen kuutio tietää komennon, jolla tilaan päästiin
// - kuutio päättelee onko se ratkaistu (välietappiin asti)
// - metodi, joka asettaa värin tiettyyn sivuun & palaseen (webkamera tai käsin naputtelu)

public class Kuutio {

    // Eri ratkaisuvaiheissa tarvittavat siirrot
    
    public static final String[] movesAll = {
        "U", "u", "Uu", "U'", "u'", "Uu'", "U2", "u2", "Uu2",
        "D", "d", "Dd", "D'", "d'", "Dd'", "D2", "d2", "Dd2",
        "L", "l", "Ll", "L'", "l'", "Ll'", "L2", "l2", "Ll2",
        "R", "r", "Rr", "R'", "r'", "Rr'", "R2", "r2", "Rr2",
        "F", "f", "Ff", "F'", "f'", "Ff'", "F2", "f2", "Ff2",
        "B", "b", "Bb", "B'", "b'", "Bb'", "B2", "b2", "Bb2",};
    
    public static final String[] movesStage1 = {
        "R", "L", "F", "B", "U", "D", "r", "l", "f", "b", "u", "d"
    };
    
    public static final String[] movesStage2 = {
        "R", "L", "F", "B", "U", "D", "r", "l", "f2", "b2", "u2", "d2"
    };
    
    public static final String[] movesStage3 = {
        "R2", "L2", "F", "B", "U", "D", "r2", "l2", "f2", "b2", "u2", "d2"
    };
    
    public static final String[] movesStage4 = {
        "R2", "L2", "F2", "B2", "U", "D", "r2", "l2", "f2", "b2"
    };
    
    public static final String[] movesStage5 = { // Thistlethwaite 1
        "R", "L", "F", "B", "U", "D"
    };
    
    public static final String[] movesStage6 = { // Thistlethwaite 2
        "R2", "L2", "F", "B", "U", "D"
    };
    
    public static final String[] movesStage7 = { // Thistlethwaite 3
        "R2", "L2", "F2", "B2", "U", "D"
    };
    
    public static final String[] movesStage8 = { // Thistlethwaite 4
        "R2", "L2", "F2", "B2", "U2", "D2"
    };
    
    // Määritetään kuution sivut ja palasten värit
    
    public static final int ORANSSI = 1;
    public static final int PUNAINEN = 2;
    public static final int VIHREÄ = 3;
    public static final int VALKOINEN = 5;
    public static final int SININEN = 7;
    public static final int KELTAINEN = 11;
    
    public static final int ALA = 0;
    public static final int ETU = 1;
    public static final int OIKEA = 2;
    public static final int TAKA = 3;
    public static final int VASEN = 4;
    public static final int YLÄ = 5;
    
    // Määritetään nurkkapalat
    
    public static final int ULB = ORANSSI + VALKOINEN + VIHREÄ;     // 9
    public static final int ULF = ORANSSI + VALKOINEN + SININEN;    // 13
    public static final int URF = ORANSSI + KELTAINEN + SININEN;    // 15
    public static final int URB = ORANSSI + KELTAINEN + VIHREÄ;     // 19
    public static final int DLB = PUNAINEN + VALKOINEN + VIHREÄ;    // 10
    public static final int DLF = PUNAINEN + VALKOINEN + SININEN;   // 14
    public static final int DRF = PUNAINEN + KELTAINEN + SININEN;   // 16
    public static final int DRB = PUNAINEN + KELTAINEN + VIHREÄ;    // 20
    
    public static final int[] corners = {ULB, ULF, URF, URB, DLB, DLF, DRF, DRB};
    
    // Määritetään alemmat reunapalat (low)
    
    public static final int UB = ORANSSI * VIHREÄ;      // 3
    public static final int UL = ORANSSI * VALKOINEN;   // 5
    public static final int UF = ORANSSI * SININEN;     // 7
    public static final int UR = ORANSSI * KELTAINEN;   // 11
    public static final int LB = VALKOINEN * VIHREÄ;    // 15
    public static final int LF = VALKOINEN * SININEN;   // 35
    public static final int RF = KELTAINEN * SININEN;   // 77
    public static final int RB = KELTAINEN * VIHREÄ;    // 33
    public static final int DB = PUNAINEN * VIHREÄ;     // 6
    public static final int DL = PUNAINEN * VALKOINEN;  // 10
    public static final int DF = PUNAINEN * SININEN;    // 14
    public static final int DR = PUNAINEN * KELTAINEN;  // 22
    
    public static final int[] edges = {UB, UL, UR, UF, DF, DL, DR, DB, LB, LF, RB, RF};
    
    // Määritetään ylemmät reunapalat (high)
    
    public static final int UB2 = 100 + ORANSSI * VIHREÄ;      // 103
    public static final int UL2 = 100 + ORANSSI * VALKOINEN;   // 105
    public static final int UF2 = 100 + ORANSSI * SININEN;     // 107
    public static final int UR2 = 100 + ORANSSI * KELTAINEN;   // 111
    public static final int LB2 = 100 + VALKOINEN * VIHREÄ;    // 115
    public static final int LF2 = 100 + VALKOINEN * SININEN;   // 135
    public static final int RF2 = 100 + KELTAINEN * SININEN;   // 177
    public static final int RB2 = 100 + KELTAINEN * VIHREÄ;    // 133
    public static final int DB2 = 100 + PUNAINEN * VIHREÄ;     // 106
    public static final int DL2 = 100 + PUNAINEN * VALKOINEN;  // 110
    public static final int DF2 = 100 + PUNAINEN * SININEN;    // 114
    public static final int DR2 = 100 + PUNAINEN * KELTAINEN;  // 122
    
    public static final int[] edges2 = {UB2, UL2, UR2, UF2, DF2, DL2, DR2, DB2, LB2, LF2, RB2, RF2};
    
    private static KuutioDAO dao = new KuutioDAO();
    
    private Sivu[] sivut;
    private String komento;
    
    public static int good = 0;
    public static int bad = 0;

    public Kuutio() {
        sivut = new Sivu[6];

        sivut[ALA] = new Sivu(PUNAINEN);
        sivut[ETU] = new Sivu(SININEN);
        sivut[OIKEA] = new Sivu(KELTAINEN);
        sivut[TAKA] = new Sivu(VIHREÄ);
        sivut[VASEN] = new Sivu(VALKOINEN);
        sivut[YLÄ] = new Sivu(ORANSSI);

        komento = null;
    }

    public Kuutio(Kuutio k) {
        sivut = new Sivu[6];

        for (int i = 0; i < sivut.length; i++) {
            sivut[i] = new Sivu(k.sivut[i]);
        }

        komento = null;
    }
    
    public boolean kuutioOK() {
        int siniTot = 0;
        int punaTot = 0;
        int keltTot = 0;
        int vihrTot = 0;
        int oranTot = 0;
        int valkTot = 0;
        
        for (int i = 0 ; i < sivut.length ; i++) {
            for (int j = 0 ; j < 4 ; j++) {
                for (int k = 0 ; k < 4 ; k++) {
                    switch (sivut[i].palaset[j][k]) {
                        case SININEN:   siniTot++; break;
                        case PUNAINEN:  punaTot++; break;
                        case KELTAINEN: keltTot++; break;
                        case VIHREÄ:    vihrTot++; break;
                        case ORANSSI:   oranTot++; break;
                        case VALKOINEN: valkTot++; break;
                    }
                }
            }
        }
        
        System.out.println(siniTot + " " + punaTot + " " + keltTot + " " + vihrTot + " " + oranTot + " " + valkTot);
        
        if (siniTot > 16 || punaTot > 16 || keltTot > 16 || vihrTot > 16 || oranTot > 16 || valkTot > 16) {
            return false;
        } else {
            return true;
        }
    }
    
    public void fixAla() {
        sivut[ALA].käännäVastapäivään();
    }
    
    public void fixYla() {
        sivut[YLÄ].käännäVastapäivään();
    }

    public void setKomento(String s) {
        komento = s;
    }

    public String getKomento() {
        return komento;
    }
    
    public void setSivu(int sivu, Color[][] colors) {
        sivut[sivu].setColors(colors);
    }

    public Color[][] getColors(int sivu) {
        return sivut[sivu].getColors();
    }

    public static KuutioDAO getDAO() {
        return dao;
    }
    
    // Metodi palauttaa reunapalan taulukkomuodossa halutusta paikasta,
    // parametrina käytetään edellä määritettyjä vakioarvoja
    
    private int[] getEdgePiece(int piece) {
        int[] p = new int[2];

        switch (piece) {
            // low
            case UB:
                p[0] = sivut[YLÄ].palaset[1][0];
                p[1] = sivut[TAKA].palaset[2][0];
                break;
            case UL:
                p[0] = sivut[YLÄ].palaset[0][2];
                p[1] = sivut[VASEN].palaset[2][0];
                break;
            case UF:
                p[0] = sivut[YLÄ].palaset[2][3];
                p[1] = sivut[ETU].palaset[2][0];
                break;
            case UR:
                p[0] = sivut[YLÄ].palaset[3][1];
                p[1] = sivut[OIKEA].palaset[2][0];
                break;
            case LB:
                p[0] = sivut[VASEN].palaset[0][2];
                p[1] = sivut[TAKA].palaset[3][2];
                break;
            case LF:
                p[0] = sivut[VASEN].palaset[3][1];
                p[1] = sivut[ETU].palaset[0][1];
                break;
            case RF:
                p[0] = sivut[OIKEA].palaset[0][2];
                p[1] = sivut[ETU].palaset[3][2];
                break;
            case RB:
                p[0] = sivut[OIKEA].palaset[3][1];
                p[1] = sivut[TAKA].palaset[0][1];
                break;
            case DB:
                p[0] = sivut[ALA].palaset[2][3];
                p[1] = sivut[TAKA].palaset[1][3];
                break;
            case DL:
                p[0] = sivut[ALA].palaset[0][2];
                p[1] = sivut[VASEN].palaset[1][3];
                break;
            case DF:
                p[0] = sivut[ALA].palaset[1][0];
                p[1] = sivut[ETU].palaset[1][3];
                break;
            case DR:
                p[0] = sivut[ALA].palaset[3][1];
                p[1] = sivut[OIKEA].palaset[1][3];
                break;
            // high
            case UB2:
                p[0] = sivut[YLÄ].palaset[2][0];
                p[1] = sivut[TAKA].palaset[1][0];
                break;
            case UL2:
                p[0] = sivut[YLÄ].palaset[0][1];
                p[1] = sivut[VASEN].palaset[1][0];
                break;
            case UF2:
                p[0] = sivut[YLÄ].palaset[1][3];
                p[1] = sivut[ETU].palaset[1][0];
                break;
            case UR2:
                p[0] = sivut[YLÄ].palaset[3][2];
                p[1] = sivut[OIKEA].palaset[1][0];
                break;
            case LB2:
                p[0] = sivut[VASEN].palaset[0][1];
                p[1] = sivut[TAKA].palaset[3][1];
                break;
            case LF2:
                p[0] = sivut[VASEN].palaset[3][2];
                p[1] = sivut[ETU].palaset[0][2];
                break;
            case RF2:
                p[0] = sivut[OIKEA].palaset[0][1];
                p[1] = sivut[ETU].palaset[3][1];
                break;
            case RB2:
                p[0] = sivut[OIKEA].palaset[3][2];
                p[1] = sivut[TAKA].palaset[0][2];
                break;
            case DB2:
                p[0] = sivut[ALA].palaset[1][3];
                p[1] = sivut[TAKA].palaset[2][3];
                break;
            case DL2:
                p[0] = sivut[ALA].palaset[0][1];
                p[1] = sivut[VASEN].palaset[2][3];
                break;
            case DF2:
                p[0] = sivut[ALA].palaset[2][0];
                p[1] = sivut[ETU].palaset[2][3];
                break;
            case DR2:
                p[0] = sivut[ALA].palaset[3][2];
                p[1] = sivut[OIKEA].palaset[2][3];
                break;
        }

        return p;
    }

    // Metodi palauttaa nurkkapalan taulukkomuodossa halutusta paikasta,
    // parametrina käytetään edellä määritettyjä vakioarvoja
    
    private int[] getCornerPiece(int piece) {
        int[] p = new int[3];

        switch (piece) {
            case ULB:
                p[0] = sivut[YLÄ].palaset[0][0];
                p[1] = sivut[VASEN].palaset[0][0];
                p[2] = sivut[TAKA].palaset[3][0];
                break;
            case ULF:
                p[0] = sivut[YLÄ].palaset[0][3];
                p[1] = sivut[VASEN].palaset[3][0];
                p[2] = sivut[ETU].palaset[0][0];
                break;
            case URF:
                p[0] = sivut[YLÄ].palaset[3][3];
                p[1] = sivut[OIKEA].palaset[0][0];
                p[2] = sivut[ETU].palaset[3][0];
                break;
            case URB:
                p[0] = sivut[YLÄ].palaset[3][0];
                p[1] = sivut[OIKEA].palaset[3][0];
                p[2] = sivut[TAKA].palaset[0][0];
                break;
            case DLB:
                p[0] = sivut[ALA].palaset[0][3];
                p[1] = sivut[VASEN].palaset[0][3];
                p[2] = sivut[TAKA].palaset[3][3];
                break;
            case DLF:
                p[0] = sivut[ALA].palaset[0][0];
                p[1] = sivut[VASEN].palaset[3][3];
                p[2] = sivut[ETU].palaset[0][3];
                break;
            case DRF:
                p[0] = sivut[ALA].palaset[3][0];
                p[1] = sivut[OIKEA].palaset[0][3];
                p[2] = sivut[ETU].palaset[3][3];
                break;
            case DRB:
                p[0] = sivut[ALA].palaset[3][3];
                p[1] = sivut[OIKEA].palaset[3][3];
                p[2] = sivut[TAKA].palaset[0][3];
                break;
        }

        return p;
    }

    // Metodi palauttaa kaikki sen 'naapurit', eli tilasiirtymät,
    // ratkaisualgoritmi käyttää tätä
    
    public Kuutio[] getNeighbors(int stage) {
        switch (stage) {
            case 1: return getNeighbors(movesStage1);
            case 2: return getNeighbors(movesStage2);
            case 3: return getNeighbors(movesStage3);
            case 4: return getNeighbors(movesStage4);
            case 5: return getNeighbors(movesStage5);
            case 6: return getNeighbors(movesStage6);
            case 7: return getNeighbors(movesStage7);
            case 8: return getNeighbors(movesStage8);
            case 9: return getNeighbors(movesStage7);
            default: return getNeighbors(movesStage7);
        }
    }

    private Kuutio[] getNeighbors(String[] moves) {
        Kuutio[] kuutio = new Kuutio[moves.length];

        for (int i = 0; i < kuutio.length; i++) {
            kuutio[i] = new Kuutio(this);
        }

        for (int i = 0; i < kuutio.length; i++) {
            kuutio[i].käännäSivua(moves[i]);
            kuutio[i].setKomento(moves[i]);
        }

        return kuutio;
    }

    // Metodilla tarkastetaan onko kuutio ratkaistu tiettyyn vaiheeseen (1 - 8) saakka
    
    public boolean goalFound(int stage) {
        switch (stage) {
            case 1: return goalFoundStage1();
            case 2: return goalFoundStage2();
            case 3: return goalFoundStage3();
            case 4: return goalFoundStage4();
            case 5: return goalFoundStage5();
            case 6: return goalFoundStage6();
            case 7: return goalFoundStage7();
            case 8: return goalFoundStage8();
            case 9: return cornersSolved();
            default: return edgesSolved();
        }
    }

    // Vasemman ja oikean sivun keskuspalojen pitää olla valkoisia tai keltaisia
    
    private boolean goalFoundStage1() {
        for (int i = 1; i < 3; i++) {
            for (int j = 1; j < 3; j++) {
                if ((sivut[VASEN].palaset[i][j] != VALKOINEN && sivut[VASEN].palaset[i][j] != KELTAINEN)
                        || (sivut[OIKEA].palaset[i][j] != VALKOINEN && sivut[OIKEA].palaset[i][j] != KELTAINEN)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean goalFoundStage2() {
        
        if (!lowAndHighEdgesOrientedCorrectly()) {
            return false;
        }

        for (int i = 1; i < 3; i++) {
            for (int j = 1; j < 3; j++) {
                if ((sivut[ETU].palaset[i][j] != SININEN && sivut[ETU].palaset[i][j] != VIHREÄ)
                        || (sivut[TAKA].palaset[i][j] != SININEN && sivut[TAKA].palaset[i][j] != VIHREÄ)) {
                    return false;
                }
            }
        }

        for (int i = 1; i < 3; i++) {
            for (int j = 1; j < 3; j++) {
                if ((sivut[YLÄ].palaset[i][j] != PUNAINEN && sivut[YLÄ].palaset[i][j] != ORANSSI)
                        || (sivut[ALA].palaset[i][j] != PUNAINEN && sivut[ALA].palaset[i][j] != ORANSSI)) {
                    return false;
                }
            }
        }
        
        if (!leftAndRightCentersCorrect()) {
            return false;
        }
        
        if (!lowAndHighEdgeParitiesMatch()) {
            return false;
        }

        return true;
    }
    
    private boolean lowAndHighEdgesOrientedCorrectly() {
        boolean correctly = false;
        
        return correctly;
    }
    
    private boolean leftAndRightCentersCorrect() {
        boolean correct = false;
        
        // CORRECT POSITIONS (left and right centers, 12 total)
        
        // WW oo    oo WW     oo WW
        // WW oo    oo WW     WW oo
        
        // WW oo    Wo Wo     oW oW
        // oo WW    Wo Wo     oW oW
        
        // Wo oW    oW Wo     WW WW
        // Wo oW    oW Wo     oo oo
        
        // oo oo    Wo oW     oW Wo
        // WW WW    oW Wo     Wo oW
        
        if (sivut[VASEN].palaset[1][1] == VALKOINEN) {
            if (sivut[VASEN].palaset[1][2] == VALKOINEN) {
                if (sivut[VASEN].palaset[2][1] == VALKOINEN && sivut[VASEN].palaset[2][2] == VALKOINEN) {
                    correct = true;
                } else if (sivut[OIKEA].palaset[1][1] == VALKOINEN && sivut[OIKEA].palaset[1][2] == VALKOINEN) {
                    correct = true;
                } else if (sivut[OIKEA].palaset[2][1] == VALKOINEN && sivut[OIKEA].palaset[2][2] == VALKOINEN) {
                    correct = true;
                }
            } else if (sivut[VASEN].palaset[2][1] == VALKOINEN) {
                if (sivut[OIKEA].palaset[1][1] == VALKOINEN && sivut[OIKEA].palaset[2][1] == VALKOINEN) {
                    correct = true;
                } else if (sivut[OIKEA].palaset[1][2] == VALKOINEN && sivut[OIKEA].palaset[2][2] == VALKOINEN) {
                    correct = true;
                }
            } else if (sivut[VASEN].palaset[2][2] == VALKOINEN) {
                if (sivut[OIKEA].palaset[2][1] == VALKOINEN && sivut[OIKEA].palaset[1][2] == VALKOINEN) {
                    correct = true;
                }
            }
        } else if (sivut[VASEN].palaset[1][2] == VALKOINEN) {
            if (sivut[VASEN].palaset[2][2] == VALKOINEN) {
                if (sivut[OIKEA].palaset[1][2] == VALKOINEN && sivut[OIKEA].palaset[2][2] == VALKOINEN) {
                    correct = true;
                } else if (sivut[OIKEA].palaset[1][1] == VALKOINEN && sivut[OIKEA].palaset[2][1] == VALKOINEN) {
                    correct = true;
                }
            } else if (sivut[VASEN].palaset[2][1] == VALKOINEN) {
                if (sivut[OIKEA].palaset[1][1] == VALKOINEN && sivut[OIKEA].palaset[2][2] == VALKOINEN) {
                    correct = true;
                }
            }
        } else if (sivut[VASEN].palaset[2][1] == VALKOINEN) {
            if (sivut[VASEN].palaset[2][2] == VALKOINEN) {
                if (sivut[OIKEA].palaset[1][1] == VALKOINEN && sivut[OIKEA].palaset[1][2] == VALKOINEN) {
                    correct = true;
                } else if (sivut[OIKEA].palaset[2][1] == VALKOINEN && sivut[OIKEA].palaset[2][2] == VALKOINEN) {
                    correct = true;
                }
            }
        } else if (sivut[VASEN].palaset[2][2] != VALKOINEN) {
            correct = true;
        }
        
        return correct;
    }
    
    public boolean lowAndHighEdgeParitiesMatch() {
        return getPermutationParity(getLowEdgePermutation()) == getPermutationParity(getHighEdgePermutation());
    }

    private boolean goalFoundStage3() {

        // Math 4 edge pairs and place them at BR, BL, FL, FR

        // Put R and L centers in columns

        // Put F and B centers in columns

        return false; // TBA
    }

    private boolean goalFoundStage4() {

        // Pair up the remaining edges

        // Solve the centers

        // Match the edges and corners permutation parities

        return false; // TBA
    }

    // tarkistetaan jokaisen 12 reunapalan 'hyvyys', eli onko ne mahdollista siirtää
    // oikeille paikoille käyttämällä vaiheen 6 siirtoja
    
    private boolean goalFoundStage5() { // Thistlethwaite 1
        boolean found = true;
        
        for (int i = 0; i < edges.length; i++) {
            if (!goodEdgePiece(getEdgePiece(edges[i]))) {
                found = false;
                break;
            }
        }

        return found;
    }

    // Siirretään palat LB, LF, RF ja RB oikealle kerrokselle
    // Käännetään nurkat oikein päin
    
    private boolean goalFoundStage6() {

        boolean found = false;

        if (isELayerPiece(getEdgePiece(LB))
                && isELayerPiece(getEdgePiece(LF))
                && isELayerPiece(getEdgePiece(RF))
                && isELayerPiece(getEdgePiece(RB))) {

            found = true;

            for (int i = 0; i < corners.length; i++) {
                if (getCornerOrientation(getCornerPiece(corners[i])) != 1) {
                    found = false;
                    break;
                }
            }
        }

        return found;
    }

    // Siirretään loput reunapalat oikeille kerroksille
    // Siirretään nurkkapalat yhteen 96:sta asennosta, jossa ne voidaan
    // ratkaista vaiheessa 8 (asennot löytyvät tietokannasta)
    
    private boolean goalFoundStage7() {

        // Put M layer edges into M layer and S layer edges into S layer
        // (M layer is the one between R and L, S layer is the one between F and B)

        // Put corners into their correct tetrads. Make the parity of the edge (and corner)
        // permutation even, fix the total twist of each tetrad
        
        boolean found = false;

        if (   isMLayerPiece(getEdgePiece(UB))
            && isMLayerPiece(getEdgePiece(UF))
            && isMLayerPiece(getEdgePiece(DB))
            && isMLayerPiece(getEdgePiece(DF))
            && dao.getCorner(this.getCornerPermutation())) {

            found = true;
        }

        return found;
    }

    // Tämän vaiheen jälkeen koko kuutio on ratkaistu!
    
    private boolean goalFoundStage8() {
        boolean found = true;

        // reunapalat ovat oikeilla paikoilla
        for (int i = 0; i < edges.length; i++) {
            int[] e = getEdgePiece(edges[i]);
            if (e[0] * e[1] != edges[i]) {
                found = false;
                break;
            }
        }

        // ylänurkat ovat oikeilla paikoilla ja oikeissa asennoissa
        if (found) {
            int[] top = {ULB, ULF, URF, URB};

            for (int i = 0; i < top.length; i++) {
                int[] c = getCornerPiece(top[i]);

                if (c[0] != ORANSSI || c[0] + c[1] + c[2] != top[i]) {
                    found = false;
                    break;
                }
            }
        }

        // alanurkat ovat oikeilla paikoilla ja oikeissa asennoissa
        if (found) {
            int[] bot = {DLB, DLF, DRF, DRB};

            for (int i = 0; i < bot.length; i++) {
                int[] c = getCornerPiece(bot[i]);

                if (c[0] != PUNAINEN || c[0] + c[1] + c[2] != bot[i]) {
                    found = false;
                    break;
                }
            }
        }

        return found;
    }

    // "Stage 9"
    public boolean cornersSolved() {
        if (dao.getCorner(this.getCornerPermutation())) {
            return true;
        } else {
            return false;
        }
    }
    
    // "Stage 10"
    public boolean edgesSolved() {
        boolean found = false;

        if (   isMLayerPiece(getEdgePiece(UB))
            && isMLayerPiece(getEdgePiece(UF))
            && isMLayerPiece(getEdgePiece(DB))
            && isMLayerPiece(getEdgePiece(DF))
            && getPermutationParity(getCornerPermutation())) {

            found = true;
        }

        return found;
    }
    
    // http://heuristicswiki.wikispaces.com/Rubik%27s+cube
    
    // Metodi palauttaa eri ratkaisuvaiheissa käytettävän heuristiikka-arvion,
    // jonka tarkoituksena on kertoa, kuinka monta siirtoa kyseisestä tilasta on
    // vaiheen ratkaisuun. Tarkat arvot on taulukoitu tietokantaan.
    
    public int getHeuristic(int stage) {
        switch (stage) {
            case 1: return getHeuristicStage1();
            case 2: return getHeuristicStage2();
            case 3: return getHeuristicStage3();
            case 4: return getHeuristicStage4();
            case 5: return getHeuristicStage5();
            case 6: return getHeuristicStage6();
            case 7: return getHeuristicStage7();
            case 8: return getHeuristicStage8();
            case 9: 
                int heuristic = dao.get(this.getCode(stage), stage);
                if (heuristic != -1) {
                    return heuristic;
                } else {
                    return 10;
                }
                
            default: return 1;
        }
    }

    private int getHeuristicStage1() {
        
        int heuristic = dao.get(this.getCode(1), 1);

        if (heuristic != -1) {
            return heuristic;
        } else {
            return 7;
        }
    }

    private int getHeuristicStage2() {
        return 0; //TBA
    }

    private int getHeuristicStage3() {
        return 0; //TBA
    }

    private int getHeuristicStage4() {
        return 0; //TBA
    }

    private int getHeuristicStage5() { // Thistlethwaite 1

        int heuristic = dao.get(this.getCode(5), 5);
        
        if (heuristic != -1) {
            return heuristic;
        } else {
            return 1;
        }
    }

    private int getHeuristicStage6() { // Thisthethwaite 2

        int heuristic = dao.get(this.getCode(6), 6);
        
        if (heuristic != -1) {
            return heuristic;
        } else {
            return 8;
        }
    }

    private int getHeuristicStage7() { // Thistlethwaite 3
        int heuristic = dao.get(this.getCode(7), 7);
        
        if (heuristic != -1) {
            ++good;
            return heuristic;
        } else {
            ++bad;
            int h1 = dao.get(this.getCode(9), 9);
            int h2 = dao.get(this.getCode(10), 10);
            
            heuristic = Math.max(h1, h2);
            
            switch (heuristic) {
                case 0:  return 0;
                case 1:  return 1;
                case 2:  return 6;
                case 3:  return 6 ;
                case 4:  return 6;
                case 5:  return 7;
                case 6:  return 8;
                case 7:  return 9;
                case 8:  return 10;
                case 9:  return 11;
                case 10: return 12;
                case 11: return 13;
                case 12: return 13;
                default: return 13;
            }
        }
    }

    private int getHeuristicStage8() { // Thistlethwaite 4
        int heuristic = dao.get(this.getCode(8), 8);
        
        if (heuristic != -1) {
            return heuristic;
        } else {
            return 1;
        }
    }

    // Metodi palauttaa kuution tilan tunnistuskoodin.
    // Koodeista laskettuja hajautusarvoja käytetään heuristiikkataulujen avaimina.
    
    public String getCode(int stage) {
        switch (stage) {
            case 1: return getCodeStage1();
            case 2: return getCodeStage2();
            case 3: return getCodeStage3();
            case 4: return getCodeStage4();
            case 5: return getCodeStage5();
            case 6: return getCodeStage6();
            case 7: return getCodeStage7();
            case 8: return getCodeStage8();
            case 9: return getCornerPermutation();
            default: 
                if (getPermutationParity(getCornerPermutation())) {
                    return getEdgePermutation()+"E";
                } else {
                    return getEdgePermutation()+"O";
                }
        }
    }

    private String getCodeStage1() {
        StringBuilder code = new StringBuilder();

        for (Sivu s : sivut) {
            for (int y = 1; y <= 2; y++) {
                for (int x = 1; x <= 2; x++) {
                    if (s.palaset[x][y] == VALKOINEN || s.palaset[x][y] == KELTAINEN) {
                        code.append(x);
                        code.append(y);
                    }
                }
            }
            code.append(':');
        }

        return code.toString();
    }

    private String getCodeStage2() {
        // koodiin tulee:
        
        // valkoisten keskuspalojen permutaatio ( 8 c 4 = 70 )
        // punaisten ja oranssien keskuspalojen permutaatio ( 16 c 8 = 12870 )
        // "high" reunapalojen permutaatio ( 24 c 12 = 2704156 )
        
        // nuo kerrottuna on: 2,4 * 10^12 =S=S=S=S
        
        return null;    // TBA
    }

    private String getCodeStage3() {
        // koodiin tulee:
        
        // valkoisten keskuspalojen permutaatio ( 12 )
        // sinisten keskuspalojen permutaatio ( 8 c 4 = 70 )
        // reunapalan ja sen vastinpalan paikka ( ??? )
        
        return null;    // TBA
    }

    private String getCodeStage4() {
        // koodiin tulee:
        
        // FB permutaatio ( 6 )
        // LR permutaatop ( 6 )
        // UD permutaatio ( 8 c 4 = 70 )
        
        // loppujen reunapalojen ja vastinpalojen paikka ( ??? )
        
        return null;    // TBA
    }

    private String getCodeStage5() {
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < edges.length; i++) {
            if (goodEdgePiece(getEdgePiece(edges[i]))) {
                code.append(1);
            } else {
                code.append(0);
            }
        }

        return code.toString();
    }

    private String getCodeStage6() {
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < edges.length; i++) {
            if (isELayerPiece(getEdgePiece(edges[i]))) {
                code.append(i);
            }
        }

        for (int i = 0; i < corners.length; i++) {
            code.append(getCornerOrientation(getCornerPiece(corners[i])));
        }

        return code.toString();
    }

    private String getCodeStage7() {
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < edges.length; i++) {
            int[] e = getEdgePiece(edges[i]);

            switch (e[0] * e[1]) {
                case UB:
                    code.append("1:");
                    break;
                case UL:
                    code.append("2:");
                    break;
                case UR:
                    code.append("3:");
                    break;
                case UF:
                    code.append("4:");
                    break;
                case DF:
                    code.append("5:");
                    break;
                case DL:
                    code.append("6:");
                    break;
                case DR:
                    code.append("7:");
                    break;
                case DB:
                    code.append("8:");
                    break;
                case LB:
                    code.append("9:");
                    break;
                case LF:
                    code.append("10:");
                    break;
                case RB:
                    code.append("11:");
                    break;
                case RF:
                    code.append("12:");
                    break;
            }
        }

        for (int i = 0; i < corners.length; i++) {
            int[] c = getCornerPiece(corners[i]);

            switch (c[0] + c[1] + c[2]) {
                case ULB:
                    code.append(1);
                    break;
                case ULF:
                    code.append(2);
                    break;
                case URF:
                    code.append(3);
                    break;
                case URB:
                    code.append(4);
                    break;
                case DLB:
                    code.append(5);
                    break;
                case DLF:
                    code.append(6);
                    break;
                case DRF:
                    code.append(7);
                    break;
                case DRB:
                    code.append(8);
                    break;
            }
        }

        return code.toString();
    }

    private String getCodeStage8() {

        StringBuilder code = new StringBuilder();

        for (int i = 0; i < edges.length; i++) {
            int[] e = getEdgePiece(edges[i]);

            switch (e[0] * e[1]) {
                case UB:
                    code.append("1:");
                    break;
                case UL:
                    code.append("2:");
                    break;
                case UR:
                    code.append("3:");
                    break;
                case UF:
                    code.append("4:");
                    break;
                case DF:
                    code.append("5:");
                    break;
                case DL:
                    code.append("6:");
                    break;
                case DR:
                    code.append("7:");
                    break;
                case DB:
                    code.append("8:");
                    break;
                case LB:
                    code.append("9:");
                    break;
                case LF:
                    code.append("10:");
                    break;
                case RB:
                    code.append("11:");
                    break;
                case RF:
                    code.append("12:");
                    break;
            }
        }

        for (int i = 0; i < corners.length; i++) {
            int[] c = getCornerPiece(corners[i]);

            switch (c[0] + c[1] + c[2]) {
                case ULB:
                    code.append(1);
                    break;
                case ULF:
                    code.append(2);
                    break;
                case URF:
                    code.append(3);
                    break;
                case URB:
                    code.append(4);
                    break;
                case DLB:
                    code.append(5);
                    break;
                case DLF:
                    code.append(6);
                    break;
                case DRF:
                    code.append(7);
                    break;
                case DRB:
                    code.append(8);
                    break;
            }
        }

        return code.toString();
    }
    
    // Tarkistaa reunapalan 'hyvyyden', käytetään vaiheen 5 metodeissa
    
    private boolean goodEdgePiece(int[] piece) {
        boolean goodPiece = false;

        if (piece[0] == ORANSSI || piece[0] == PUNAINEN) {
            goodPiece = true;
        } else if (piece[0] == VALKOINEN || piece[0] == KELTAINEN) {
            if (piece[1] == SININEN || piece[1] == VIHREÄ) {
                goodPiece = true;
            }
        }

        return goodPiece;
    }

    private boolean isELayerPiece(int[] piece) { // used in getCodeStage6
        // U and D

        if (piece[0] != ORANSSI && piece[0] != PUNAINEN && piece[1] != ORANSSI && piece[1] != PUNAINEN) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isMLayerPiece(int[] piece) {
        // R and L

        if (piece[0] != VALKOINEN && piece[0] != KELTAINEN && piece[1] != VALKOINEN && piece[1] != KELTAINEN) {
            return true;
        } else {
            return false;
        }
    }

    /*
    private boolean isSLayerPiece(int[] piece) {
        // F and B

        if (piece[0] != SININEN && piece[0] != VIHREÄ && piece[1] != SININEN && piece[1] != VIHREÄ) {
            return true;
        } else {
            return false;
        }
    }
    */

    // Käytetään metodissa getCodeStage6()
    
    private int getCornerOrientation(int[] piece) {
        if (piece[0] == ORANSSI || piece[0] == PUNAINEN) {
            return 1;
        } else if (piece[1] == ORANSSI || piece[1] == PUNAINEN) {
            return 2;
        } else {
            return 3;
        }
    }

    public boolean getPermutationParity(String perm) { // true = even, false = odd
        int inversions = 0;

        for (int i = 0; i < perm.length() - 1; i++) {
            for (int j = i + 1; j < perm.length(); j++) {
                if (perm.charAt(j) < perm.charAt(i)) {
                    inversions++;
                }
            }
        }

        return inversions % 2 == 0;
    }
    
    // Käytetään seiskavaiheessa
    
    public String getCornerPermutation() {
        StringBuilder code = new StringBuilder();
        
        for (int i = 0 ; i < corners.length ; i++) {
            int[] c = getCornerPiece(corners[i]);
            
            switch (c[0] + c[1] + c[2]) {
                case ULB: code.append(1); break;
                case ULF: code.append(2); break;
                case URF: code.append(3); break;
                case URB: code.append(4); break;
                case DLB: code.append(5); break;
                case DLF: code.append(6); break;
                case DRF: code.append(7); break;
                case DRB: code.append(8); break;
            }
        }
        
        return code.toString();
    }
    
    // Käytetään seiskavaiheessa
    
    public String getEdgePermutation() {
        StringBuilder code = new StringBuilder();
        
        int[] MS = {UB, UL, UR, UF, DF, DL, DR, DB};
        
        for (int i = 0 ; i < MS.length ; i++) {
            int[] e = getEdgePiece(edges[i]);
            
            switch (e[0] * e[1]) {
                case UB:
                case UF:
                case DB:
                case DF:
                    code.append(i);
            }
        }
        
        return code.toString();
    }
    
    public String getLowEdgePermutation() {
        StringBuilder code = new StringBuilder();
        
        for (int i = 0 ; i < edges.length ; i++) {
            int[] e = getEdgePiece(edges[i]);
            
            // public static final int[] edges = {UB, UL, UR, UF, DF, DL, DR, DB, LB, LF, RB, RF};
            
            switch (e[0] * e[1]) {
                case UB: code.append('A'); break;
                case UL: code.append('B'); break;
                case UR: code.append('C'); break;
                case UF: code.append('D'); break;
                case DF: code.append('E'); break;
                case DL: code.append('F'); break;
                case DR: code.append('G'); break;
                case DB: code.append('H'); break;
                case LB: code.append('I'); break;
                case LF: code.append('J'); break;
                case RB: code.append('K'); break;
                case RF: code.append('L'); break;
            }
        }
        
        return code.toString();
    }
    
    public String getHighEdgePermutation() {
        StringBuilder code = new StringBuilder();
        
        for (int i = 0 ; i < edges2.length ; i++) {
            int[] e = getEdgePiece(edges2[i]);
            
            // public static final int[] edges = {UB, UL, UR, UF, DF, DL, DR, DB, LB, LF, RB, RF};
            
            switch (e[0] * e[1]) {
                case UB: code.append('A'); break;
                case UL: code.append('B'); break;
                case UR: code.append('C'); break;
                case UF: code.append('D'); break;
                case DF: code.append('E'); break;
                case DL: code.append('F'); break;
                case DR: code.append('G'); break;
                case DB: code.append('H'); break;
                case LB: code.append('I'); break;
                case LF: code.append('J'); break;
                case RB: code.append('K'); break;
                case RF: code.append('L'); break;
            }
        }
        
        return code.toString();
    }
    
    public boolean isHighEdgePiece(int piece) {
        boolean highPiece = false;
        
        if (piece > 100) { // high slot
            if (goodEdgePiece(getEdgePiece(piece))) {
                highPiece = true;
            }
        } else { // low slot
            if (!goodEdgePiece(getEdgePiece(piece))) {
                highPiece = true;
            }
        }
        
        return highPiece;
    }
    
    // Metodi kääntelee kuutiota parametrina annetun ratkaisutaulukon mukaan
    
    public void applySolution(ArrayList<String> solution) {

        for (String s : solution) {
            käännäSivua(s);
        }
    }

    // Metodi suorittaa sivun käännöksen useiden apumetodien avulla.
    // Tuplakäännökset ja vastakkaiskäännökset toteutetaan kahtena tai kolmena
    // tavallisena käännöksenä.
    
    public void käännäSivua(String käännös) {

        int kerrat = 1;
        String k = käännös;

        if (käännös.length() == 2) {
            if (käännös.charAt(1) == '2') {
                kerrat = 2;
                k = käännös.substring(0, 1);
            } else if (käännös.charAt(1) == '\'') {
                kerrat = 3;
                k = käännös.substring(0, 1);
            }
        } else if (käännös.length() == 3) {
            if (käännös.charAt(2) == '2') {
                kerrat = 2;
            } else if (käännös.charAt(2) == '\'') {
                kerrat = 3;
            }
            k = käännös.substring(0, 2);
        }

        for (int i = 0; i < kerrat; i++) {
            switch (k) {
                case "U":
                    käännös_U();
                    break;
                case "u":
                    käännös_u();
                    break;
                case "Uu":
                    käännös_U();
                    käännös_u();
                    break;

                case "D":
                    käännös_D();
                    break;
                case "d":
                    käännös_d();
                    break;
                case "Dd":
                    käännös_D();
                    käännös_d();
                    break;

                case "L":
                    käännös_L();
                    break;
                case "l":
                    käännös_l();
                    break;
                case "Ll":
                    käännös_L();
                    käännös_l();
                    break;

                case "R":
                    käännös_R();
                    break;
                case "r":
                    käännös_r();
                    break;
                case "Rr":
                    käännös_R();
                    käännös_r();
                    break;

                case "F":
                    käännös_F();
                    break;
                case "f":
                    käännös_f();
                    break;
                case "Ff":
                    käännös_F();
                    käännös_f();
                    break;

                case "B":
                    käännös_B();
                    break;
                case "b":
                    käännös_b();
                    break;
                case "Bb":
                    käännös_B();
                    käännös_b();
                    break;
            }
        }
    }

    private void käännös_U() {
        sivut[YLÄ].käännäMyötäpäivään();
        käännös_U_sivut(0);
    }

    private void käännös_u() {
        käännös_U_sivut(1);
    }

    private void käännös_U_sivut(int i) {
        int[] temp = getVäriRivi(VASEN, i);
        setVäriRivi(VASEN, i, getVäriRivi(ETU, i));
        setVäriRivi(ETU, i, getVäriRivi(OIKEA, i));
        setVäriRivi(OIKEA, i, getVäriRivi(TAKA, i));
        setVäriRivi(TAKA, i, temp);
    }

    private void käännös_D() {
        sivut[ALA].käännäMyötäpäivään();
        käännös_D_sivut(3);
    }

    private void käännös_d() {
        käännös_D_sivut(2);
    }

    private void käännös_D_sivut(int i) {
        int[] temp = getVäriRivi(VASEN, i);
        setVäriRivi(VASEN, i, getVäriRivi(TAKA, i));
        setVäriRivi(TAKA, i, getVäriRivi(OIKEA, i));
        setVäriRivi(OIKEA, i, getVäriRivi(ETU, i));
        setVäriRivi(ETU, i, temp);
    }

    private void käännös_L() {
        sivut[VASEN].käännäMyötäpäivään();
        käännös_L_sivut(3, 0);
    }

    private void käännös_l() {
        käännös_L_sivut(2, 1);
    }

    private void käännös_L_sivut(int i, int j) {
        int[] temp = getVäriSarakeRev(TAKA, i);
        setVäriSarake(TAKA, i, getVäriSarakeRev(ALA, j));
        setVäriSarake(ALA, j, getVäriSarake(ETU, j));
        setVäriSarake(ETU, j, getVäriSarake(YLÄ, j));
        setVäriSarake(YLÄ, j, temp);
    }

    private void käännös_R() {
        sivut[OIKEA].käännäMyötäpäivään();
        käännös_R_sivut(3, 0);
    }

    private void käännös_r() {
        käännös_R_sivut(2, 1);
    }

    private void käännös_R_sivut(int i, int j) {
        int[] temp = getVäriSarake(ETU, i);
        setVäriSarake(ETU, i, getVäriSarake(ALA, i));
        setVäriSarake(ALA, i, getVäriSarakeRev(TAKA, j));
        setVäriSarake(TAKA, j, getVäriSarakeRev(YLÄ, i));
        setVäriSarake(YLÄ, i, temp);
    }

    private void käännös_F() {
        sivut[ETU].käännäMyötäpäivään();
        käännös_F_sivut(3, 0);
    }

    private void käännös_f() {
        käännös_F_sivut(2, 1);
    }

    private void käännös_F_sivut(int i, int j) {
        int[] temp = getVäriSarakeRev(VASEN, i);
        setVäriSarake(VASEN, i, getVäriRivi(ALA, j));
        setVäriRivi(ALA, j, getVäriSarakeRev(OIKEA, j));
        setVäriSarake(OIKEA, j, getVäriRivi(YLÄ, i));
        setVäriRivi(YLÄ, i, temp);
    }

    private void käännös_B() {
        sivut[TAKA].käännäMyötäpäivään();
        käännös_B_sivut(3, 0);
    }

    private void käännös_b() {
        käännös_B_sivut(2, 1);
    }

    private void käännös_B_sivut(int i, int j) {
        int[] temp = getVäriSarake(OIKEA, i);
        setVäriSarake(OIKEA, i, getVäriRiviRev(ALA, i));
        setVäriRivi(ALA, i, getVäriSarake(VASEN, j));
        setVäriSarake(VASEN, j, getVäriRiviRev(YLÄ, j));
        setVäriRivi(YLÄ, j, temp);
    }

    private int[] getVäriSarake(int sivu, int sarake) {
        int[] c = new int[4];

        for (int i = 0; i < 4; i++) {
            c[i] = sivut[sivu].palaset[sarake][i];
        }

        return c;
    }

    private int[] getVäriSarakeRev(int sivu, int sarake) {
        int[] c = new int[4];

        for (int i = 0, j = 3; i < 4; i++, j--) {
            c[i] = sivut[sivu].palaset[sarake][j];
        }

        return c;
    }

    private int[] getVäriRivi(int sivu, int rivi) {
        int[] c = new int[4];

        for (int i = 0; i < 4; i++) {
            c[i] = sivut[sivu].palaset[i][rivi];
        }

        return c;
    }

    private int[] getVäriRiviRev(int sivu, int rivi) {
        int[] c = new int[4];

        for (int i = 0, j = 3; i < 4; i++, j--) {
            c[i] = sivut[sivu].palaset[j][rivi];
        }

        return c;
    }

    private void setVäriSarake(int sivu, int sarake, int[] c) {
        for (int i = 0; i < 4; i++) {
            sivut[sivu].palaset[sarake][i] = c[i];
        }
    }

    private void setVäriRivi(int sivu, int rivi, int[] c) {
        for (int i = 0; i < 4; i++) {
            sivut[sivu].palaset[i][rivi] = c[i];
        }
    }

    // Apuluokka, jossa mallinnetaan kuution sivut
    
    private class Sivu {

        private int[][] palaset;

        // Luodaan yksivärinen sivu
        public Sivu(int x) {
            palaset = new int[4][4];

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    palaset[i][j] = x;
                }
            }
        }

        private Sivu(Sivu s) {
            palaset = new int[4][4];

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    palaset[i][j] = s.palaset[i][j];
                }
            }
        }

        public void setColors(Color[][] colors) {
            for (int i = 0 ; i < 4 ; i++) {
                for (int j = 0 ; j < 4 ; j++) {
                    if (colors[i][j].equals(Color.blue)) {
                        palaset[i][j] = SININEN;
                    } else if (colors[i][j].equals(Color.green)) {
                        palaset[i][j] = VIHREÄ;
                    } else if (colors[i][j].equals(Color.red)) {
                        palaset[i][j] = PUNAINEN;
                    } else if (colors[i][j].equals(Color.orange)) {
                        palaset[i][j] = ORANSSI;
                    } else if (colors[i][j].equals(Color.yellow)) {
                        palaset[i][j] = KELTAINEN;
                    } else if (colors[i][j].equals(Color.white)) {
                        palaset[i][j] = VALKOINEN;
                    }
                }
            }
        }
        
        public Color[][] getColors() {
            Color[][] c = new Color[4][4];

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    switch (palaset[i][j]) {
                        case ORANSSI:
                            c[i][j] = Color.orange;
                            break;
                        case PUNAINEN:
                            c[i][j] = Color.red;
                            break;
                        case VIHREÄ:
                            c[i][j] = Color.green;
                            break;
                        case VALKOINEN:
                            c[i][j] = Color.white;
                            break;
                        case SININEN:
                            c[i][j] = Color.blue;
                            break;
                        case KELTAINEN:
                            c[i][j] = Color.yellow;
                            break;
                    }
                }
            }

            return c;
        }

        public void käännäMyötäpäivään() {
            // käännetään keskusta
            int temp = palaset[1][1];
            palaset[1][1] = palaset[1][2];
            palaset[1][2] = palaset[2][2];
            palaset[2][2] = palaset[2][1];
            palaset[2][1] = temp;

            // käännetään kulmat
            temp = palaset[0][0];
            palaset[0][0] = palaset[0][3];
            palaset[0][3] = palaset[3][3];
            palaset[3][3] = palaset[3][0];
            palaset[3][0] = temp;

            // käännetään sivut
            temp = palaset[1][0];
            palaset[1][0] = palaset[0][2];
            palaset[0][2] = palaset[2][3];
            palaset[2][3] = palaset[3][1];
            palaset[3][1] = temp;

            temp = palaset[2][0];
            palaset[2][0] = palaset[0][1];
            palaset[0][1] = palaset[1][3];
            palaset[1][3] = palaset[3][2];
            palaset[3][2] = temp;
        }

        public void käännäVastapäivään() {
            // käännetään keskusta
            int temp = palaset[1][1];
            palaset[1][1] = palaset[2][1];
            palaset[2][1] = palaset[2][2];
            palaset[2][2] = palaset[1][2];
            palaset[1][2] = temp;

            // käännetään kulmat
            temp = palaset[0][0];
            palaset[0][0] = palaset[3][0];
            palaset[3][0] = palaset[3][3];
            palaset[3][3] = palaset[0][3];
            palaset[0][3] = temp;

            // käännetään sivut
            temp = palaset[1][0];
            palaset[1][0] = palaset[3][1];
            palaset[3][1] = palaset[2][3];
            palaset[2][3] = palaset[0][2];
            palaset[0][2] = temp;

            temp = palaset[2][0];
            palaset[2][0] = palaset[3][2];
            palaset[3][2] = palaset[1][3];
            palaset[1][3] = palaset[0][1];
            palaset[0][1] = temp;
        }
    }
}
