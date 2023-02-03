package it.polito.ezshop.model;
import it.polito.ezshop.exceptions.*;
public class User implements it.polito.ezshop.data.User, java.io.Serializable{
    private Integer id;
    private String username;
    private String password;
    private String role;

    public User(Integer id, String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException{
        if(!checkId(id)){
            throw new IllegalArgumentException("Not a valid user id");
        }
        if(!checkString(username)){
            throw new InvalidUsernameException("Not a valid username");
        }
        if(!checkString(password)){
            throw new InvalidPasswordException("Not a valid password");
        }
        if(!checkString(role) || !checkRole(role)){
            throw new InvalidRoleException("Not a valid role");
        }
        setId(id);
        setUsername(username);
        setPassword(password);
        setRole(role);
    }

    @Override
    public Integer getId(){
        return this.id;
    }

    @Override
    public void setId(Integer id){
        if(!checkId(id)){
            return;
        }
        this.id=id;
    }

    @Override
    public String getUsername(){
        return this.username;
    }

    @Override
    public void setUsername(String username){
        if(!checkString(username)){
            return;
        }
        this.username=username;
    }

    @Override
    public String getPassword(){
        return this.password;
    }

    @Override
    public void setPassword(String password){
        if(!checkString(password)){
            return;
        }
        this.password=password;
    }

    @Override
    public String getRole(){
        return this.role;
    }

    @Override
    public void setRole(String role){
        if(!checkString(role) || !checkRole(role)){
            return;
        }
        this.role=role;
    }

    private boolean checkString(String s){
        if(s==null || s.length()==0){
            return false;
        }
        else{
            return true;
        }
    }

    private boolean checkRole(String role){
        if(!role.equalsIgnoreCase("Administrator") && !role.equalsIgnoreCase("Cashier") && !role.equalsIgnoreCase("ShopManager")){
            return false;
        }
        else{
            return true;
        }
    }

    private boolean checkId(int id){
        if(id<0){
            return false;
        }
        else{
            return true;
        }
    }
}
