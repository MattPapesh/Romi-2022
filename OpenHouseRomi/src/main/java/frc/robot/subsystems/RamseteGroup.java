package frc.robot.subsystems;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File; 
import java.util.LinkedList;

import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// Purpose: Provided a group name, return a ramsete command for each of its paths

// NOTE: ALL DIRECTORIES THAT ARE STORED ARE ONLY THE SUB DIRECTORIES OF THE DEPLOY FOLDER! 
// A COMPLETE DIRECTORY WOULD BE THE DIRECTORY OF THE DEPLOY FOLDER FOLLOWED BY THE DIRECTORY IN QUESTION! 
// THIS GOES FOR ALL PATHWEAVER AND RAMSETE RELATED SUBSYSTEMS. 

public class RamseteGroup extends SubsystemBase{
    private final String GROUPS = "/Groups/"; 
    private final String FILE_TYPE = ".path"; 
    private String group_dir = "";
    private String group_name = "";
    private String project_dir = "";         
    //private Drivetrain drivetrain = null;
    private Ramsete ramsete = null;
    private LinkedList<RamseteCommand> ramsete_command_group = null;
    private LinkedList<String> path_name_list = null; 

    public RamseteGroup(Drivetrain drivetrain, String project_dir, String group_name){
        //this.drivetrain = drivetrain; 
        this.project_dir = project_dir; 
        this.group_name = group_name; 
        group_dir = project_dir + GROUPS + group_name;
        ramsete = new Ramsete(drivetrain, group_dir);
        ramsete_command_group = new LinkedList<RamseteCommand>(); 
        path_name_list = new LinkedList<String>();

        setPathNameList(new File(group_dir));
        setRamseteGroup(); 
    }

    // Reads group files via a BufferedReader, removes the file type ".path", and stores the remaining names in path_name_list
    private void setPathNameList(File group_file){
        BufferedReader reader = null;

        try{
            reader = new BufferedReader(new FileReader(group_file));
        }
        catch(FileNotFoundException e){
            System.err.println("RamseteGroup.java: Exception caught! Could not find the group file! \n");
        }

        while(true){
            try{
                path_name_list.addLast(reader.readLine().replaceAll(FILE_TYPE, ""));
            }
            catch(IOException e){
                break;
            }
        }

        try{
            reader.close();
        }
        catch(IOException e){
            System.err.println("RamseteGroup.java: Exception caught! BufferedReader caught an IOException when closing! \n");
        }
    }

    private void setRamseteGroup(){
        for(int i = 0; i < path_name_list.size(); i++){
            ramsete_command_group.addLast(ramsete.getRamseteCommand(path_name_list.get(i))); 
        }
    }

    public LinkedList<RamseteCommand> getRamseteCommandGroup(){
        if (ramsete_command_group != null){
            return ramsete_command_group; 
        }
        else{
            System.err.println("RamseteGroup.java: Warning! A ramsete command group wasn't returned! \n");
            return null;
        }
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
