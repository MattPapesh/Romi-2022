// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants 
{
    public static final class Drive //Found with frc characterization tool
    {
        public static final double ksVolts = 0.373;
        public static final double kvVoltSecsPerMeter = 6.23; 
        public static final double kaVoltSecsPerMeterSquared = 0.0175; 
        public static final double kpDriveVelocity = 0.00181;  

        public static final double kiDriveCoefficient = 0;
        public static final double kdDriveCoefficient = 0; 

        public static final double kTrackWidthMeters = 0.646043228;
        public static final DifferentialDriveKinematics kDriveKinematics = 
        new DifferentialDriveKinematics(kTrackWidthMeters);
    };

    public static final class Autonomous
    {
        public static final double kMaxSpeedMetersPerSec = 0.8;
        public static final double kMaxAccelerationMetersPerSecSquared = 0.8;

        //Baseline values:
        public static final double kRamseteB = 2; 
        public static final double kRamseteZeta = 0.7;
        
        public static final String autonomous_routine = "myRoutine";
    };

}
