package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.*;
import frc.robot.subsystems.PathweaverProject;
import frc.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.geometry.Pose2d;

// Purpose: Utilize pathweaver related subsystems to autonmously drive a group of paths of a pathweaver project given a group name

public class AutonomousDrive{
    private Drivetrain drivetrain = null; 
    private PathweaverProject pathweaver_project = null;   

    public AutonomousDrive(Drivetrain drivetrain, PathweaverProject pathweaver_project){
        this.drivetrain = drivetrain; 
        this.pathweaver_project = pathweaver_project; 
    }

    public SequentialCommandGroup getPathGroupCommand(String group_name){
        SequentialCommandGroup path_group_command = new SequentialCommandGroup();
        path_group_command.addRequirements(drivetrain, pathweaver_project);

        drivetrain.resetEncoders();//needed?
        drivetrain.resetGyro();

        for(int i = 0; i < pathweaver_project.getGroupSize(group_name); i++){
            Pose2d pose = pathweaver_project.getTrajectorialInitialPose(group_name, i);
            drivetrain.resetOdometry(pose);//needed?
           
            path_group_command.addCommands(
                new InstantCommand(
                    () -> {drivetrain.resetOdometry(pose); System.out.println("Got command! \n");}, drivetrain)
                    .andThen(pathweaver_project.getRamseteCommand(group_name, i))
                    .andThen(new InstantCommand(() -> {drivetrain.tankDriveVolts(0, 0);}))
            );
        }

        return path_group_command;
    }
}
