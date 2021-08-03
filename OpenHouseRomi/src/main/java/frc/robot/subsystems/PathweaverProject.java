package frc.robot.subsystems;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// Purpose: Create a pathweaver project that can possess groups of ramsete commands derived from the project's path groups

// NOTE: ALL DIRECTORIES THAT ARE STORED ARE ONLY THE SUB DIRECTORIES OF THE DEPLOY FOLDER! 
// A COMPLETE DIRECTORY WOULD BE THE DIRECTORY OF THE DEPLOY FOLDER FOLLOWED BY THE DIRECTORY IN QUESTION! 
// THIS GOES FOR ALL PATHWEAVER AND RAMSETE RELATED SUBSYSTEMS. 

public class PathweaverProject extends SubsystemBase {
    private final String INITIAL_DIR = "paths/";
    private String project_name = "";
    private String project_dir = ""; 
    private Drivetrain drivetrain = null; 

    public PathweaverProject(Drivetrain drivetrain, String project_name){
        this.project_name = project_name; 
        this.drivetrain = drivetrain; 
        project_dir = INITIAL_DIR + project_name; 
    }

    public RamseteCommand getRamseteCommand(String group_name, int path_index){
        PathGroup ramsete_group = new PathGroup(drivetrain, project_dir, group_name);
        return ramsete_group.getRamseteCommand(path_index);
    }

    public Pose2d getTrajectorialInitialPose(String group_name, int path_index){
        PathGroup ramsete_group = new PathGroup(drivetrain, project_dir, group_name);
        return ramsete_group.getTrajectorialInitialPose(path_index);
    }

    public String getProjectName(){
        return project_name;
    }

    public String getProjectDir(){
        return project_dir; 
    }
}
