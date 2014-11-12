package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Query;
import models.Friends;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.allUsers;
import views.html.friendsList;

import java.util.List;
import java.util.ListIterator;

/**
 * Created by oliver on 12.11.14.
 */

@Security.Authenticated(Secured.class)
public class Users extends Controller
{
    public static Result showAll()
    {
        Query<User> query = Ebean.createQuery(User.class);
        Query<Friends> query2 = Ebean.createQuery(Friends.class);
        query2.where(Expr.like("user_email", "%" + request().username() + "%"));
        List<Friends> friends = query2.findList();

        query.where(
                Expr.not(Expr.like("email", "%" + request().username() + "%")));
        List<User> all = query.findList();

        ListIterator<Friends> litr = friends.listIterator();

        while(litr.hasNext()) {
            Friends element = litr.next();
            if(all.contains(User.find.byId(element.friend_email)))
            {
                all.remove(User.find.byId(element.friend_email));
            }
        }

//        List<User> all = User.find.where().not(User.find.where().like("email", "%" + request().username() + "%"));
        return ok(allUsers.render(all, Form.form(SelectFriend.class)));
    }

    public static Result addFriend()
    {
        Form<SelectFriend> sel_form = Form.form(SelectFriend.class).bindFromRequest();
        String email = sel_form.get().email;

        new Friends(request().username(), email).save();
        List<Friends> friends = Friends.find.where().like("user_email", "%"+request().username()+"%").findList();

        return ok(friendsList.render(friends));
    }

    public static class SelectFriend
    {
        public String email;
    }

}
