package frc.robot.subsystems;

import java.util.LinkedList;

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

    PathweaverProject(Drivetrain drivetrain, String project_name){
        this.project_name = project_name; 
        this.drivetrain = drivetrain; 
        project_dir = INITIAL_DIR + project_name; 
    }

    public LinkedList<RamseteCommand> getRamseteCommandGroup(String group_name){
        RamseteGroup ramsete_group = new RamseteGroup(drivetrain, project_dir, group_name);

        if (ramsete_group.getRamseteCommandGroup() != null){
            return ramsete_group.getRamseteCommandGroup();
        }
        
        System.err.println("PathweaverProject.java: Warning! A ramsete command list wasn't returned! \n");
        return null;
    }

    public String getProjectName(){
        return project_name;
    }

    public String getProjectDir(){
        return project_dir; 
    }
}
