package frc.robot.commands;

import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.subsystems.pathweaver;

public class Autonomous extends SequentialCommandGroup
{
    Trajectory autonomous_trajectory = null; 

    public Autonomous(pathweaver subsystem, String path_name)
    {
        addRequirements(subsystem);
        subsystem.begin(path_name);
        autonomous_trajectory = subsystem.getTrajectory();
    }

    private RamseteCommand getRamseteCommand()
    {
        return new RamseteCommand
        (
            autonomous_trajectory, pose, controller, feedforward,
            kinematics, wheelSpeeds, leftController, 
            rightController, outputVolts, requirements
        );
    }
}
