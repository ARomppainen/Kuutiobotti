package kuutiopc;

import java.sql.*;

public class KuutioDAO {

    private Connection c;
    
    private PreparedStatement[] statements;
    
    private PreparedStatement getCorner;
    private PreparedStatement insertCorner;
    
    private PreparedStatement getCorners7;
    private PreparedStatement insertCorners7;
    private PreparedStatement updateCorners7;
    private PreparedStatement getEdges7;
    private PreparedStatement insertEdges7;
    private PreparedStatement updateEdges7;
    
    
    public KuutioDAO() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://localhost/kuutio", "kuutio", "kuutio");
            prepareStatements();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void prepareStatements() {
        
        statements = new PreparedStatement[25];
        
        try {
            statements[0] = null;
            
            statements[1] = c.prepareStatement("SELECT moves FROM stageone WHERE hash=?");
            statements[2] = c.prepareStatement("SELECT moves FROM stagetwo WHERE hash=?");
            statements[3] = c.prepareStatement("SELECT moves FROM stagethree WHERE hash=?");
            statements[4] = c.prepareStatement("SELECT moves FROM stagefour WHERE hash=?");
            statements[5] = c.prepareStatement("SELECT moves FROM stagefive WHERE hash=?");
            statements[6] = c.prepareStatement("SELECT moves FROM stagesix WHERE hash=?");
            statements[7] = c.prepareStatement("SELECT moves FROM stageseven WHERE hash=?");
            statements[8] = c.prepareStatement("SELECT moves FROM stageeight WHERE hash=?");
            
            statements[9]  = c.prepareStatement("INSERT INTO stageone VALUES (?,?)");
            statements[10] = c.prepareStatement("INSERT INTO stagetwo VALUES (?,?)");
            statements[11] = c.prepareStatement("INSERT INTO stagethree VALUES (?,?)");
            statements[12] = c.prepareStatement("INSERT INTO stagefour VALUES (?,?)");
            statements[13] = c.prepareStatement("INSERT INTO stagefive VALUES (?,?)");
            statements[14] = c.prepareStatement("INSERT INTO stagesix VALUES (?,?)");
            statements[15] = c.prepareStatement("INSERT INTO stageseven VALUES (?,?)");
            statements[16] = c.prepareStatement("INSERT INTO stageeight VALUES (?,?)");
            
            statements[17] = c.prepareStatement("UPDATE stageone SET moves=? WHERE hash=?");
            statements[18] = c.prepareStatement("UPDATE stagetwo SET moves=? WHERE hash=?");
            statements[19] = c.prepareStatement("UPDATE stagethree SET moves=? WHERE hash=?");
            statements[20] = c.prepareStatement("UPDATE stagefour SET moves=? WHERE hash=?");
            statements[21] = c.prepareStatement("UPDATE stagefive SET moves=? WHERE hash=?");
            statements[22] = c.prepareStatement("UPDATE stagesix SET moves=? WHERE hash=?");
            statements[23] = c.prepareStatement("UPDATE stageseven SET moves=? WHERE hash=?");
            statements[24] = c.prepareStatement("UPDATE stageeight SET moves=? WHERE hash=?");
            
            getCorner = c.prepareStatement("SELECT corner FROM corners WHERE corner=?");
            insertCorner = c.prepareStatement("INSERT INTO corners VALUES (?)");
            
            getCorners7 = c.prepareStatement("SELECT moves FROM stageseven_corners WHERE hash=?");
            insertCorners7 = c.prepareStatement("INSERT INTO stageseven_corners VALUES (?,?)");
            updateCorners7 = c.prepareStatement("UPDATE stageseven_corners SET moves=? WHERE hash=?");
            getEdges7 = c.prepareStatement("SELECT moves FROM stageseven_edges WHERE hash=?");
            insertEdges7 = c.prepareStatement("INSERT INTO stageseven_edges VALUES (?,?)");
            updateEdges7 = c.prepareStatement("UPDATE stageseven_edges SET moves=? WHERE hash=?");
            
        } catch (SQLException e) {}
    }
    
    public int get(String code, int stage) {
        
        if (stage >= 1 && stage <= 8) {
            return get(code, statements[stage]);
        } else if (stage == 9) {
            return get(code, getCorners7);
        } else {
            return get(code, getEdges7);
        }
    }
    
    private int get(String code, PreparedStatement statement) {
        int result = -1;
        ResultSet rs;
        
        try {
            statement.setInt(1, code.hashCode());
            rs = statement.executeQuery();
            
            if (rs.next()) {
                result = rs.getInt(1);
                
                if (rs != null)
                    rs.close();
            }
            
        } catch (SQLException e) {
            System.out.println("ERROR");
        }
        
        return result;
    }
    
    public void insert(String code, int moves, int stage) {
        if (stage >= 1 && stage <= 8) {
            insert(code, moves, statements[stage + 8]);
        } else if (stage == 9) {
            insert(code, moves, insertCorners7);
        } else {
            insert(code, moves, insertEdges7);
        }
    }
    
    private void insert(String code, int moves, PreparedStatement statement) {
        try {
            statement.setInt(1, code.hashCode());
            statement.setInt(2, moves);
            statement.executeUpdate();
        } catch (SQLException e) {}
    }
    
    public void update(String code, int moves, int stage) {
        if (stage >= 1 && stage <= 8) {
            update(code, moves, statements[stage + 16]);
        } else if (stage == 9) {
            update(code, moves, updateCorners7);
        } else {
            update(code, moves, updateEdges7);
        }
    }
    
    private void update(String code, int moves, PreparedStatement statement) {
        try {
            statement.setInt(1, moves);
            statement.setInt(2, code.hashCode());
            statement.executeUpdate();
        } catch (SQLException e) {}
    }
    
    public boolean getCorner(String code) {
        boolean found = false;
        ResultSet rs;
        
        try {
            getCorner.setInt(1, code.hashCode());
            rs = getCorner.executeQuery();
            
            if (rs.next()) {
                found = true;
                
                if (rs != null)
                    rs.close();
            }
            
        } catch (SQLException e) {}
        
        return found;
    }
    
    public void insertCorner(String code) {
        try {
            insertCorner.setInt(1, code.hashCode());
            insertCorner.executeUpdate();
        } catch (SQLException e) {}
    }
}
