package com.example.qaz.BasicClasses;

import java.util.List;

public class Group {
    private int id_g;
    private int[] id_u, id_d;
    private String name;

    public int getId_g() {
        return id_g;
    }

    public void setId_g(int id_g) {
        this.id_g = id_g;
    }

    public int[] getId_u() {
        return id_u;
    }

    public void setId_u(List<Integer> id_u) {
        this.id_u = new int[id_u.size()];
        for (int i = 0; i<id_u.size(); i++)
            this.id_u[i] = id_u.get(i);
    }
    public void setId_u(int[] id_u){this.id_u = id_u;}

    public int[] getId_d() {
        return id_d;
    }

    public void setId_d(List<Integer> id_d) {
        this.id_d = new int[id_d.size()];
        for (int i = 0; i<id_d.size(); i++)
            this.id_d[i] = id_d.get(i);
    }
    public void setId_d(int[] id_d){this.id_d = id_d;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
