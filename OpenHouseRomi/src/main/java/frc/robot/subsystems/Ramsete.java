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

// NOTE: ALL DIRECTORIES THAT ARE STORED ARE ONLY THE SUB DIRECTORIES OF THE DEPLOY FOLDER! 
// A COMPLETE DIRECTORY WOULD BE THE DIRECTORY OF THE DEPLOY FOLDER FOLLOWED BY THE DIRECTORY IN QUESTION! 
// THIS GOES FOR ALL PATHWEAVER AND RAMSETE RELATED SUBSYSTEMS. 

public class Ramsete extends SubsystemBase{
    private Drivetrain drivetrain = null; 
    private RamseteCommand ramsete_command = null;
    private final String FILE_TYPE = ".wpilib.json";
    private final String REDIRECT_DIR = "../output/";
    private String group_dir = "";
    private String path_dir = "Path directory was not provided! \n";
    private String path_name = "Path name was not provided! \n";  

    /* Provides a directory to a .wpilib.json file type from the output of the group in question 
        /   EX:
        /   paths/Groups -> group_1, group_2, and group_3
        /
        /   paths/Groups/group_1/group -> path_A, path_B, and path_C
        /
        /   paths/Groups/group_1/group/../output/path_A.wpilib.json

        group dir "path/Groups/ToPort/group/"      "../output/ToPort_1.wpilib.json"
    */
    //paths\foward_and_back\Groups\forward = GROUP DIR

    public Ramsete(Drivetrain drivetrain, String group_dir){// paths/foward_and_back/Groups/../output/
        this.drivetrain = drivetrain; 
        this.group_dir = group_dir; 
        
        path_dir = group_dir + REDIRECT_DIR; 
        // Is only a rough idea where thr path is located. A path name is needed to complete the directory
    }

    private Trajectory getTrajectory(){
        Path trajectory_path = Filesystem.getDeployDirectory().toPath().resolve(path_dir);

        try{
            return TrajectoryUtil.fromPathweaverJson(trajectory_path); 
        }
        catch(IOException e){
            System.err.println("Failed to get the trajectory of path: " + path_name + "! \n");
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
      
    public RamseteCommand getRamseteCommand(String path_name){
        this.path_name = path_name; 
        path_dir = group_dir + REDIRECT_DIR + path_name + FILE_TYPE;
        setRamseteCommand();

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
