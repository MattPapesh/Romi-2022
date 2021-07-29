package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
//import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Constants;
import frc.robot.subsystems.*;

public class Autonomous extends CommandBase
{
    private RamseteCommand ramsete_command = null;
    //private Command command = null;
    
    private pathweaver pathweaver = null;
    private Drivetrain drivetrain = null; 

    public Autonomous(Drivetrain drivetrain, pathweaver pathweaver, String path_name)
    {
        addRequirements(pathweaver, drivetrain); 
        pathweaver.begin(path_name);
        drivetrain.resetEncoders();
        drivetrain.resetGyro();
    
        this.pathweaver = pathweaver; 
        this.drivetrain = drivetrain; 

        ramsete_command = 
        new RamseteCommand 
        (
            pathweaver.getTrajectory(), drivetrain::getPose, 
            new RamseteController(Constants.Autonomous.kRamseteB, Constants.Autonomous.kRamseteZeta), 
            new SimpleMotorFeedforward(Constants.Drive.ksVolts, Constants.Drive.kvVoltSecsPerMeter, Constants.Drive.kaVoltSecsPerMeterSquared),
            Constants.Drive.kDriveKinematics, drivetrain::getWheelSpeeds, 
            new PIDController(Constants.Drive.kpDriveVelocity, Constants.Drive.kiDriveCoefficient, Constants.Drive.kdDriveCoefficient), 
            new PIDController(Constants.Drive.kpDriveVelocity, Constants.Drive.kiDriveCoefficient, Constants.Drive.kdDriveCoefficient),
            drivetrain::tankDriveVolts, drivetrain 
        );
    }

    public Command getAutonomousCommand(){drivetrain.resetOdometry(pathweaver.getTrajectory().getInitialPose()); 
        return new InstantCommand(() -> {drivetrain.resetOdometry(pathweaver.getTrajectory().getInitialPose());}, drivetrain)
    .andThen(ramsete_command)
    .andThen(new InstantCommand(() -> {drivetrain.tankDriveVolts(0, 0);}, drivetrain)); }
}
