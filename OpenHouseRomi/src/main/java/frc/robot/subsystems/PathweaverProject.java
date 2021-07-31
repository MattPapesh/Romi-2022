package frc.robot.subsystems;

import java.util.LinkedList;
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

    PathweaverProject(String project_name){
        this.project_name = project_name; 
        project_dir = INITIAL_DIR + project_name; 
        ramsete_group_list = new LinkedList<RamseteGroup>();
    }

    public void appendRamseteGroup(RamseteGroup ramsete_group){
        if (ramsete_group != null){
            ramsete_group_list.addLast(ramsete_group);
        }
    }

    public RamseteGroup getAppendedRamseteGroup(String group_name){
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
                    System.err.println("Error caught! There probably isn't any appended group with the name \"" + group_name + "\"!");
                }

                break;
            }
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
