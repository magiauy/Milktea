package milktea.milktea.DAO;

import milktea.milktea.DTO.Category;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Category_DAO extends Connect{
    public static ArrayList<Category> getAllCategory(){
        ArrayList<Category> arrCategory = new ArrayList<>();
        if((openConnection("Category"))){
        try{
        String sql = "Select * from category";

        Statement stmt = connection.createStatement();

        ResultSet rs = stmt.executeQuery(sql);

        while(rs.next()){
        Category category = new Category();

        category.setId(rs.getString("id"));
        category.setName(rs.getString("name"));

        arrCategory.add(category);
                    }

                }catch(SQLException e){
                e.printStackTrace();
            }finally{
        closeConnection();
            }
        }
        return arrCategory;
    }
    public static boolean addCategory(Category category){
        boolean result = false;
        if(openConnection("Category")){
        try{
        String sql = "Insert into category values(?,?)";

        PreparedStatement stmt = connection.prepareStatement(sql);

        stmt.setString(1,category.getId());
        stmt.setString(2,category.getName());

        if(stmt.executeUpdate()>=1){
        result = true;
                    }

                }catch(SQLException e){
                e.printStackTrace();
                System.out.println(e);
            }finally{
        closeConnection();
            }
        }
        return result;
    }
    public static boolean editCategory(Category category){
        boolean result = false;
        if(openConnection("Category")){
        try{
        String sql = "Update category set name = ? where id = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);

        stmt.setString(1,category.getName());
        stmt.setString(2,category.getId());

        if(stmt.executeUpdate()>=1){
        result = true;
        }

        }catch(SQLException e){
            e.printStackTrace();
            System.out.println(e);
        }finally{
            closeConnection();
        }
    }
        return result;
    }
}
