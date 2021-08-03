package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.*;
import frc.robot.subsystems.PathweaverProject;
import frc.robot.subsystems.Drivetrain;

import java.util.LinkedList;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class DrivePathGroup extends CommandBase{
    private Drivetrain drivetrain = null; 
    private PathweaverProject pathweaver_project = null; 
    private LinkedList<SequentialCommandGroup> sequential_command_list = null; 

    public DrivePathGroup(Drivetrain drivetrain, PathweaverProject pathweaver_project, String project_name, String group_name){
       addRequirements(drivetrain, pathweaver_project);
       this.drivetrain = drivetrain; 
       this.pathweaver_project = pathweaver_project;
       sequential_command_list = getSequentialCommandList(group_name);
    }

    private LinkedList<SequentialCommandGroup> getSequentialCommandList(String group_name){
        LinkedList<SequentialCommandGroup> sequential_command_list = new LinkedList<SequentialCommandGroup>();

        for(int i = 0; i < pathweaver_project.getGroupSize(group_name); i++){
            Pose2d pose = pathweaver_project.getTrajectorialInitialPose(group_name, i);
            drivetrain.resetOdometry(pose);
            
            sequential_command_list.addLast(
                new InstantCommand(
                    () -> {drivetrain.resetOdometry(pose);}, drivetrain)
                    .andThen(pathweaver_project.getRamseteCommand(group_name, i))
                    .andThen(new InstantCommand(() -> {drivetrain.tankDriveVolts(0, 0);}))
            );
        }

        return sequential_command_list; 
    }

   @Override
   public void initialize() {
       for(int i = 0; i < sequential_command_list.size(); i++){
           sequential_command_list.get(i).schedule();
       }
   }

   @Override
   public void execute() {}
   @Override
   public void end(boolean interrupted) {}
   @Override
   public boolean isFinished() {
       return true;
   }
}
