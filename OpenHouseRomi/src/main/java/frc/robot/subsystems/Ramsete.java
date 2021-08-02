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

/* Purpose: Provided a name of a pathweaver path, return a ramsete command.

   NOTE: ALL DIRECTORIES THAT ARE STORED ARE ONLY THE SUB DIRECTORIES OF THE DEPLOY FOLDER! 
    A COMPLETE DIRECTORY WOULD BE THE DIRECTORY OF THE DEPLOY FOLDER FOLLOWED BY THE DIRECTORY IN QUESTION! 
   THIS GOES FOR ALL PATHWEAVER AND RAMSETE RELATED SUBSYSTEMS. 

   NOTE: Ramsete.java provides a directory to a .wpilib.json file type from the output of the group in question 
   For instance:
   paths -> project_A, project_B, project_C
   paths\project_A -> Groups, output, Paths, pathweaver.json
   paths\project_A\Groups ->(files) Group_A, Group_B, Group_C
   paths\project_A\Groups/Group_A ->(files) A1.path, A2.path, A3.path
   paths\project_A\output ->(files) A1.wpilib.json, A2.wpilib.json, A3.wpilib.json, ....

   EX:
   DIR: paths/project_A/Groups/../output/A1.wpilib.json
   ROOT DIR: .../src/main/deploy -> Is different on a roboRIO and Romi compared to a computer
   PROJECT DIR: paths/project_A 
   GROUP DIR: paths/project_A/Groups -> Where group files are accessed to get path names in order to find the correct files in the output
*/

public class Ramsete extends SubsystemBase {
    private Drivetrain drivetrain = null; 
    private RamseteCommand ramsete_command = null;
    private final String FILE_TYPE = ".wpilib.json";
    private final String REDIRECT_DIR = "/../../output/";
    private String group_dir = "";
    private String path_dir = "Path directory was not provided! \n";
    private String path_name = "Path name was not provided! \n";  

    public Ramsete(Drivetrain drivetrain, String group_dir){
        this.drivetrain = drivetrain; 
        this.group_dir = group_dir; 
    }

    private Trajectory getTrajectory(){
        Path trajectory_path = Filesystem.getDeployDirectory().toPath().resolve(path_dir);

        try{
            return TrajectoryUtil.fromPathweaverJson(trajectory_path); 
        }
        catch(IOException e){
            System.err.println("Ramsete.java: Exception caught! Failed to get the trajectory of path: " + path_name + "! \n");
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
            System.err.println("Ramsete.java: Warning! Could not get the ramsete command of path: " + path_name + "! \n");
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
