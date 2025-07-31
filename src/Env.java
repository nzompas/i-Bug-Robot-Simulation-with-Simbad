package myfirst;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import simbad.sim.*;

public class Env extends EnvironmentDescription {

    public Env() {

        light1SetPosition(0, 2, 6);
        light1IsOn = true;

        add(new MyRobot(new Vector3d(0, 0, 0), "robot 1"));
        add(new Arch(new Vector3d(0,0,-5),this));
        Arch a = new Arch(new Vector3d(5,0,0),this);
        add(new Wall(new Vector3d(0,0,2), 2 ,1,this));
        a.rotate90(1);
        add(a);
        Arch b = new Arch(new Vector3d(-5,0,0),this);
        b.rotate90(1);
        add(b);
    }
}
