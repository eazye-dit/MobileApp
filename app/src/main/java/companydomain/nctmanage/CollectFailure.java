package companydomain.nctmanage;

import java.util.ArrayList;

/**
 * Created by YUJIN on 2018-05-01.
 */

public class CollectFailure {

    ArrayList<Integer> FailureIds;//1 3 5 7 9//
    ArrayList<Integer> CollectIndex;//4 // 10// 16//21
    ArrayList<String> Comments;
    public static CollectFailure instance = new CollectFailure();
    public CollectFailure(){
        FailureIds= new ArrayList<Integer>();
        Comments=new ArrayList<String>();
    }

    public void InputFailureId(String id)
    {
        FailureIds.add(Integer.getInteger(id));
    }

    public String GetJSonFailureIds()
    {
        String ret="";
        for(int i=0;i<FailureIds.size();i++)
        {

        }
        return ret;
    }
}
