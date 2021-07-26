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
        public final double ksVolts = 0.189;
        public final double kvVoltSecsPerMeter = 6.000; 
        public final double kaVoltSecsPerMeterSquared = 0.203; 
        public final double kpDriveVelocity = 1.050; 

        public final double kTrackWidthMeters = 0.150;
        public final DifferentialDriveKinematics kDriveKinematics = 
        new DifferentialDriveKinematics(kTrackWidthMeters);
    };

    public static final class Autonomous
    {
        public final double kMaxSpeedMetersPerSec = 0.800;
        public final double kMaxAccelerationMeterPerSecSquared = 0.800;

        //Baseline values:
        public final double kRamseteB = 2.000; 
        public final double kRamseteZeta = 0.700; 
    };

}
