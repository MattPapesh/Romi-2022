package frc.robot.subsystems;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File; 
import java.io.InputStream;
import java.util.LinkedList;

import javax.swing.plaf.basic.BasicComboPopup.ListDataHandler;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// Purpose: Provided a group name, return a ramsete command for each of its paths

// NOTE: ALL DIRECTORIES THAT ARE STORED ARE ONLY THE SUB DIRECTORIES OF THE DEPLOY FOLDER! 
// A COMPLETE DIRECTORY WOULD BE THE DIRECTORY OF THE DEPLOY FOLDER FOLLOWED BY THE DIRECTORY IN QUESTION! 
// THIS GOES FOR ALL PATHWEAVER AND RAMSETE RELATED SUBSYSTEMS. 

public class RamseteGroup extends SubsystemBase{
    private final String INITIAL_DIR = "paths/";
    private final String GROUPS = "/Groups/"; 
    private final String FILE_TYPE = ".path"; 
    private String group_dir = "";
    private String group_name = "";
    private String project_dir = "";         // paths/foward_and_back/Groups/
    private Drivetrain drivetrain = null;
    private Ramsete path_ramsete = null;
    private LinkedList<RamseteCommand> ramsete_group = null;
    private LinkedList<String> path_list = null; 
     

    public RamseteGroup(Drivetrain drivetrain, String project_dir, String group_name){
        this.drivetrain = drivetrain; 
        this.group_name = group_name; 
        this.project_dir = project_dir;
        group_dir = INITIAL_DIR + project_dir + GROUPS; 
        path_ramsete = new Ramsete(drivetrain, group_dir); 

        setPathList(new File("DIR OF GROUP FILE!!!"));
        setRamseteGroup();
    }

    private void setPathList(File group_file){
        BufferedReader reader = null;
        path_list = new LinkedList<String>();

        try{
            reader = new BufferedReader(new FileReader(group_file));
        }
        catch(FileNotFoundException e){
            System.err.println("Could not count the number of paths! \n");
        }

        while(true){
            try{
                path_list.addLast(reader.readLine().replaceAll(FILE_TYPE, ""));
            }
            catch(IOException e){
                break; 
            }
        }
    }

    private void setRamseteGroup(){
        int num_of_paths = 
    }

    public LinkedList<RamseteCommand> getRamseteGroup(){
        return ramsete_group; 
    }

    public String getRamseteGroupName(){
        return group_name; 
    }
}
