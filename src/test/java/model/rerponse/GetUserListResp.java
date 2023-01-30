package model.rerponse;

import java.util.ArrayList;

public class GetUserListResp {
    public int page;
    public int per_page;
    public int total;
    public int total_pages;
    public ArrayList<Datum> data;
    public Support support;
}
