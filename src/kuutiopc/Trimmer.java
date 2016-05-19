package kuutiopc;

import java.util.ArrayList;

public class Trimmer {
    
    // U, u, Uu, U', u', Uu', U2, u2, Uu2
    // U + U = U2
    // U + u = Uu
    // U + U' = null
    // U + Uu' = u'
    // U + U2 = U'
    // VALMIS 1
    // u + U = Uu
    // u + u = u2
    // u + u' = null
    // u + Uu' = U'
    // u + u2 = u'
    // VALMIS 2
    // Uu + Uu = Uu2
    // Uu + U' = u
    // Uu + u' = U
    // Uu + Uu' = null
    // Uu + Uu2 = Uu'
    // VALMIS 3
    // U'+ U = null
    // U' + Uu = u
    // U' + U' = U2
    // U' + u' = Uu'
    // U' + U2 = U
    // VALMIS 4
    // u' + u = null
    // u' + Uu = U
    // u' + U' = Uu'
    // u' + u' = u2
    // u' + u2 = u
    // VALMIS 5
    // Uu' + U = u'
    // Uu' + u = U'
    // Uu' + Uu = null
    // Uu' + Uu' = Uu2
    // Uu' + Uu2 = Uu
    // VALMIS 6
    // U2 + U = U'
    // U2 + U' = U
    // U2 + U2 = null
    // U2 + u2 = Uu2
    // VALMIS 7
    // u2 + u = u'
    // u2 + u' = u
    // u2 + U2 = Uu2
    // u2 + u2 = null
    // VALMIS 8
    // Uu2 + Uu = Uu'
    // Uu2 + Uu' = Uu
    // Uu2 + Uu2 = null
    // VALMIS 9
    
    public ArrayList<String> trimSolution(ArrayList<Kuutio> al) {
        if (al == null || al.isEmpty()) {
            return null;
        }
        
        ArrayList<String> strings = new ArrayList<>();
        
        for (Kuutio k : al) {
            strings.add(k.getKomento());
        }
        
        return trim(strings);
    }
    
    public ArrayList<String> trim(ArrayList<String> al) {
        
        if (al == null || al.isEmpty()) {
            return null;
        }
        
        if (al.size() == 1) {
            return al;
        }
        
        ArrayList<String> trimmed = new ArrayList<>();

        String s1 = al.get(0);
        
        boolean notFound;
        boolean deleted;

        for (int i = 1; i < al.size(); i++) {
            String s2 = al.get(i);
            
            //System.out.println("S1 = "+ s1);
            //System.out.println("S2 = "+ s2);

            notFound = false;
            deleted = false;

            if (s1.toLowerCase().charAt(0) == s2.toLowerCase().charAt(0)) {
                String old1 = s1;
                String old2 = s2;

                s1 = s1.replaceAll("[udlrfb]", "u");
                s1 = s1.replaceAll("[UDLRFB]", "U");
                s2 = s2.replaceAll("[udlrfb]", "u");
                s2 = s2.replaceAll("[UDLRFB]", "U");

                //System.out.println("S1 replaced = "+ s1);
                //System.out.println("S2 replaced = "+ s2);
                
                String result = tryToCombine(s1, s2);
                //System.out.println("RESULT: "+ result);

                if (result != null) {
                    if (result.equals("X")) {
                        deleted = true;
                    } else {
                        result = result.replace('u', old1.toLowerCase().charAt(0));
                        result = result.replace('U', old1.toUpperCase().charAt(0));
                        s1 = result;
                        //System.out.println("REAL RESULT: "+ result);
                    }
                } else {
                    s1 = old1;
                    s2 = old2;
                    notFound = true;
                }
            } else {
                notFound = true;
            }

            if (notFound) {
                trimmed.add(s1);
                s1 = s2;
            }

            if (deleted) {
                i++;
                if (i < al.size()) {
                    s1 = al.get(i);
                }
            }

            if (i == al.size() - 1) {
                trimmed.add(s1);
            }
        }
        
        return trimmed;
    }

    private String tryToCombine(String s1, String s2) {
        String result = null;

        // U, u, Uu, U', u', Uu', U2, u2, Uu2
        switch (s1) {
            case "U":
                result = comb1(s2);
                break;
            case "u":
                result = comb2(s2);
                break;
            case "Uu":
                result = comb3(s2);
                break;
            case "U'":
                result = comb4(s2);
                break;
            case "u'":
                result = comb5(s2);
                break;
            case "Uu'":
                result = comb6(s2);
                break;
            case "U2":
                result = comb7(s2);
                break;
            case "u2":
                result = comb8(s2);
                break;
            case "Uu2":
                result = comb9(s2);
                break;
        }

        return result;
    }

    private String comb1(String s2) {
        String result = null;

        // U + U = U2
        // U + u = Uu
        // U + U' = null
        // U + Uu' = u'
        // U + U2 = U'

        switch (s2) {
            case "U":
                result = "U2";
                break;
            case "u":
                result = "Uu";
                break;
            case "U'":
                result = "X";
                break;
            case "Uu'":
                result = "u'";
                break;
            case "U2":
                result = "U'";
                break;
        }

        return result;
    }

    private String comb2(String s2) {
        String result = null;

        // u + U = Uu
        // u + u = u2
        // u + u' = null
        // u + Uu' = U'
        // u + u2 = u'
        // VALMIS 2

        switch (s2) {
            case "U":
                result = "Uu";
                break;
            case "u":
                result = "u2";
                break;
            case "u'":
                result = "X";
                break;
            case "Uu'":
                result = "U'";
                break;
            case "u2":
                result = "u'";
                break;
        }

        return result;
    }

    private String comb3(String s2) {
        String result = null;

        // Uu + Uu = Uu2
        // Uu + U' = u
        // Uu + u' = U
        // Uu + Uu' = null
        // Uu + Uu2 = Uu'
        // VALMIS 3

        switch (s2) {
            case "Uu":
                result = "Uu2";
                break;
            case "U'":
                result = "u";
                break;
            case "u'":
                result = "U";
                break;
            case "Uu'":
                result = "X";
                break;
            case "Uu2":
                result = "Uu'";
                break;
        }

        return result;
    }

    private String comb4(String s2) {
        String result = null;

        // U' + U = null
        // U' + Uu = u
        // U' + U' = U2
        // U' + u' = Uu'
        // U' + U2 = U
        // VALMIS 4

        switch (s2) {
            case "U":
                result = "X";
                break;
            case "Uu":
                result = "u";
                break;
            case "U'":
                result = "U2";
                break;
            case "u'":
                result = "Uu'";
                break;
            case "U2":
                result = "U";
                break;
        }

        return result;
    }

    private String comb5(String s2) {
        String result = null;

        // u' + u = null
        // u' + Uu = U
        // u' + U' = Uu'
        // u' + u' = u2
        // u' + u2 = u
        // VALMIS 5

        switch (s2) {
            case "u":
                result = "X";
                break;
            case "Uu":
                result = "U";
                break;
            case "U'":
                result = "Uu'";
                break;
            case "u'":
                result = "u2";
                break;
            case "u2":
                result = "u";
                break;
        }

        return result;
    }

    private String comb6(String s2) {
        String result = null;

        // Uu' + U = u'
        // Uu' + u = U'
        // Uu' + Uu = null
        // Uu' + Uu' = Uu2
        // Uu' + Uu2 = Uu
        // VALMIS 6

        switch (s2) {
            case "U":
                result = "u'";
                break;
            case "u":
                result = "U'";
                break;
            case "Uu":
                result = "X";
                break;
            case "Uu'":
                result = "Uu2";
                break;
            case "Uu2":
                result = "Uu";
                break;
        }

        return result;
    }

    private String comb7(String s2) {
        String result = null;

        // U2 + U = U'
        // U2 + U' = U
        // U2 + U2 = null
        // U2 + u2 = Uu2
        // VALMIS 7

        switch (s2) {
            case "U":
                result = "U'";
                break;
            case "U'":
                result = "U";
                break;
            case "U2":
                result = "X";
                break;
            case "u2":
                result = "Uu2";
                break;
        }

        return result;
    }

    private String comb8(String s2) {
        String result = null;

        // u2 + u = u'
        // u2 + u' = u
        // u2 + U2 = Uu2
        // u2 + u2 = null
        // VALMIS 8

        switch (s2) {
            case "u":
                result = "u'";
                break;
            case "u'":
                result = "u";
                break;
            case "U2":
                result = "Uu2";
                break;
            case "u2":
                result = "X";
                break;
        }

        return result;
    }

    private String comb9(String s2) {
        String result = null;

        // Uu2 + Uu = Uu'
        // Uu2 + Uu' = Uu
        // Uu2 + Uu2 = null
        // VALMIS 9

        switch (s2) {
            case "Uu":
                result = "Uu'";
                break;
            case "Uu'":
                result = "Uu";
                break;
            case "Uu2":
                result = "X";
                break;
        }

        return result;
    }
}