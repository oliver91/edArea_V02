package models;

import play.db.ebean.Model;

import javax.persistence.Entity;

/**
 * Created by oliver on 12.11.14.
 */

@Entity
public class Friends extends Model
{
    public String user_email;
    public String friend_email;

    public Friends(String user_email, String friend_email)
    {
        this.user_email = user_email;
        this.friend_email = friend_email;
    }

    public static Finder<String, Friends> find = new Finder<String, Friends>(String.class, Friends.class);
}
