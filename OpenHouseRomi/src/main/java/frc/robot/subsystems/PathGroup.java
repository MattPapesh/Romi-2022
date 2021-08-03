package frc.robot.subsystems;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File; 
import java.util.LinkedList;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// Purpose: Provided a group name, return a ramsete command for each of its paths

// NOTE: ALL DIRECTORIES THAT ARE STORED ARE ONLY THE SUB DIRECTORIES OF THE DEPLOY FOLDER! 
// A COMPLETE DIRECTORY WOULD BE THE DIRECTORY OF THE DEPLOY FOLDER FOLLOWED BY THE DIRECTORY IN QUESTION! 
// THIS GOES FOR ALL PATHWEAVER AND RAMSETE RELATED SUBSYSTEMS. 

public class PathGroup extends SubsystemBase {
    private class pathInfo{
        public RamseteCommand ramsete_command = null;
        public Pose2d trajectorial_initial_pose = null;
        
        pathInfo(RamseteCommand ramsete_command, Pose2d trajectorial_initial_pose){
            this.ramsete_command = ramsete_command;
            this.trajectorial_initial_pose = trajectorial_initial_pose; 
        }
    };
    
    private final String GROUPS = "/Groups/"; 
    private final String FILE_TYPE = ".path"; 
    private String group_dir = "";
    private String group_name = "";
    private String project_dir = "";         
    private PathSet ramsete = null;
    private LinkedList<pathInfo> path_info_list = null; 

    public PathGroup(Drivetrain drivetrain, String project_dir, String group_name){
        this.project_dir = project_dir; 
        this.group_name = group_name; 
        group_dir = project_dir + GROUPS + group_name;
        ramsete = new PathSet(drivetrain, group_dir);
        path_info_list = new LinkedList<pathInfo>(); 

        try{
            LinkedList<String> path_name_list = setPathNameList(new File(group_dir));
            
            for(int i = 0; i < path_name_list.size(); i++){
                ramsete.setPath(path_name_list.get(i));
                path_info_list.addLast(new pathInfo(ramsete.getRamseteCommand(), ramsete.getTrajectorialInitialPose()));
            }
        }
        catch(NullPointerException e){}
    }

    // Reads group files via a BufferedReader, removes the file type ".path", and stores the remaining names in path_name_list
    private LinkedList<String> setPathNameList(File group_file){
        LinkedList<String> path_name_list = new LinkedList<String>(); 
        BufferedReader buffered_reader = null;

        try{
            buffered_reader = new BufferedReader(new FileReader(group_file));
        }
        catch(FileNotFoundException e){
            System.err.println("RamseteGroup.java: Exception caught! Could not find the group file! \n");
        }

        while(true){
            try{
                path_name_list.addLast(buffered_reader.readLine().replaceAll(FILE_TYPE, ""));
            }
            catch(IOException e){
                break;
            }
        }

        try{
            buffered_reader.close();
        }
        catch(IOException e){
            System.err.println("RamseteGroup.java: Exception caught! BufferedReader caught an IOException when closing! \n");
        }

        return path_name_list;
    }

    public RamseteCommand getRamseteCommand(int path_index){
        RamseteCommand ramsete_command = path_info_list.get(path_index).ramsete_command;
        
        if (ramsete_command == null){
            System.err.println("RamseteGroup.java: Warning! A ramsete command wasn't returned! \n");
        }
        
        return ramsete_command; 
    }

    public Pose2d getTrajectorialInitialPose(int path_index){
        Pose2d pose = path_info_list.get(path_index).trajectorial_initial_pose;

        if(pose == null){
            System.err.println("RamseteGroup.java: Warning! A pose wasn't returned! \n");
        }

        return pose; 
    }

    public String getRamseteGroupName(){
        return group_name; 
    }

    public String getProjectDirectory(){
        return project_dir; 
    }

    public String getGroupDirectory(){
        return group_dir; 
    }
}
