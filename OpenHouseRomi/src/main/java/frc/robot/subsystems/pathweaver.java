package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Filesystem;
import java.io.IOException;
import java.nio.file.Path;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
//import edu.wpi.first.wpilibj.geometry.*;
import edu.wpi.first.wpilibj.trajectory.*;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
//import java.util.List;

public class pathweaver extends SubsystemBase
{
    private final String PARENT_FOLDER = "paths/output/"; 
    private final String FILE_TYPE = ".wpilib.json"; 
    private String path_name = "";
    private String path_dir = "";
    private Trajectory trajectory = new Trajectory(); 

    TrajectoryConfig config =
        new TrajectoryConfig(Constants.Autonomous.kMaxSpeedMetersPerSec, 
                             Constants.Autonomous.kMaxAccelerationMetersPerSecSquared)
            .setKinematics(Constants.Drive.kDriveKinematics)
            .addConstraint
            (
                new DifferentialDriveVoltageConstraint(
                new SimpleMotorFeedforward(Constants.Drive.ksVolts, 
                                       Constants.Drive.kvVoltSecsPerMeter, 
                                       Constants.Drive.kaVoltSecsPerMeterSquared),
                 Constants.Drive.kDriveKinematics,
                 10)
            );

    public void begin(String path_name)
    {
        this.path_name = path_name;
        path_dir = PARENT_FOLDER + path_name + FILE_TYPE; 
        Path trajectory_path = Filesystem.getDeployDirectory().toPath().resolve(path_dir);
        
        try{ trajectory = TrajectoryUtil.fromPathweaverJson(trajectory_path);
        System.out.println("pathweaver.java: pathweaver trajectory found!       Directory: " + trajectory_path + "\n");} 
        catch (IOException e){System.out.println("Wrong directory!");}      
    }

    public Trajectory getTrajectory(){System.out.println("returning trajectory! \n"); return trajectory; 

    /*return TrajectoryGenerator.generateTrajectory(
        // Start at the origin facing the +X direction
        new Pose2d(0, 0, new Rotation2d(0)),
        List.of(
       //     new Translation2d(0.0, 1),
      //      new Translation2d(1, 0)//,
           // new Translation2d(0.5, 0)
        ),
        new Pose2d(1.0, 0, new Rotation2d(0)),
      config);*/
}
    public String getPathName(){return path_name; }
}
