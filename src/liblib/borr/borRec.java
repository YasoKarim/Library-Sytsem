package liblib.borr;

import admin.book;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
//borRec is a simple class to store the details of a borrow transaction
//those datails will be stored in the database
public class borRec {
    public DatePicker rdatp;
    public String isbn,rid,eid,name,author;
    public borRec(book b,String rid,String eid) {
        this.isbn=b.getIsbn();
        this.name=b.getNam();
        this.author=b.getAuth();
        this.rid=rid;
        this.eid=eid;
        rdatp=new DatePicker();
    }
    public DatePicker getRdatp() {return rdatp;}
    public String getIsbn() {return isbn;}
    public String getRid() {return rid;}
    public String getEid() {return eid;}
    public String getName() {return name;}
    public String getAuthor() {return author;}

}
