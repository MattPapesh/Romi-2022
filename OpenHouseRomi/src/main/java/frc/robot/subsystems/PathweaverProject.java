package frc.robot.subsystems;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

import edu.wpi.first.wpilibj.Filesystem;
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
    private Drivetrain drivetrain = null; 

    PathweaverProject(Drivetrain drivetrain, String project_name){
        this.project_name = project_name; 
        this.drivetrain = drivetrain; 
        project_dir = INITIAL_DIR + project_name; 
        ramsete_group_list = new LinkedList<RamseteGroup>();
        
    }

    public RamseteGroup getRamseteGroup_v2(String group_name){
        return new RamseteGroup(drivetrain, group_name);
    }

    public RamseteGroup getRamseteGroup(String group_name){
        int list_index = 0; 
        
        while(true){
            try{
                RamseteGroup current_ramsete_group = ramsete_group_list.get(list_index);

                if(current_ramsete_group.getRamseteGroupName() == group_name){
                    return current_ramsete_group;
                }
            }
            catch(IndexOutOfBoundsException e){
                if(list_index == 0){
                    System.err.println(
                        "PathweaverProject.java: Exception caught! RamseteGroup: \"" + group_name + "\" could not be found!"
                    );
                }

                break;
            }

            list_index++;
        }

        return null;
    }

    public String getProjectName(){
        return project_name;
    }

    public String getProjectDir(){
        return project_dir; 
    }
}
