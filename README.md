# i-Bug Robot Simulation with Simbad

This project simulates a mobile robot navigating toward a light source using the **i-Bug algorithm**, implemented in **Java** with the **Simbad 3D robot simulator**.

## 🧠 Description

The robot starts at the origin and attempts to reach a light source located 2 meters high, while navigating around obstacles using:

- **Rotation & alignment** to detect and face the light.
- **Sonar-based obstacle avoidance** via wall-following.
- **Light sensor feedback** to detect local maxima and resume light pursuit.

The robot operates in three classes:
- `MyFirst.java`: Main class initializing the simulation.
- `Env.java`: The environment setup including the robot, a light source, and four obstacles (3 arches, 1 wall).
- `MyRobot.java`: Robot behavior using 12 sonars and 3 light sensors (center, left, right), implementing the i-Bug logic.

## 🛠 Requirements

- Java 8+
- Simbad simulator

## 🔧 How It Works

1. Full rotation to detect max light angle.
2. Turns to face the light and moves forward.
3. When encountering obstacles:
   - Starts wall-following.
   - Monitors light intensity.
   - If local max found, resumes step 1.
