package companydomain.nctmanage;

/**
 * Created by YUJIN on 2018-04-29.
 */

public class CheckListItem {
    private String id;
    private String item;
    private String name;

    public void setId(String id){
        this.id = id;
    }

    public void setItem(String item ){
        this.item= item;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getId(){
        return this.id;
    }

    public String getItem(){
        return this.item;
    }

    public String getName(){
        return this.name;
    }
}
