package myfirst;
import javax.vecmath.Vector3d;
import simbad.sim.Agent;
import simbad.sim.LightSensor;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;


public class MyRobot extends Agent {
    RangeSensorBelt sonars;
    LightSensor centerLightSensor;
    LightSensor leftLightSensor;
    LightSensor rightLightSensor;
    double[] list = new double[253];
    double angleToLight;
    boolean rotation = true;
    boolean calculateAngle=true;
    boolean findObstacle=false;
    int count=0;
    double maxAngle=0;
    boolean avoid=true;
    boolean obstacleHere=false;
    boolean startMoving=false;
    boolean turn=false;
    boolean againMoving= false;
    boolean againMoving1=false;
    double min=Double.POSITIVE_INFINITY;
    boolean find=false;
    boolean flag2=false;

    public MyRobot(Vector3d position, String name) {
        super(position, name);
        sonars = RobotFactory.addSonarBeltSensor(this,12);
        centerLightSensor = RobotFactory.addLightSensor(this);
        leftLightSensor = RobotFactory.addLightSensor(
                this,
                new Vector3d(0.1, 0, 0.2),
                (float) (Math.PI / 4), // 45 μοίρες αριστερά
                "leftSensor"
        );
        rightLightSensor = RobotFactory.addLightSensor(
                this,
                new Vector3d(-0.1, 0, 0.2),
                (float) (-Math.PI / 4), // 45 μοίρες δεξιά
                "rightSensor");
    }

    public void initBehavior() {
        setTranslationalVelocity(0);
        setRotationalVelocity(0.5);
    }

    public void performBehavior() {
        double center = centerLightSensor.getLux();
        double left = leftLightSensor.getLux();
        double right = rightLightSensor.getLux();
        angleToLight = Math.atan2(left - right, center);
        // Εντοπισμός μέγιστης κατεύθυνσης φωτός
        if (rotation) {
            //Περιστροφή γύρω από τον εαυτό του για να υπολογίσει τη μέγιστη γωνία προς τη λάμπα
            setRotationalVelocity(0.5);
            setTranslationalVelocity(0);
            list[count] = angleToLight;
            count += 1;
            if (count > 252) {
                rotation = false;
                for (int i = 0; i < list.length; i++) {
                    if (list[i] > maxAngle) {
                        maxAngle = list[i];
                    }
                }
            }
        } //Ευθυγράμμιση & Κίνηση προς τη λάμπα
        else {
            if (calculateAngle)
                if (Math.abs(angleToLight - maxAngle) < 0.0001) { //peiramata
                    calculateAngle = false;
                    findObstacle=true;
                    // Κίνηση προς τη λάμπα
                    setRotationalVelocity(0);
                    setTranslationalVelocity(0.5);
                } else {
                    // Ευθυγράμμιση
                    setRotationalVelocity(0.5);
                    setTranslationalVelocity(0);
                }
        }
        // Αν φτάσει κοντά στη λάμπα, τότε σταμάτα
        if (center > 0.07) {
            setTranslationalVelocity(0);
            setRotationalVelocity(0);
        } // Ψάχνει για πιθανά εμπόδια στον δρόμο του
            if (findObstacle){
                    for (int i = 0; i < sonars.getNumSensors(); i++) {
                        //Εντοπισμός εμποδίου μπροστά
                        if (sonars.getMeasurement(0) < 0.3 && (!obstacleHere || flag2)) {
                            obstacleHere = true;
                        }
                        //Αν έχει βρει εμπόδιο τότε
                        if (obstacleHere) {
                            //Στροφή αριστερά μέχρι να έχει το εμπόδιο ακριβώς στα πλάγια του
                            if (sonars.getMeasurement(9) > 0.13 && avoid) {
                                setTranslationalVelocity(0.0);
                                setRotationalVelocity(0.5);
                                startMoving = true;
                                flag2 = false;
                            }
                            //Όταν έχει το εμπόδιο στα πλάγια, ξεκινάει να κινείτε ευθεία
                            else if (startMoving) {
                                avoid = false;
                                setRotationalVelocity(0.0);
                                setTranslationalVelocity(0.5);
                                turn = true;
                                flag2 = true;
                            }
                            //όταν πλέον προσπεράσει το εμπόδιο αρχίζει να γυρίζει δεξιόστροφα
                            if (sonars.getMeasurement(9) > 1.4 && sonars.getMeasurement(8) > 0.4 && turn) {
                                startMoving = false;
                                setRotationalVelocity(-0.5);
                                setTranslationalVelocity(0.0);
                                againMoving = true;
                            }
                            //Μόλις γυρίσει αρκετά, συνεχίζει ευθεία δίπλα στο εμπόδιο
                            if (sonars.getMeasurement(10) < 0.51 && againMoving) {
                                turn = false;
                                setRotationalVelocity(-0.5);
                                setTranslationalVelocity(0.0);
                                againMoving1 = true;
                            }
                            if (againMoving1 && sonars.getMeasurement(10) > 0.516) {
                                againMoving = false;
                                setRotationalVelocity(0.0);
                                setTranslationalVelocity(0.5);
                                if (sonars.getMeasurement(9) < 0.3) {
                                    startMoving = true;
                                    againMoving1 = false;
                                }
                            }
                            if (center > min) {
                                find = true;
                            }
                            if (find && center < min) {
                                findObstacle = false;
                                rotation = true;
                                count = 0;
                                calculateAngle = true;
                                maxAngle = 0;
                                avoid = true;
                                obstacleHere = false;
                                startMoving = false;
                                turn = false;
                                againMoving = false;
                                againMoving1 = false;
                                min = Double.POSITIVE_INFINITY;
                                find = false;
                                flag2 = false;
                            }
                            min = center;
                        }
                    }
        }
    }
}


