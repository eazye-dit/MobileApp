package companydomain.nctmanage;

import java.util.ArrayList;

/**
 * Created by YUJIN on 2018-05-01.
 */

public class CollectFailure {

    private int appointmentid=0;
    ArrayList<Integer> FailureIds;//1 3 5 7 9//
    ArrayList<Integer> CollectBiggestIndex;//4 // 10// 16//21
    ArrayList<Integer> TestIds;
    ArrayList<String> Comments;
    public static CollectFailure instance = new CollectFailure();

    public CollectFailure(){
        FailureIds= new ArrayList<Integer>();
        Comments=new ArrayList<String>();
        CollectBiggestIndex = new ArrayList<Integer>();
        TestIds = new ArrayList<Integer>();
    }
    public void SetInit()
    {
        FailureIds.clear();
        Comments.clear();
        CollectBiggestIndex.clear();
        TestIds.clear();
    }

    public void ClickFailureId(String id)
    {
        int i;
        if(CollectBiggestIndex.size()>0)
        {
            i=CollectBiggestIndex.get(CollectBiggestIndex.size()-1);
        }
        else
        {
            i=0;
        }
        int intid=Integer.parseInt(id);
        while(true)
        {
            if(i>=FailureIds.size()-1) {
                FailureIds.add(intid);
                break;
            }
            if(FailureIds.get(i)==intid)
            {
                FailureIds.remove(i);
                break;
            }
            i++;
        }
    }
    public void SetAppointmentId(String id)
    {
        appointmentid=Integer.parseInt(id);
    }
    public String GetStringAppointmentId(){
        return String.valueOf(appointmentid);
    }

    public void AddIndexWithLastIndex()
    {
        CollectBiggestIndex.add(FailureIds.size()-1);
    }

    public void AddTestId(String addId)
    {
        TestIds.add(Integer.parseInt(addId));
    }
    public void AddTestId(int addId)
    {
        TestIds.add(addId);
    }
    public void AddComment(String comment)
    {
        Comments.add(comment);
    }
    public void ClickNext(String comment)
    {
        AddTestId(TestIds.size()+1);
        AddComment(comment);
        AddIndexWithLastIndex();
    }

    public void ClickPrev()
    {
        DeleteFailures();
        DeleteLastComment();
        DeleteLastIndex();
        DeleteLastSubject();
    }

    public void DeleteFailures()
    {
        int lower;
        int i;
        if(CollectBiggestIndex.size()==0)
        {
            SetInit();
            return;
        }


        if(CollectBiggestIndex.size()==1)
        {
            lower=-1;
        }
        else
        {
            lower=CollectBiggestIndex.get(CollectBiggestIndex.size()-2);
        }
        //i=CollectBiggestIndex.get(CollectBiggestIndex.size()-1);
        i=FailureIds.size()-1;
        while(true)
        {
            if(i<=lower)
            {
                break;
            }

            FailureIds.remove(i--);
        }
    }
    public void DeleteLastSubject()
    {
        TestIds.remove(TestIds.size()-1);
    }
    public void DeleteLastIndex()
    {
        CollectBiggestIndex.remove(CollectBiggestIndex.size()-1);
    }
    public void DeleteLastComment()
    {
        Comments.remove(Comments.size()-1);
    }
    public String GetJSonFailureIds()
    {
        //String ret="{\"test\":[\"appointmentid\":"+appointmentid;
        String ret="{\"test\":{\"id\":"+appointmentid;
        ret += ",\"results\":[";
        for(int i=0;i<TestIds.size();i++)
        {
            ret += "{\"id\":"+TestIds.get(i)+",";
            ret += "\"checked_id\":[";
            int j;
            if(i==0)
            {
                j=0;
            }
            else
            {
                j=CollectBiggestIndex.get(i-1)+1;
            }
            while(FailureIds.size()>0)
            {
                ret +=FailureIds.get(j++);

                if(j>CollectBiggestIndex.get(i))
                {
                    break;
                }
                ret +=",";
            }
            ret += "],\"comment\":\""+Comments.get(i)+"\"}";

            if(i+1<TestIds.size())
            {
                ret += ",";
            }
        }

        ret +="]}}";
        return ret;
    }
}