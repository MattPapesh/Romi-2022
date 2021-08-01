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
    private LinkedList<RamseteGroup> ramsete_group_list = null;
    //private Drivetrain drivetrain = null; 

    PathweaverProject(Drivetrain drivetrain, String project_name){
        this.project_name = project_name; 
        //this.drivetrain = drivetrain; 
        project_dir = INITIAL_DIR + project_name; 
        ramsete_group_list = new LinkedList<RamseteGroup>();
    }

    public LinkedList<RamseteCommand> getRamseteCommandList(String group_name){
        RamseteGroup current_ramsete_group = null;
        int list_index = 0; 
        
        for(int i = 0; i < ramsete_group_list.size(); i++){
            
            current_ramsete_group = ramsete_group_list.get(list_index);

            if(current_ramsete_group.getRamseteGroupName() == group_name){
                return current_ramsete_group.getRamseteCommandGroup();
            } 
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
