package dev.easyplay.adapter;

public class BrowserItem implements Comparable<BrowserItem>{
    private String name;
    private String data;
    private String date;
    private String path;
    private String image;
    public String type;

    public BrowserItem(String n, String d, String dt, String p, String img, String type)
    {
        name = n;
        data = d;
        date = dt;
        path = p;
        image = img;
        this.type = type;
    }

    public BrowserItem(String n, String d, String dt, String p, String img)
    {
        name = n;
        data = d;
        date = dt;
        path = p;
        image = img;
    }

    public String getName()
    {
        return name;
    }
    public String getData()
    {
        return data;
    }
    public String getDate()
    {
        return date;
    }
    public String getPath()
    {
        return path;
    }
    public String getImage() {
        return image;
    }
    public int compareTo(BrowserItem o) {
        if(this.name != null)
            return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
        else
            throw new IllegalArgumentException();
    }
}
