package frc.robot.subsystems;

import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import java.io.IOException;
import java.nio.file.Path;

// Purpose: Provided a name of a pathweaver path, return a ramsete command

public class Ramsete extends SubsystemBase{
    private Drivetrain drivetrain = null; 
    private RamseteCommand ramsete_command = null;
    private final String FILE_TYPE = ".wpilib.json";
    private final String REDIRECT_DIR = "/../output/";
    private String group_dir = "";
    private String path_dir = "";  
    private String path_name = "";

    /* Provides a directory to a .wpilib.json file type from the output of the group in question 
        /   EX:
        /   paths/Groups -> group_1, group_2, and group_3
        /
        /   paths/Groups/group_1/group -> path_A, path_B, and path_C
        /
        /   paths/Groups/group_1/group/../output/path_A.wpilib.json

        group dir "path/Groups/ToPort/group"      "/../output/ToPort_1.wpilib.json"
        */

    public Ramsete(Drivetrain drivetrain, String group_dir, String path_name){
        this.drivetrain = drivetrain; 
        this.group_dir = group_dir; 
        this.path_name = path_name;
        path_dir = group_dir + REDIRECT_DIR + path_name + FILE_TYPE; 
       
        setRamseteCommand(); 
    }

    private Trajectory getTrajectory(){
        Path trajectory_path = Filesystem.getDeployDirectory().toPath().resolve(path_dir);

        try{
            return TrajectoryUtil.fromPathweaverJson(trajectory_path); 
        }
        catch(IOException e){
            System.out.println("Failed to get the trajectory of path: " + path_name + "! \n");
            return null;
        }
    }

    private void setRamseteCommand(){
        ramsete_command = new RamseteCommand(
            getTrajectory(), drivetrain::getPose, 
            new RamseteController(Constants.Autonomous.kRamseteB, Constants.Autonomous.kRamseteZeta),
            new SimpleMotorFeedforward(Constants.Drive.ksVolts, Constants.Drive.kvVoltSecsPerMeter, Constants.Drive.kaVoltSecsPerMeterSquared),
            Constants.Drive.kDriveKinematics, drivetrain::getWheelSpeeds, 
            new PIDController(Constants.Drive.kpDriveVelocity, Constants.Drive.kiDriveCoefficient, Constants.Drive.kdDriveCoefficient),
            new PIDController(Constants.Drive.kpDriveVelocity, Constants.Drive.kiDriveCoefficient, Constants.Drive.kdDriveCoefficient),
            drivetrain::tankDriveVolts, drivetrain
        );
    }

    public RamseteCommand getRamseteCommand(){
        if(ramsete_command != null){
            return ramsete_command; 
        }
        else{
            System.err.println("Could not get the ramsete command of path: " + path_name + "! \n");
            return null; 
        }
    }

    public String getGroupDir(){
        return group_dir;
    }

    public String getPathDir(){
        return path_dir;
    }

    public String getPathName(){
        return path_name; 
    }
}
