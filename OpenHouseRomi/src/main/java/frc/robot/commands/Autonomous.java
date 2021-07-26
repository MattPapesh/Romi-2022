package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.*;

public class Autonomous extends SequentialCommandGroup
{
    private pathweaver pathweaver = null;
    private Drivetrain drivetrain = null; 
    private Trajectory trajectory = null;
    private Pose2d pose2d = null;  
    private RamseteController ramsete_controller = null;  
    private SimpleMotorFeedforward SMF = null; 

    public Autonomous(pathweaver pathweaver, Drivetrain drivetrain, String path_name)
    {
        addRequirements(pathweaver, drivetrain); 
        pathweaver.begin(path_name);
 
        this.pathweaver = pathweaver;
        this.drivetrain = drivetrain; 
        trajectory = pathweaver.getTrajectory();
        pose2d = drivetrain.getPose(); // may need to be constantly called???
        ramsete_controller = new RamseteController(new Constants.Autonomous().kRamseteB, new Constants.Autonomous().kRamseteZeta);
        SMF = new SimpleMotorFeedforward(new Constants.Drive().ksVolts, new Constants.Drive().kvVoltSecsPerMeter, new Constants.Drive().kaVoltSecsPerMeterSquared);

        //Once everthing has been done, call the 
    }

    private RamseteCommand getRamseteCommand()
    {
        return new RamseteCommand
        (
            trajectory, pose2d, ramsete_controller, SMF,
            new Constants.Drive().kDriveKinematics, drivetrain.getWheelSpeeds(), leftController, 
            rightController, outputVolts, pathweaver
        );
    }
}
