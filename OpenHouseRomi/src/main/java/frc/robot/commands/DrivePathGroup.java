package frc.robot.commands;

//import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.subsystems.PathweaverProject;
import frc.robot.subsystems.Drivetrain;

import java.util.LinkedList;

import edu.wpi.first.wpilibj.geometry.Pose2d;

public class DrivePathGroup extends SequentialCommandGroup{
    private Drivetrain drivetrain = null; 
    private PathweaverProject pathweaver_project = null; 
    private LinkedList<SequentialCommandGroup> sequential_command_list = null; 

    public DrivePathGroup(Drivetrain drivetrain, PathweaverProject pathweaver_project, String group_name){
        addRequirements(drivetrain, pathweaver_project);
        this.drivetrain = drivetrain; 
        this.pathweaver_project = pathweaver_project;
       
        addPathCommands(group_name);
    }

    private void addPathCommands(String group_name){

        for(int i = 0; i < pathweaver_project.getGroupSize(group_name); i++){
            Pose2d pose = pathweaver_project.getTrajectorialInitialPose(group_name, i);
            drivetrain.resetOdometry(pose);
           
            addCommands(
                new InstantCommand(
                    () -> {drivetrain.resetOdometry(pose); System.out.println("Got command! \n");}, drivetrain)
                    .andThen(pathweaver_project.getRamseteCommand(group_name, i))
                    .andThen(new InstantCommand(() -> {drivetrain.tankDriveVolts(0, 0);}))
            );
        }
    }
}
