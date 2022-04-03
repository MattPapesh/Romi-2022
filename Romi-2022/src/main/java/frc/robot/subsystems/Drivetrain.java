package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;

import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Constants;
import frc.robot.sensors.RomiGyro;

public class Drivetrain extends SubsystemBase {
  
  final RomiGyro gyro = new RomiGyro(); 
  final Field2d field_2d = new Field2d();
  final DifferentialDriveOdometry drive_odometry = new DifferentialDriveOdometry(gyro.getRotation2d());

  final double RPM_TO_MS_RATIO = (Constants.Drive.kWheelDiameterMeters* Math.PI) / 60; // RPM to m/s

  final Spark DRIVETRAIN_LEFT = new Spark(0); 
  final Spark DRIVETRAIN_RIGHT = new Spark(0);

  DifferentialDrive differential_drive = null;
  MotorControllerGroup left_motor_controller = null;
  MotorControllerGroup right_motor_controller = null;

  RelativeEncoder left_encoder = (RelativeEncoder)new Encoder(1, 4);
  RelativeEncoder right_encoder = (RelativeEncoder)new Encoder(2, 2); 
  
  public Drivetrain(){
    
    DRIVETRAIN_RIGHT.setInverted(true);
    differential_drive = new DifferentialDrive(DRIVETRAIN_LEFT, DRIVETRAIN_RIGHT);
  }

  public void TankDrive(double left_speed, double right_speed)
  {
    if(differential_drive != null)
    {
      differential_drive.tankDrive(left_speed, right_speed);
    }
  }

  public void resetOdometry(Pose2d pose) {
    drive_odometry.resetPosition(pose, gyro.getRotation2d());
  }

  public void tankDriveVolts(double left_volts, double right_volts) {
    left_motor_controller.setVoltage(left_volts);
    right_motor_controller.setVoltage(right_volts);
    differential_drive.feed();
  }

  public Pose2d getPose() {
    return drive_odometry.getPoseMeters();
  }

  public DifferentialDriveWheelSpeeds getWheelSpeeds() { 
    return new DifferentialDriveWheelSpeeds(left_encoder.getVelocity() * RPM_TO_MS_RATIO, right_encoder.getVelocity() * RPM_TO_MS_RATIO);
  }

  public double getGyroAngleZ() {
    return gyro.getAngleZ();
  }

  public void resetGyro() {
    gyro.reset();
  }

  public void setMaxOutput(double max_output) {
    differential_drive.setMaxOutput(max_output);
  }

  public void zeroHeading() {
    gyro.reset();
  }

  public double getHeading() {
    return gyro.getRotation2d().getDegrees();
  }

  public double getTurnRate() {
    return gyro.getRate();
  }

  @Override
  public void periodic() {
    drive_odometry.update(gyro.getRotation2d(), left_encoder.getPosition(), right_encoder.getPosition());
    field_2d.setRobotPose(getPose());
  }

  @Override
  public void simulationPeriodic() {}
}